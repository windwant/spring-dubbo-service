#!/bin/sh

SERVER_IP=192.168.115.129
USER=jekins
SERVER_PORT=22


echo "服务器是"${SERVER_IP}",端口是"$SERVER_PORT
echo "建立临时拷贝目录"
"rm -rf MyDeploy"
"mkdir -p MyDeploy"

echo "传输打包文件至临时目录"
#根据自己的目录结构进行改动
springBoot=/root/.jenkins/workspace/sboot/spring-boot-test-1.0-SNAPSHOT-test.zip

scp  $springBoot ~/MyDeploy
echo "拷贝业务服务文件成功"

echo "尝试优雅退出服务"
"pid=\`ps -ef|grep spring-boot-test|grep -v grep|awk '{print \$2}'\` && sudo kill \$pid"

sleep 3
echo "3秒后检查服务有无退出，若服务依旧存在，则强行杀掉"
"pid=\`ps -ef|grep spring-boot-test|grep -v grep|awk '{print \$2}'\` && [ ! -z $pid ] && sudo kill -9 \$pid"

echo "建立部署目录"
"sudo rm -rf /work/MyDeploy"
echo "文件从临时目录拷贝至部署目录"
"sudo cp -r ~/MyDeploy /work/"

echo "解压服务文件"
"cd /work/MyDeploy && sudo unzip  spring-boot-test-1.0-SNAPSHOT.jar -d sboot && sudo rm -f spring-boot-test-1.0-SNAPSHOT-test.zip"

echo "启动服务"
ssh -p $SERVER_PORT ${USER}@$SERVER_IP "cd /work/MyDeploy/sboot && sudo chmod +x startup.sh && sudo ./startup.sh >/dev/null 2>&1"