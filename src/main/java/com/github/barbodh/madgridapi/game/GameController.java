package com.github.barbodh.madgridapi.game;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class GameController {
    private final SimpMessagingTemplate messagingTemplate;
    private final GameService gameService;

    @MessageMapping("/update")
    public void handleGameUpdate(@Payload GameUpdate gameUpdate) {
        var updatedGame = gameService.updateGame(gameUpdate);
        messagingTemplate.convertAndSendToUser(gameUpdate.getPlayerId(), "/game/notify", updatedGame);
    }
}
