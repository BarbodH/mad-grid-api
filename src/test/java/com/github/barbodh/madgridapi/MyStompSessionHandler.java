package com.github.barbodh.madgridapi;

import com.github.barbodh.madgridapi.lobby.LobbyNotification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import java.util.concurrent.CountDownLatch;

@Slf4j
public class MyStompSessionHandler extends StompSessionHandlerAdapter {
    private final CountDownLatch latch;

    public MyStompSessionHandler(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        log.info("New session: {}", session.getSessionId());

        IncomingPlayerClientPayload payload = new IncomingPlayerClientPayload();
        payload.setId("55");
        payload.setGameMode(0);

        session.send("/game/seek-opponent", payload);
        latch.countDown();
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        exception.printStackTrace();
        latch.countDown();
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        log.info("Received: {}", ((LobbyNotification) payload).getMessage());
    }
}
