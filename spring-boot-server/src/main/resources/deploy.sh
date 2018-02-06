#!/bin/sh

SERVER_IP=192.168.115.129
USER=jekins
SERVER_PORT=22


echo "服务器是"${SERVER_IP}",端口是"$SERVER_PORT
echo "建立临时拷贝目录"
rm -rf ~/MyDeploy
mkdir -p ~/MyDeploy

scp /root/.jenkins/workspace/sboot/target/spring-boot-test-1.0-SNAPSHOT-test.zip ~/MyDeploy
echo "拷贝业务服务文件成功"

echo "尝试优雅退出服务"
pid=$(ps -ef |grep 'BootSpring' | grep -v 'grep'|awk '{print $2}')
kill -9 $pid

sleep 3
echo "建立部署目录"
rm -rf /work/MyDeploy
echo "文件从临时目录拷贝至部署目录"
cp -r ~/MyDeploy /work

echo "解压服务文件"
cd /work/MyDeploy
unzip spring-boot-test-1.0-SNAPSHOT-test.zip -d sboot
rm -f spring-boot-test-1.0-SNAPSHOT-test.zip

echo "启动服务"
cd /work/MyDeploy/sboot
chmod 777 *
sed -i 's/\r$//' startup.sh && sed -i 's/java/\/usr\/share\/jdk1.8\/jre\/bin\/java/g' startup.sh
./startup.sh >/dev/null 2>&1
