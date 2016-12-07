package org.windwant.spring.websocket;

import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by windwant on 2016/12/7.
 */
@ServerEndpoint("/myws")
@Component
public class WebSocketSvr {

    private static AtomicInteger onLine = new AtomicInteger(0);

    private static CopyOnWriteArraySet<WebSocketSvr> websockets = new CopyOnWriteArraySet<>();

    private Session session;

    @OnOpen
    public void onOpen(Session session){
        this.session = session;
        websockets.add(this);
        System.out.println("current online: " + onLine.incrementAndGet());
    }

    @OnClose
    public void onClose(){
        System.out.println("current online: " + onLine.decrementAndGet());
        websockets.remove(this);
    }

    @OnError
    public void onError(Throwable t){
        System.out.println("websocket error: " + t.getMessage());
        websockets.remove(this);
    }

    @OnMessage
    public void onMessage(String message, Session session){
        System.out.println("incoming message: " + message);
        websockets.forEach(e -> {
            try {
                e.session.getBasicRemote().sendText("system return: " + Math.random());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
    }
}
