package org.windwant.registry;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.agent.model.NewService;
import com.ecwid.consul.v1.agent.model.Service;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    public void doDeregister(String id) {
        ConsulClient consul = new ConsulClient(host, port);
        consul.agentServiceDeregister(id);
    }

    @Override
    public void doDeregisterByName(String... names) {
        ConsulClient consul = new ConsulClient(host, port);
        Map<String, Service> services = consul.getAgentServices().getValue().values().stream().collect(Collectors.toMap(Service::getService, Function.identity(), (key1, key2) -> key2));
        for (String temp: names) {
            if(services.get(temp) != null){
                consul.agentServiceDeregister(services.get(temp).getId());
            }
        }
    }
}
