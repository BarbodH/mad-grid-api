package com.github.barbodh.madgridapi.integration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import java.util.concurrent.CountDownLatch;

@Getter
@AllArgsConstructor
public class ExtendedStompSessionHandler extends StompSessionHandlerAdapter {
    private final CountDownLatch latch;

    public ExtendedStompSessionHandler() {
        latch = new CountDownLatch(1);
    }
}
