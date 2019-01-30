# rest

rest api maven项目

1. servlet、listener、interceptor、filter配置
2. jmx监控MBean
3. 定时任务配置
4. aop配置
5. ftp服务
6. 测试
7. Metrics监控
8. 参数验证
    测试：/hellox?name=
9. 跨域处理
10. 添加shiro权限控制

    测试用户：userName: admin passwd: admin
             
    验证码：/login/checkcode
    
    登录：/login?userName=&passwd=&code=
    
    测试：/hellox?name=
11. 导出Excel

    测试：/export

12. 服务启动注册到consul；并测试获取redis服务，初始化redis资源；consul 监控redis服务 ；reids分布式锁；注意consul客户端和consul程序版本问题

13. SPI机制: org/windwant/spring/core/spi

    运行时配置：META-INF/services/org.windwant.spring.core.spi.Calc

14. static资源， “/” 映射

15. dianping Cat 监控

16: acturtor 项目监控信息获取

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