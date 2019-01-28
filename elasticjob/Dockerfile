from centos:latest

ADD jdk-8u111-linux-x64.tar.gz /usr/java/

ADD target/elasticjob-1.0-SNAPSHOT-prod.tar.gz /usr/dubbo-server/elasticjob/

#RUN yum install -y dos2unix && dos2unix /usr/dubbo-server/elasticjob/startup.sh

RUN cd /usr/dubbo-server/elasticjob/ && sed -i 's/java/\/usr\/java\/jdk1.8.0_111\/bin\/java/g' startup.sh startupd.sh && chmod 777 startup.sh startupd.sh

ENTRYPOINT cd /usr/dubbo-server/elasticjob/ && sh startupd.sh