package org.windwant.wsproxy.util;

import com.google.common.net.HostAndPort;
import com.orbitz.consul.Consul;
import com.orbitz.consul.KeyValueClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.windwant.common.util.ConfigUtil;


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

    public static boolean putRequestChannel(String requestCode) {
        return getClient().putValue("channel-" + requestCode, requestCode);
    }

    public static void removeRequestChannel(String requestCode) {
        getClient().deleteKey("channel-" + requestCode);
    }
}
