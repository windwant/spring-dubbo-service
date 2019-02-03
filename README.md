spring-dubbo-service  微服务
============================


一、服务模块
---------------------

## 1、server

  >*  配置：servlet、listener、interceptor、filter、aop、 定时任务
  >*  mybatis配置集成，多数据源；级联查询 一对一 一对多 注解配置及xml配置方式；分页处理：传递Page参数 或者继承Page的对象参数
  >*  druid数据源连接池；配置druid数据源监控：http://localhost:8081/druid/index.html
  >*  ehcache 二级缓存配置 xml 注解 
  >*  ftp服务；Excel相关处理；jmx监控MBean
  >*  测试；ArchUnit
  >*  参数验证；跨域处理；shiro权限控制；static资源， “/” 映射 
  >*  服务启动注册到consul；并测试获取redis服务，初始化redis资源；consul 监控redis服务 ；reids分布式锁；注意consul客户端和consul程序版本问题
  >*  SPI机制: org/windwant/spring/core/spi
  >*  dubbo server
  >*  监控：dianping Cat 监控；acturtor 项目监控信息获取；Metrics监控

## 2、test

  >*  测试dubbo rpc服务

  >*  测试websocket protobuf

## 3、common

  >*  api 接口

## 4、rpcproxy

  >*  netty rpc 服务代理，处理业务消息解析 分发

## 5、wsproxy

  >*  netty rpc websocket 服务代理

## 6、protocal

  >*  protobuf 资源

## 7、client

  >*  proxy websocketproxy 测试工程

## 8、elasticjob

  >*  测试spring-boot-server druid监控定时任务


## 9、registry

  >*  consul服务注册：示例

  >*  RegistryFactory.INSTANCE.
                     getRegistry(RegistryFactory.CONSUL).
                     doRegister(RegistryService.build(ConfigUtil.get("service.name"),
                             WSUtil.getHost(),
                             ConfigUtil.getInteger("websocket.connect.port"),
                             ConfigUtil.get("service.version")));

## 10、bus

  >*  消息监听推送

## 11、rest

  >*  rest api

## 12、serverx

  >*  fescar 分布式事务应用

## 13、clientx

  >*  fescar 分布式事务应用 客户端调用实例


二、服务部署：
-----------------

## 1. consul：

>* 部署consul服务

>* 启动consul：
>    * consul agent -server -bootstrap-expect=1  -data-dir=data -node=server0 -bind=127.0.0.1 -client 0.0.0.0 -ui

## 2. redis：

>* 部署redis服务

>* 启动redis

## 3. zookeeper

>* 部署zookeeper服务

>* 启动zookeeper

## 4. mongo

>* 部署mongo服务

>* 启动mongo

三、docker
-----------------

>* docker-compose 启动相应服务

>* docker network create --subnet=192.168.0.0/16 mynet 创建网络

>* rest：
>    * docker build -t rest .
>    * docker run -it -d --network mynet -p 8086:8086 rest
>    * curl http://localhost:8086/hello/roger

>* server：
>    * docker build -t server .
>    * docker run -it -d --network mynet -p 8086:8086 server
>    * curl http://localhost:8083/info
