package com.github.barbodh.madgridapi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"spring.main.allow-bean-definition-overriding=true"})
public class IntegrationTest {
    @LocalServerPort
    private int port;
    @Autowired
    private MappingJackson2MessageConverter messageConverter;

    @Test
    public void test1() throws ExecutionException, InterruptedException, TimeoutException {
        var webSocketClient = new StandardWebSocketClient();
        var stompClient = new WebSocketStompClient(webSocketClient);

        stompClient.setMessageConverter(messageConverter);

        String url = "ws://localhost:" + port + "/ws";
        var completableFuture = new CompletableFuture<StompSession>();

        var sessionHandler = new StompSessionHandlerAdapter() {
            public void afterConnected(StompSession session) {
                session.subscribe("/player/{playerId}/lobby/notify", new StompFrameHandler() {
                    @Override
                    public Type getPayloadType(StompHeaders headers) {
                        return String.class;
                    }

                    @Override
                    public void handleFrame(StompHeaders headers, Object payload) {

                    }
                });
            }
        };

        stompClient.connectAsync(url, sessionHandler);
        var session = completableFuture.get(10, TimeUnit.SECONDS);
    }

    @Test
    public void test2() {

    }
}
