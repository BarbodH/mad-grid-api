package com.github.barbodh.madgridapi.game.controller;

import com.github.barbodh.madgridapi.game.service.GameService;
import com.github.barbodh.madgridapi.game.model.GameUpdate;
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
        var updatedGame = gameService.update(gameUpdate);
        messagingTemplate.convertAndSendToUser(gameUpdate.getPlayerId(), "/game/notify", updatedGame);
    }
}
