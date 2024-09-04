package com.github.barbodh.madgridapi.game.heartbeat;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class PlayerTimeoutEvent extends ApplicationEvent {
    private final String playerId;

    public PlayerTimeoutEvent(Object source, String playerId) {
        super(source);
        this.playerId = playerId;
    }
}
