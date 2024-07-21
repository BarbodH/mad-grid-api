package com.github.barbodh.madgridapi.lobby;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class LobbyController {
    private final SimpMessagingTemplate messagingTemplate;
    private final LobbyService lobbyService;

    @MessageMapping("/seek-opponent")
    public void handleIncomingUser(@Payload IncomingPlayer incomingPlayer) {
        final String notificationUrl = "/lobby/notify";
        lobbyService.matchPlayer(incomingPlayer).ifPresentOrElse(
                multiplayerGame -> {
                    messagingTemplate.convertAndSendToUser(
                            multiplayerGame.getPlayer1().getId(),
                            notificationUrl,
                            new LobbyNotification(multiplayerGame)
                    );
                    messagingTemplate.convertAndSendToUser(
                            multiplayerGame.getPlayer2().getId(),
                            notificationUrl,
                            multiplayerGame
                    );
                },
                () -> messagingTemplate.convertAndSendToUser(
                        incomingPlayer.getId(),
                        notificationUrl,
                        new LobbyNotification()
                )
        );
    }
}
