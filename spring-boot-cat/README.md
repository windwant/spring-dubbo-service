将resources下

1. com内的 cat-client 资源放到 maven 本地资源库对应位置

2. cat.war 放到tomcat路径下

3. data 内为配置文件

    linux下 放到对应路径下 /data/appdatas/cat

    windows下 放到 tomcat所在盘根路径下

4. 项目下 添加 resources/META-INF/app.properties 文件 内容：app.name=cat【配置的名称】

5. 启动tomcat 访问 http://localhost:8080/cat/r/