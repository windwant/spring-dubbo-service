package org.windwant.registry;

import java.util.UUID;

/**
 * Created by Administrator on 18-4-9.
 */
public class RegistryService {

    private String id;

    private String name;

    private String host;

    private Integer port;

    private String version;

    public static RegistryService build(String name, String host, int port, String version){
        RegistryService service = new RegistryService();
        service.setId(UUID.randomUUID().toString());
        service.setHost(host);
        service.setName(name);
        service.setPort(port);
        service.setVersion(version);
        return service;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
