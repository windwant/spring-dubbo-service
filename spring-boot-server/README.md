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

    student:

    CREATE TABLE `student` (
      `id` int(11) NOT NULL AUTO_INCREMENT,
      `name` varchar(100) DEFAULT '',
      `sex` enum('0','1') DEFAULT NULL,
      PRIMARY KEY (`id`)
    ) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

    score:

    CREATE TABLE `score` (
      `id` int(11) NOT NULL AUTO_INCREMENT,
      `stu_id` int(11) NOT NULL,
      `sub_id` int(11) NOT NULL,
      `score` double(15,0) DEFAULT NULL,
      UNIQUE KEY `id` (`id`)
    ) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

    subject:

    CREATE TABLE `subject` (
      `id` int(11) NOT NULL,
      `name` varchar(100) DEFAULT NULL,
      PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

    work:

    CREATE TABLE `work` (
      `id` int(11) NOT NULL AUTO_INCREMENT,
      `sub_id` int(11) NOT NULL,
      `name` varchar(255) DEFAULT NULL,
      `content` text,
      PRIMARY KEY (`id`),
      FULLTEXT KEY `content_full` (`content`)
    ) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;



    测试：/stu/1  /score/1

        /stu/1?s=1 /score/1?s=1

20: ehcache 二级缓存配置 xml 注解

21: 分页处理：传递Page参数 或者继承Page的对象参数

    测试：http://localhost:8082/stu/0?page=3&limit=2

22: acturtor 项目监控信息获取

    原生端点：

    应用配置：应用配置、环境变量、自动化配置报告等配置类信息，

    /env 可用环境属性 key password secret ****展示

    /autoconfig 自动化配置报告，所有自动化配置候选项及是否满足自动化配置的各个先决条件；positiveMatches成功匹配的，negativeMatches匹配不成功

    /beans 创建的所有bean

    /configprops 配置的属性信息报告

    /mappings 控制器映射关系

    /info 应用自定义信息，需要在应用属性文件中配置

    度量指标：运行监控度量指标，内存，线程，请求等

    /metrics 度量指标，可以细化请求 : /metrics/mem.free

    /health 健康指标信息

    /dump 线程信息

    /trace http跟踪信息

    操控类：/shutdown post请求关闭应用

    属性配置endpoints.shutdown.enabled=true