package org.windwant.spring.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * Created by windwant on 2016/12/7.
 */
@Configuration
public class EndpointConfig {

    @Bean
    public ServerEndpointExporter serverEndpointExporter (){
        return new ServerEndpointExporter();
    }
}
