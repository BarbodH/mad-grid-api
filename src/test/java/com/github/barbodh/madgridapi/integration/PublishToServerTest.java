package com.github.barbodh.madgridapi.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.lang.NonNull;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.scheduling.concurrent.DefaultManagedTaskScheduler;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"spring.main.allow-bean-definition-overriding=true"})
public class PublishToServerTest {
    @LocalServerPort
    private int port;
    private final MappingJackson2MessageConverter converter;

    @Autowired
    public PublishToServerTest(MappingJackson2MessageConverter converter) {
        this.converter = converter;
    }

    @Test
    public void testPublishPlayerToLobby() throws InterruptedException {
        var timeout = 3; // Seconds
        var webSocketClient = new StandardWebSocketClient();
        var stompClient = new WebSocketStompClient(webSocketClient);
        stompClient.setMessageConverter(converter);
        stompClient.setTaskScheduler(new DefaultManagedTaskScheduler());

        var sessionHandler = new ExtendedStompSessionHandler() {
            @Override
            public void afterConnected(StompSession session, @NonNull StompHeaders connectedHeaders) {
                session.send("/game/seek-opponent", new IncomingPlayerClientPayload("0", 0));
                getLatch().countDown();
            }

            @Override
            public void handleException(@NonNull StompSession session, StompCommand command, @NonNull StompHeaders headers, @NonNull byte[] payload, @NonNull Throwable exception) {
                fail(exception.getMessage());
            }
        };
        stompClient.connectAsync(String.format("ws://localhost:%d/ws", port), sessionHandler);

        boolean messageSent = sessionHandler.getLatch().await(timeout, TimeUnit.SECONDS);
        assertTrue(messageSent);
    }
}
