# spring-boot-server

springboot maven项目

1. servlet、listener、interceptor、filter配置
2. mybatis配置集成，多数据源
3. jmx监控MBean
4. 定时任务配置
5. aop配置
6. ftp服务
7. 测试
8. Metrics监控
9. 参数验证
    测试：/hellox?name=
10. 跨域处理
11. 添加shiro权限控制

    测试用户：userName: admin passwd: admin
             
    验证码：/login/checkcode
    
    登录：/login?userName=&passwd=&code=
    
    测试：/hellox?name=
12. 导出Excel

    测试：/export

13. 服务启动注册到consul；并测试获取redis服务，初始化redis资源；consul 监控redis服务 ；reids分布式锁；注意consul客户端和consul程序版本问题

14. SPI机制: org/windwant/spring/core/spi

    运行时配置：META-INF/services/org.windwant.spring.core.spi.Calc

15. static资源， “/” 映射

16. 使用druid数据源连接池；配置druid数据源监控：http://localhost:8081/druid/index.html

17. Dubbo rpc

18. dianping Cat 监控

19. mybatis 级联查询 一对一 一对多 注解配置及xml配置方式

    测试表：

    stu：

    CREATE TABLE `stu` (
          `id` int(11) NOT NULL AUTO_INCREMENT,
          `name` varchar(100) DEFAULT '',
          PRIMARY KEY (`id`)
        ) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

    score：

    CREATE TABLE `score` (
      `id` int(11) NOT NULL AUTO_INCREMENT,
      `stu_id` int(11) NOT NULL,
      `item` varchar(255) DEFAULT NULL,
      `score` double(15,0) DEFAULT NULL,
      UNIQUE KEY `id` (`id`)
    ) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

    测试：/stu/1  /score/1

