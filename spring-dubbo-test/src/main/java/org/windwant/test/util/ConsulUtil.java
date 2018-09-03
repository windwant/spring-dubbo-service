package org.windwant.test.util;

import com.google.common.net.HostAndPort;
import com.orbitz.consul.Consul;
import com.orbitz.consul.KeyValueClient;
import com.orbitz.consul.model.kv.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.windwant.util.ConfigUtil;
import org.windwant.util.NetUtil;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class ConsulUtil {

	private static final Logger logger = LoggerFactory.getLogger(ConsulUtil.class);

    private static KeyValueClient keyValueClient;

    static {
        keyValueClient = Consul.builder().withHostAndPort(
                HostAndPort.fromParts(ConfigUtil.get("consul.host"),
                        ConfigUtil.getInteger("consul.port")))
                .build()
                .keyValueClient();
    }

    private ConsulUtil() {
    }

    private static KeyValueClient getClient() {
        return keyValueClient;
    }

    public static boolean putRequestChannel(String root, String requestCode) {
        return getClient().putValue(root + "/" + "channel-" + requestCode, requestCode);
    }

    public static void removeRequestChannel(String requestCode) {
        getClient().deleteKey("channel-" + requestCode);
    }

    /**
     * 服务启动 清除本及注册的服务及旧的通道信息
     */
    public static List getServiceChannel(){
        List<Value> channels = getClient().getValues(NetUtil.getHost());

        return channels.stream().map(Value::getValueAsString).map(Optional::get).collect(Collectors.toList());
    }
}
