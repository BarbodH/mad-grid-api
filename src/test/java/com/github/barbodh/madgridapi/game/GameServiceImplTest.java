package com.github.barbodh.madgridapi.game;

import com.github.barbodh.madgridapi.exception.ScoreUpdateNotAllowedException;
import com.github.barbodh.madgridapi.game.dao.GameDao;
import com.github.barbodh.madgridapi.game.model.GameUpdate;
import com.github.barbodh.madgridapi.game.model.MultiplayerGame;
import com.github.barbodh.madgridapi.game.model.Player;
import com.github.barbodh.madgridapi.game.service.GameServiceImpl;
import com.github.barbodh.madgridapi.registry.service.PlayerRegistryService;
import com.github.barbodh.madgridapi.util.ArgumentValidator;
import com.github.barbodh.madgridapi.util.StringUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GameServiceImplTest {
    @Mock
    private GameDao gameDao;
    @Mock
    private PlayerRegistryService playerRegistryService;
    @InjectMocks
    private GameServiceImpl gameServiceImpl;

    @Test
    public void testCreateMultiplayerGame_exceptionHandling() {
        try (var mockedArgumentValidator = mockStatic(ArgumentValidator.class)) {
            var gameMode = 2;
            var playerId1 = "31028";
            var playerId2 = "60682";
            gameServiceImpl.create(gameMode, playerId1, playerId2);

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

        var multiplayerGame = gameServiceImpl.create(gameMode, playerId1, playerId2);

        verify(playerRegistryService).update(multiplayerGame.getPlayer1().getId());
        verify(playerRegistryService).update(multiplayerGame.getPlayer2().getId());
        verify(playerRegistryService, times(0)).delete(anyString());
        assertEquals(StringUtil.generateGameId(playerId1, playerId2), multiplayerGame.getId());
        assertEquals(gameMode, multiplayerGame.getGameMode());
        assertEquals(playerId1, multiplayerGame.getPlayer1().getId());
        assertEquals(playerId2, multiplayerGame.getPlayer2().getId());
        assertEquals(0, multiplayerGame.getPlayer1().getScore());
        assertEquals(0, multiplayerGame.getPlayer2().getScore());
        assertTrue(multiplayerGame.isActive());
        assertTrue(multiplayerGame.getPlayer1().isPlaying());
        assertTrue(multiplayerGame.getPlayer2().isPlaying());
    }

    private static Stream<Arguments> provideArgs_testUpdateGame_resultTrue() {
        return Stream.of(
                Arguments.of(5, 8, false),
                Arguments.of(8, 5, false),
                Arguments.of(8, 4, true)
        );
    }

    @ParameterizedTest
    @MethodSource("provideArgs_testUpdateGame_resultTrue")
    public void testUpdateGame_resultTrue(int score1, int score2, boolean finishGame) {
        var player1 = new Player("123", score1, true);
        var player2 = new Player("987", score2, true);
        var game = new MultiplayerGame(StringUtil.generateGameId(player1.getId(), player2.getId()), 0, player1, player2, true);
        var gameUpdate = new GameUpdate(game.getId(), player1.getId(), true);
        when(gameDao.findById(game.getId())).thenReturn(Optional.of(new MultiplayerGame(game.getId(), game.getGameMode(), player1, player2, true)));

        var updatedGame = gameServiceImpl.update(gameUpdate);
        player1.incrementScore();

        if (finishGame) {
            game.finish();

            verify(playerRegistryService, times(0)).update(anyString());
            verify(playerRegistryService).delete(game.getPlayer1().getId());
            verify(playerRegistryService).delete(game.getPlayer2().getId());
            verify(gameDao, times(0)).save(any(MultiplayerGame.class));
            verify(gameDao).deleteById(game.getId());
            assertEquals(game, updatedGame);
        } else {
            verify(playerRegistryService, times(0)).update(anyString());
            verify(playerRegistryService, times(0)).delete(anyString());
            verify(gameDao).save(updatedGame);
            verify(gameDao, times(0)).deleteById(anyString());
            assertEquals(game, updatedGame);
        }
    }

    private static Stream<Arguments> provideArgs_testUpdateGame_resultFalse() {
        return Stream.of(
                Arguments.of(8, 9, true),
                Arguments.of(8, 8, false),
                Arguments.of(8, 7, false)
        );
    }

    @ParameterizedTest
    @MethodSource("provideArgs_testUpdateGame_resultFalse")
    public void testUpdateGame_resultFalse(int score1, int score2, boolean finishGame) {
        var player1 = new Player("123", score1, true);
        var player2 = new Player("987", score2, true);
        var game = new MultiplayerGame(StringUtil.generateGameId(player1.getId(), player2.getId()), 0, player1, player2, true);
        var gameUpdate = new GameUpdate(game.getId(), player1.getId(), false);
        when(gameDao.findById(game.getId())).thenReturn(Optional.of(new MultiplayerGame(game.getId(), game.getGameMode(), player1, player2, true)));

        var updatedGame = gameServiceImpl.update(gameUpdate);

        if (finishGame) {
            game.finish();

            verify(playerRegistryService, times(0)).update(anyString());
            verify(playerRegistryService).delete(game.getPlayer1().getId());
            verify(playerRegistryService).delete(game.getPlayer2().getId());
            verify(gameDao, times(0)).save(any(MultiplayerGame.class));
            verify(gameDao, times(0)).save(any(MultiplayerGame.class));
            verify(gameDao).deleteById(game.getId());
            assertEquals(game, updatedGame);
        } else {
            player1.setPlaying(false);

            verify(playerRegistryService, times(0)).update(anyString());
            verify(playerRegistryService, times(0)).delete(anyString());
            verify(gameDao).save(updatedGame);
            verify(gameDao, times(0)).deleteById(anyString());
            assertEquals(game, updatedGame);
        }
    }

    @Test
    public void testUpdateGame_updateFinishedPlayer() {
        var finishedPlayer = new Player("123", 9, false);
        var player = new Player("987", 8, true);
        var game = new MultiplayerGame(StringUtil.generateGameId(finishedPlayer.getId(), player.getId()), 0, finishedPlayer, player, true);
        var gameUpdate = new GameUpdate(game.getId(), finishedPlayer.getId(), false);
        when(gameDao.findById(game.getId())).thenReturn(Optional.of(new MultiplayerGame(game.getId(), game.getGameMode(), finishedPlayer, player, true)));

        verify(playerRegistryService, times(0)).update(anyString());
        verify(playerRegistryService, times(0)).delete(anyString());
        verify(gameDao, times(0)).save(any(MultiplayerGame.class));
        verify(gameDao, times(0)).deleteById(anyString());
        assertThrows(ScoreUpdateNotAllowedException.class, () -> gameServiceImpl.update(gameUpdate));
    }

    @Test
    public void testUpdateGame_invalidGameId() {
        var player1 = new Player("123", 9, true);
        var player2 = new Player("987", 8, true);
        var game = new MultiplayerGame(StringUtil.generateGameId(player1.getId(), player2.getId()), 0, player1, player2, true);
        var gameUpdate = new GameUpdate(game.getId(), player2.getId(), true);
        when(gameDao.findById(game.getId())).thenReturn(Optional.empty());

        verify(playerRegistryService, times(0)).update(anyString());
        verify(playerRegistryService, times(0)).delete(anyString());
        var exception = assertThrows(IllegalArgumentException.class, () -> gameServiceImpl.update(gameUpdate));
        assertTrue(exception.getMessage().contains(game.getId()));
    }

    @Test
    public void testUpdateGame_invalidPlayerId() {
        var player1 = new Player("123", 8, true);
        var player2 = new Player("987", 9, true);
        var player3 = new Player("456", 9, true);
        var game = new MultiplayerGame(StringUtil.generateGameId(player1.getId(), player2.getId()), 0, player1, player2, true);
        var gameUpdate = new GameUpdate(game.getId(), player3.getId(), true);
        when(gameDao.findById(game.getId())).thenReturn(Optional.of(new MultiplayerGame(game.getId(), game.getGameMode(), player1, player2, true)));

        verify(playerRegistryService, times(0)).update(anyString());
        verify(playerRegistryService, times(0)).delete(anyString());
        var exception = assertThrows(IllegalArgumentException.class, () -> gameServiceImpl.update(gameUpdate));
        assertTrue(exception.getMessage().contains(gameUpdate.getPlayerId()));
    }
}
