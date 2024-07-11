package com.github.barbodh.madgridapi.game;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GameService {
    private final GameDao gameDao;

    public MultiplayerGame createMultiplayerGame(GameMode gameMode, String userId1, String userId2) {
        return gameDao.createMultiplayerGame(String.format("%s_%s", userId1, userId2), gameMode, userId1, userId2);
    }
}
