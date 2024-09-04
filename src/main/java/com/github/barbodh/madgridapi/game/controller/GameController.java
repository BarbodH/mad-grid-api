package com.github.barbodh.madgridapi.game.controller;

import com.github.barbodh.madgridapi.game.heartbeat.HeartbeatManager;
import com.github.barbodh.madgridapi.game.heartbeat.PlayerTimeoutEvent;
import com.github.barbodh.madgridapi.game.model.GameUpdate;
import com.github.barbodh.madgridapi.game.model.PlayerHeartbeat;
import com.github.barbodh.madgridapi.game.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class GameController {
    private final SimpMessagingTemplate messagingTemplate;
    private final GameService gameService;
    private final HeartbeatManager heartbeatManager;

    @MessageMapping("/update")
    public void handleGameUpdate(@Payload GameUpdate gameUpdate) {
        updateGame(gameUpdate);
    }

    @MessageMapping("/heartbeat")
    public void receivePlayerHeartbeat(@Payload PlayerHeartbeat playerHeartbeat) {
        heartbeatManager.update(playerHeartbeat.getGameId(), playerHeartbeat.getPlayerId());
    }

    @EventListener
    public void onPlayerTimeout(PlayerTimeoutEvent event) {
        updateGame(new GameUpdate(event.getGameId(), event.getPlayerId(), false));
    }

    private void updateGame(GameUpdate gameUpdate) {
        var updatedGame = gameService.update(gameUpdate);
        messagingTemplate.convertAndSendToUser(updatedGame.getPlayer1().getId(), "/game/notify", updatedGame);
        messagingTemplate.convertAndSendToUser(updatedGame.getPlayer2().getId(), "/game/notify", updatedGame);
    }
}
