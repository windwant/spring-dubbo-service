package org.windwant.client;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.util.resource.Resource;
import org.windwant.common.util.ConfigUtil;

/**
 * Created by Administrator on 2018/2/12.
 * websocket protobuf 内嵌jetty 测试服务器
 * 访问地址：localhost:8989
 */
public class JSWebSocketProxyProtoTestServer {

    public static void main(String[] args) throws Exception {
        Server server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(8989);
        server.setConnectors(new Connector[]{connector});

        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(true);
        resourceHandler.setWelcomeFiles(new String[]{"websocket-" + ConfigUtil.get("evn") + ".html"});//welcome file list
        Resource resource = Resource.newClassPathResource("static"); //static resource root
        resourceHandler.setBaseResource(resource);

        server.setHandler(resourceHandler);
        server.start();
        server.join();

    }
}
