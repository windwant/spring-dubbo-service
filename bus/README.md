# spring-dubbo-bus：消息总线服务 rabbitmq redis

监控 bus_queue 消息队列 获取消息并通过wsproxy服务发送

启动顺序：spring-dubbo-server=》spring-dubbo-wsproxy=》spring-dubbo-bus

测试：spring-dubbo-test模块

启动 JSWebSocketProxyProtoTestServer

=》浏览器访问 http://localhost:8989/

=》点击发送 实现通信记录

=》运行MsgQueueMsgTest向Redis rabbitmq bus_queue添加消息

=》观察页面消息结果





