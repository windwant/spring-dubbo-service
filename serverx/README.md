# serverx

业务服务 dubbo服务 分布式服务测试
以下几点说明：

1. DubboTXServicexImpl 服务x； dubbo-tx-servicex.xml 发布
2. DubboTXServiceyImpl 服务y dubbo-tx-servicey.xml 发布
3. DubboTXBusiServiceImpl 服务调用
4. DBConfig 定义不同的数据源；关键 DataSourceProxy
5. 配置：GlobalTransactionScanner(String applicationId, String txServiceGroup)
   txServiceGroup 对应 application.conf   vgroup_mapping.dubbo_service_tx_group = "localRgroup"
7. 依赖说明：fescar使用的dubbo 存在filter定义问题
           <dependency>
               <groupId>com.alibaba.fescar</groupId>
               <artifactId>fescar-dubbo</artifactId>
               <version>0.1.3</version>
           </dependency>
8. mybatis 配置 config.setDefaultExecutorType(ExecutorType.SIMPLE);不能为 BATCH
9. 。。。