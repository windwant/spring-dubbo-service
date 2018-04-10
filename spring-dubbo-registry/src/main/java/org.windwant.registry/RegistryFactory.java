package org.windwant.registry;

import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Administrator on 18-4-9.
 */
public class RegistryFactory {
    public static final RegistryFactory INSTANCE = new RegistryFactory();

    private ConcurrentHashMap<String, Registry> registries = new ConcurrentHashMap();

    public static final String CONSUL = "consul";
    public static final String REDIS = "redis";

    private static String CONSUL_HOST;
    private static Integer CONSUL_PORT;
    public RegistryFactory() {
        Properties properties = new Properties();
        try {
            properties.load(RegistryFactory.class.getClassLoader().getResourceAsStream("registry.properties"));
            CONSUL_HOST = properties.getProperty("consul.host");
            CONSUL_PORT = Integer.parseInt(properties.getProperty("consul.port"));
        }catch (Exception e){
            e.getStackTrace();
        }

        synchronized (RegistryFactory.class){
            if(registries.get(CONSUL) == null) {
                registries.put(CONSUL, new ConsulRegistry(CONSUL_HOST, CONSUL_PORT));
            }
        }
    }

    public RegistryFactory getInstance() {
        return INSTANCE;
    }

    public Registry getRegistry(String name){
        return registries.get(name);
    }
}
