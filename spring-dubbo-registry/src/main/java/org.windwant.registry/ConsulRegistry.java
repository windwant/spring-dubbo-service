package org.windwant.registry;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.agent.model.NewService;

/**
 * Created by Administrator on 18-4-9.
 */
public class ConsulRegistry implements Registry {

    private String host;

    private Integer port;

    public ConsulRegistry(String host, Integer port){
        this.host = host;
        this.port = port;
    }

    @Override
    public String doRegister(RegistryService service) {
        ConsulClient consul = new ConsulClient(host, port);
        NewService newService = new NewService();
        newService.setId(service.getId());
        newService.setName(service.getName());
        newService.setPort(service.getPort());
        newService.setAddress(service.getHost());
        NewService.Check check = new NewService.Check();
        check.setInterval("6s");
        check.setTimeout("3s");
        check.setTcp(service.getHost() + ":" + service.getPort());

        newService.setCheck(check);
        consul.agentServiceRegister(newService);
        return service.getId();
    }

    @Override
    public void doUnRegister(String id) {
        ConsulClient consul = new ConsulClient(host, port);
        consul.agentServiceDeregister(id);
    }
}
