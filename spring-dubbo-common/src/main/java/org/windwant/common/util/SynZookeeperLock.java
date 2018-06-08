package org.windwant.common.util;

import org.apache.zookeeper.*;
import org.apache.zookeeper.ZooDefs.Ids;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * zookeeper 分布式锁
 */
public class SynZookeeperLock {
    private static final int SESSION_TIMEOUT = 30000;

    public static ZooKeeper getInstance(String domain){
        try {
            CountDownLatch c = new CountDownLatch(1);
            ZooKeeper zk = new ZooKeeper(domain, SESSION_TIMEOUT, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    if (event.getState() == Watcher.Event.KeeperState.SyncConnected) {
                        c.countDown(); // 唤醒当前正在执行的线程
                    }
                }
            });
            //阻塞直到连接完成
            c.await();
            return zk;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 获取分布式锁
     * 使用临时节点，避免进程获取锁后，down掉未释放锁问题
     * @param domain
     * @param path
     * @param data
     * @param c
     */
    public static void tryLock(String domain, String path, byte[] data, CountDownLatch c){
        //每次获取锁使用新的会话连接
        ZooKeeper zk = getInstance(domain);
        zk.create(path, data, Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL, (rc, path1, ctx, name) -> {
            //节点创建成功，获取锁
            if (rc == 0) {
                System.out.println(Thread.currentThread().getName() + "：result " + rc + " lock " + path + ", created!");
                try {
                    //模拟获取锁后3秒释放
                    Thread.sleep(3000);
                    System.out.println(Thread.currentThread().getName() + "：task complete，try release lock！");
                    zk.delete(path, -1, (rc1, path2, ctx1) -> {
                        if(rc1 == 0){
                            System.out.println(Thread.currentThread().getName() + "：lock released！");
                        }
                    }, null);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //释放等待
                c.countDown();
            } else if(rc == -110) {//节点已存在，则说明锁已被其它进程获取，则创建watch，并阻塞等待
                System.out.println(Thread.currentThread().getName() + "：result " + rc + " lock " + path + " already created, waiting!");
                try {
                    zk.exists(path, event -> {
                        //watch 到锁删除事件，则触发重新获取锁
                        if (event.getType().equals(Watcher.Event.EventType.NodeDeleted)) {
                            System.out.println(Thread.currentThread().getName() + "：get node deleted event! try lock!");
                            //释放连接，避免服务器因为连接数限制
                            try {
                                zk.close();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            SynZookeeperLock.tryLock(domain, path, data, c);
                            c.countDown();
                        }
                    });
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else {
                //-4 -112
                System.out.println(Thread.currentThread().getName() + ": connection lose or session invalid");
                c.countDown();
//                tryLock(domain, path, data, c);
            }
        }, new Object());
        try {
            //阻塞等待结果
            c.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String lockPath = "/testlock";
        byte[] lock = "lock".getBytes();
        String domain = "127.0.0.1:2181";
        //测试获取锁线程 注意服务器最大连接数限制
        for (int i = 0; i < 20; i++) {
            Thread tmp = new Thread( () -> tryLock(domain, lockPath, lock, new CountDownLatch(1)));
            tmp.start();
        }
    }
}
