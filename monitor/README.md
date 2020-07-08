## dingping cat 监控

### resources下资源处理：

  >*  cat.war 放到tomcat路径下
>    * car.war生成步骤参考 https://github.com/dianping/cat

  >*  data 内为配置文件
>    * linux下 放到对应路径下 /data/appdatas/cat
>    * windows下 放到 tomcat所在盘根路径下

  >*  项目下 添加 resources/META-INF/app.properties 文件 内容：app.name=cat【配置的名称】

  >*  启动tomcat 访问 http://localhost:8080/cat/r/

## Arthas java诊断工具

  >*  直接使用lib下 arthas-boot.jar包启动： java -jar arthas-boot.jar

  >*  详细介绍见: Arthas Alibaba 开源 Java 诊断工具 https://www.cnblogs.com/niejunlei/p/10323019.html

  >*  Arthas目前支持Web Console，用户在attach成功之后，可以直接访问：http://127.0.0.1:8563/。可以填入IP，远程连接其它机器上的arthas。