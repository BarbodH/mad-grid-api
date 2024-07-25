package com.github.barbodh.madgridapi.game;

import com.github.barbodh.madgridapi.util.ArgumentValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GameServiceTest {
    @Mock
    private GameDao gameDao;
    @InjectMocks
    private GameService gameService;

    @Test
    public void testCreateMultiplayerGame_exceptionHandling() {
        try (var mockedArgumentValidator = mockStatic(ArgumentValidator.class)) {
            var gameMode = 2;
            var playerId1 = "31028";
            var playerId2 = "60682";
            gameService.createMultiplayerGame(gameMode, playerId1, playerId2);

            mockedArgumentValidator.verify(() -> ArgumentValidator.validateGameMode(gameMode));
            mockedArgumentValidator.verify(() -> ArgumentValidator.validatePlayerId(playerId1));
            mockedArgumentValidator.verify(() -> ArgumentValidator.validatePlayerId(playerId2));
        }
    }

    @Test
    public void testCreateMultiplayerGame() {
        var gameMode = 2;
        var playerId1 = "31028";
        var playerId2 = "60682";
        when(gameDao.save(any(MultiplayerGame.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var multiplayerGame = gameService.createMultiplayerGame(gameMode, playerId1, playerId2);

        assertEquals(String.format("%s_%s", playerId1, playerId2), multiplayerGame.getId());
        assertEquals(gameMode, multiplayerGame.getGameMode());
        assertEquals(playerId1, multiplayerGame.getPlayer1().getId());
        assertEquals(playerId2, multiplayerGame.getPlayer2().getId());
        assertEquals(0, multiplayerGame.getPlayer1().getScore());
        assertEquals(0, multiplayerGame.getPlayer2().getScore());
    }

    @Test
    public void testUpdateGame_resultTrue() {
        var player1 = new Player("123", 8, true);
        var player2 = new Player("987", 9, true);
        var game = new MultiplayerGame(String.format("%s_%s", player1.getId(), player2.getId()), 0, player1, player2, true);
        var gameUpdate = new GameUpdate(game.getId(), player1.getId(), true);
        when(gameDao.findById(player1.getId())).thenReturn(new MultiplayerGame(game.getId(), game.getGameMode(), player1, player2));

        var updatedGame = gameService.updateGame(gameUpdate);
        player1.incrementScore();

        assertEquals(game, updatedGame);
    }

    @Test
    public void testUpdateGame_resultTrue_exceededMaxScoreDifference() {
        var player1 = new Player("123", 4, true);
        var player2 = new Player("987", 8, true);
        var game = new MultiplayerGame(String.format("%s_%s", player1.getId(), player2.getId()), 0, player1, player2, true);
        var gameUpdate = new GameUpdate(game.getId(), player2.getId(), true);
        when(gameDao.findById(player2.getId())).thenReturn(new MultiplayerGame(game.getId(), game.getGameMode(), player1, player2));

        var updatedGame = gameService.updateGame(gameUpdate);
        player2.incrementScore();
        game.finish();

        assertEquals(game, updatedGame);
    }

    @ParameterizedTest
    @ValueSource(ints = {9, 10})
    public void testUpdateGame_resultFalse_leadingOrTie(int score) {
        var player1 = new Player("123", score, true);
        var player2 = new Player("987", 9, true);
        var game = new MultiplayerGame(String.format("%s_%s", player1.getId(), player2.getId()), 0, player1, player2, true);
        var gameUpdate = new GameUpdate(game.getId(), player1.getId(), false);
        when(gameDao.findById(player1.getId())).thenReturn(new MultiplayerGame(game.getId(), game.getGameMode(), player1, player2));

        var updatedGame = gameService.updateGame(gameUpdate);
        player1.setPlaying(false);

        assertEquals(game, updatedGame);
    }

    @Test
    public void testUpdateGame_resultFalse_trailing() {
        var player1 = new Player("123", 9, true);
        var player2 = new Player("987", 8, true);
        var game = new MultiplayerGame(String.format("%s_%s", player1.getId(), player2.getId()), 0, player1, player2, true);
        var gameUpdate = new GameUpdate(game.getId(), player2.getId(), false);
        when(gameDao.findById(player1.getId())).thenReturn(new MultiplayerGame(game.getId(), game.getGameMode(), player1, player2));

        var updatedGame = gameService.updateGame(gameUpdate);
        game.finish();

        assertEquals(game, updatedGame);
   }
}
