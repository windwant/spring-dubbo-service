#!/bin/sh

SERVER_IP=192.168.115.129
USER=jekins
SERVER_PORT=22


echo "服务器是"${SERVER_IP}",端口是"$SERVER_PORT
echo "建立临时目录"
rm -rf ~/MyDeploy
mkdir -p ~/MyDeploy

scp /root/.jenkins/workspace/serverx/target/serverx-1.0-test.zip ~/serverx
echo "复制文件成功"

echo "尝试退出服务"
pid=$(ps -ef |grep 'ServicexApplication' | grep -v 'grep'|awk '{print $2}')
kill -9 $pid

sleep 3
echo "建立部署目录"
rm -rf /work/serverx
echo "文件复制到部署目录"
cp -r ~/serverx/work

echo "解压服务文件"
cd /work/MyDeploy
unzip serverx-1.0-test.zip -d serverx
rm -f serverx-1.0-test.zip

echo "启动服务"
cd /work/MyDeploy/serverx
chmod 777 *
sed -i 's/\r$//' startup.sh && sed -i 's/java/\/usr\/share\/jdk1.8\/jre\/bin\/java/g' startup.sh
./startup.sh >/dev/null 2>&1
