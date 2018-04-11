# spring-dubbo-push

监控redis msg_push 消息队列 获取通过wsproxy服务发送

启动顺序：pring-boot-server=》spring-dubbo-wsproxy=》spring-dubbo-push

测试：spring-dubbo-test模块

启动 JSWebSocketProxyProtoTestServer

=》浏览器访问 http://localhost:8989/

=》填写连接标识、发送内容，点击发送

=》运行PushMsgRedisTest向Redis msg_push添加消息

=》观察页面消息结果





