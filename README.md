# spring-dubbo-service  微服务

一、服务模块

1、spring-boot-server

  1). servlet、listener、interceptor、filter配置
  2). mybatis配置集成，多数据源
  3). jmx监控MBean
  4). 定时任务配置
  5). aop配置
  6). ftp服务
  7). 测试
  8). Metrics监控
  9). 参数验证
  10). 跨域处理
  11). 添加shiro权限控制
  12). 导出Excel
  13). 服务启动注册到consul；并测试获取redis服务，初始化redis资源；consul 监控redis服务 ；reids分布式锁；注意consul客户端和consul程序版本问题
  14). SPI机制: org/windwant/spring/core/spi
  15). static资源， “/” 映射
  16). 使用druid数据源连接池；配置druid数据源监控：http://localhost:8081/druid/index.html
  17). dubbo server

2、spring-dubbo-test

  测试dubbo rpc服务

  测试websocket protobuf

3、spring-dubbo-common

  api 接口

4、spring-dubbo-proxy

  netty rpc 服务代理，处理业务消息解析 分发

5、spring-dubbo-wsproxy

  netty rpc websocket 服务代理

6、spring-dubbo-protocal

  protobuf 资源

7、spring-dubbo-client

  proxy websocketproxy 测试工程

8、spring-dubbo-elasticjob

  测试spring-boot-server druid监控定时任务


9、spring-dubbo-registry

  consul服务注册：示例

  RegistryFactory.INSTANCE.
                     getRegistry(RegistryFactory.CONSUL).
                     doRegister(RegistryService.build(ConfigUtil.get("service.name"),
                             WSUtil.getHost(),
                             ConfigUtil.getInteger("websocket.connect.port"),
                             ConfigUtil.get("service.version")));

十、spring-dubbo-push

  消息监听推送


二、服务部署：

1. consul：

部署consul服务

启动consul：

    consul agent -server -bootstrap-expect=1  -data-dir=data -node=server0 -bind=127.0.0.1 -client 0.0.0.0 -ui

2. redis：

部署redis服务

启动redis

3. zookeeper

部署zookeeper服务

启动zookeeper

