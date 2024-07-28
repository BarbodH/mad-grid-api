package com.github.barbodh.madgridapi.game.service;

import com.github.barbodh.madgridapi.exception.ScoreUpdateNotAllowedException;
import com.github.barbodh.madgridapi.game.dao.GameDao;
import com.github.barbodh.madgridapi.game.model.GameUpdate;
import com.github.barbodh.madgridapi.game.model.MultiplayerGame;
import com.github.barbodh.madgridapi.game.model.Player;
import com.github.barbodh.madgridapi.util.ArgumentValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {
    private final GameDao gameDao;

    @Override
    public MultiplayerGame create(int gameMode, String playerId1, String playerId2) {
        ArgumentValidator.validatePlayerId(playerId1);
        ArgumentValidator.validatePlayerId(playerId2);
        ArgumentValidator.validateGameMode(gameMode);

        var game = new MultiplayerGame(
                String.format("%s_%s", playerId1, playerId2),
                gameMode,
                new Player(playerId1),
                new Player(playerId2)
        );
        gameDao.save(game);

        return game;
    }

    @Override
    public MultiplayerGame update(GameUpdate gameUpdate) {
        return gameDao.findById(gameUpdate.getGameId())
                .map(game -> {
                    var player = Stream.of(game.getPlayer1(), game.getPlayer2())
                            .filter(p -> p.getId().equals(gameUpdate.getPlayerId()))
                            .findFirst()
                            .orElseThrow(() -> new IllegalArgumentException("Player not found. Provided ID: " + gameUpdate.getPlayerId()));

                    if (!player.isPlaying()) {
                        throw new ScoreUpdateNotAllowedException();
                    }

                    var score1 = game.getPlayer1().getScore();
                    var score2 = game.getPlayer2().getScore();
                    var playerScore = player.getScore();

                    if (gameUpdate.isResult()) {
                        player.incrementScore();

                        if (Math.abs(score1 - score2) == 4 && (playerScore > score1 || playerScore > score2)) {
                            game.finish();
                            gameDao.deleteById(gameUpdate.getGameId());
                            return game;
                        }
                    } else if (playerScore < score1 || playerScore < score2) {
                        game.finish();
                        gameDao.deleteById(gameUpdate.getGameId());
                        return game;
                    } else {
                        player.setPlaying(false);
                    }

                    gameDao.save(game);
                    return game;
                })
                .orElseThrow(() -> new IllegalArgumentException("Game not found. Provided ID: " + gameUpdate.getGameId()));
    }
}
