package com.github.barbodh.madgridapi.game;

import com.github.barbodh.madgridapi.util.ArgumentValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameService {
    private final GameDao gameDao;

    public MultiplayerGame createMultiplayerGame(int gameMode, String playerId1, String playerId2) {
        ArgumentValidator.validatePlayerId(playerId1);
        ArgumentValidator.validatePlayerId(playerId2);
        ArgumentValidator.validateGameMode(gameMode);

        return gameDao.save(new MultiplayerGame(
                String.format("%s_%s", playerId1, playerId2),
                gameMode,
                new Player(playerId1, 0),
                new Player(playerId2, 0)
        ));
    }
}
