package org.windwant.wsproxy.util;

import com.orbitz.consul.Consul;
import com.orbitz.consul.KeyValueClient;
import com.orbitz.consul.option.DeleteOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.windwant.util.ConfigUtil;
import org.windwant.util.NetUtil;
import org.windwant.registry.RegistryFactory;

import java.net.MalformedURLException;
import java.net.URL;


public class ConsulUtil {

	private static final Logger logger = LoggerFactory.getLogger(ConsulUtil.class);

    private static KeyValueClient keyValueClient;

    static {
        //构造consul kv client
        try {
            keyValueClient = Consul.builder()
                    .withUrl(new URL("http", ConfigUtil.get("consul.host"),
                            ConfigUtil.getInteger("consul.port"), ""))
//                    .withHostAndPort(HostAndPort.fromParts(ConfigUtil.get("consul.host"),
//                            ConfigUtil.getInteger("consul.port")))
                    .build()
                    .keyValueClient();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private ConsulUtil() {
    }

    private static KeyValueClient getClient() {
        return keyValueClient;
    }

    //注册channel
    public static boolean putRequestChannel(String root, String requestCode) {
        return getClient().putValue(root + "/" + "channel-" + requestCode, requestCode);
    }

    //移除channel 注册
    public static void removeRequestChannel(String requestCode) {
        getClient().deleteKey("channel-" + requestCode);
    }

    /**
     * 服务启动 清除本及注册的服务及旧的通道信息
     */
    public static void initClear(){
        getClient().deleteKey(NetUtil.getHost(), DeleteOptions.RECURSE);
        RegistryFactory.INSTANCE.
                getRegistry(RegistryFactory.CONSUL).
                doDeregisterByName(NetUtil.getHost() + "/" + ConfigUtil.get("service.name"), NetUtil.getHost() + "/" + ConfigUtil.get("service.bus.name"));
    }
}
