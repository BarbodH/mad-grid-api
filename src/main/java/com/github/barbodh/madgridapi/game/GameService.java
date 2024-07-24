package com.github.barbodh.madgridapi.game;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameService {
    private final GameDao gameDao;

    public MultiplayerGame createMultiplayerGame(int gameMode, String userId1, String userId2) {
        return gameDao.save(new MultiplayerGame(
                String.format("%s_%s", userId1, userId2),
                gameMode,
                new Player(userId1, 0),
                new Player(userId2, 0)
        ));
    }
}
