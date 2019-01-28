from centos:latest

ADD jdk-8u111-linux-x64.tar.gz /usr/java/

ADD target/bus-1.0-SNAPSHOT-prod.tar.gz /usr/dubbo-server/bus/

#RUN yum install -y dos2unix && dos2unix /usr/dubbo-server/bus/startup.sh

RUN cd /usr/dubbo-server/bus/ && sed -i 's/java/\/usr\/java\/jdk1.8.0_111\/bin\/java/g' startup.sh startupd.sh && chmod 777 startup.sh startupd.sh

ENTRYPOINT cd /usr/dubbo-server/bus/ && sh startupd.sh