package org.windwant.spring.config;

import com.baidu.disconf.client.common.annotations.DisconfFile;
import com.baidu.disconf.client.common.annotations.DisconfFileItem;
import com.baidu.disconf.client.common.annotations.DisconfUpdateService;
import com.baidu.disconf.client.common.update.IDisconfUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * 配置中心配置文件
 * Created by Administrator on 2018/2/12.
 */
@Service
@Scope("singleton")
@DisconfUpdateService(classes = { SpringConfig.class })
@DisconfFile(filename = "config.properties") //配置中心 配置文件名
public class SpringConfig implements IDisconfUpdate {

    private static String host;

    private static Integer port;

    private static String username;

    private static String password;

    private static Integer connectTimeOut;

    @DisconfFileItem(name = "ftp.host", associateField = "host") //对应配置item
    public static String getHost() {
        return host;
    }

    public static void setHost(String host) {
        SpringConfig.host = host;
    }

    @DisconfFileItem(name = "ftp.port", associateField = "port") //对应配置item
    public static Integer getPort() {
        return port;
    }

    public static void setPort(Integer port) {
        SpringConfig.port = port;
    }

    @DisconfFileItem(name = "ftp.username", associateField = "username") //对应配置item
    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        SpringConfig.username = username;
    }

    @DisconfFileItem(name = "ftp.password", associateField = "password") //对应配置item
    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        SpringConfig.password = password;
    }

    @DisconfFileItem(name = "ftp.connectTimeOut", associateField = "connectTimeOut") //对应配置item
    public static Integer getConnectTimeOut() {
        return connectTimeOut;
    }

    public static void setConnectTimeOut(Integer connectTimeOut) {
        SpringConfig.connectTimeOut = connectTimeOut;
    }

    private static String redisHost;

    private static Integer redisPort;

    private static String consulHost;

    @DisconfFileItem(name = "consul.host", associateField = "consulHost") //对应配置item
    public static String getConsulHost() {
        return consulHost;
    }

    public static void setConsulHost(String consulHost) {
        SpringConfig.consulHost = consulHost;
    }

    @DisconfFileItem(name = "redis.host", associateField = "redisHost") //对应配置item
    public static String getRedisHost() {
        return redisHost;
    }

    public static void setRedisHost(String redisHost) {
        SpringConfig.redisHost = redisHost;
    }

    @DisconfFileItem(name = "redis.port", associateField = "redisPort") //对应配置item
    public static Integer getRedisPort() {
        return redisPort;
    }

    public static void setRedisPort(Integer redisPort) {
        SpringConfig.redisPort = redisPort;
    }

    @Override
    public void reload() throws Exception {
//        logger.info("reload proxy.server.port: {}", serverPort);
    }
}
