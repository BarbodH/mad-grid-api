package com.github.barbodh.madgridapi.game;

import com.github.barbodh.madgridapi.lobby.IncomingPlayer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GameController {
    private final GameService gameService;

    @GetMapping("/create")
    public MultiplayerGame createTestGame() {
        var user1 = new IncomingPlayer();
        user1.setId("123");
        user1.setGameMode(GameMode.REVERSE);

        var user2 = new IncomingPlayer();
        user2.setId("987");
        user2.setGameMode(GameMode.REVERSE);

        return gameService.createMultiplayerGame(GameMode.MESSY, user1.getId(), user2.getId());
    }
}
