package org.windwant.test;

import com.google.protobuf.InvalidProtocolBufferException;
import okhttp3.*;
import okio.ByteString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.windwant.test.util.ProxyUtil;
import org.windwant.protobuf.BootRequestResponse;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 *
 * websocket protobuf 测试client 使用okhttp3
 */
public class OkhttpWebSocketProxyProtoTest {

    private static final Logger logger = LoggerFactory.getLogger(OkhttpWebSocketProxyProtoTest.class);

    private final static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .readTimeout(3000, TimeUnit.SECONDS)//设置读取超时时间
            .writeTimeout(3000, TimeUnit.SECONDS)//设置写的超时时间
            .connectTimeout(3000, TimeUnit.SECONDS)//设置连接超时时间
            .build();

    private static final String url="ws://localhost:8087/websocket";

    public static void main(String[] args) {
        Request request = new Request.Builder().url(url).build();
        WebSocket webSocket = okHttpClient.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                logger.info("websocket open... ");
                for (int i = 0; i < 100; i++) {
                    webSocket.send(ByteString.of(ProxyUtil.getByteBufBootRequest(ThreadLocalRandom.current().nextInt()).array()));
                }
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                super.onMessage(webSocket, text);
                logger.info("websocket response: {}", text);
            }

            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                super.onMessage(webSocket, bytes);
                try {
                    BootRequestResponse.BootResponse response = BootRequestResponse.BootResponse.parseFrom(bytes.toByteArray());
                    logger.info("websocket response:\r\n{}", response.toString());
                } catch (InvalidProtocolBufferException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                super.onClosing(webSocket, code, reason);
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                super.onClosed(webSocket, code, reason);
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                super.onFailure(webSocket, t, response);
            }
        });
    }
}
