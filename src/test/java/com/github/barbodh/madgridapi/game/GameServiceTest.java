package com.github.barbodh.madgridapi.game;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GameServiceTest {
    @Mock
    private GameDao gameDao;
    @InjectMocks
    private GameService gameService;

    @Test
    public void testCreateMultiplayerGame() {
        when(gameDao.save(any(MultiplayerGame.class))).thenAnswer(invocation -> invocation.getArgument(0));
        var gameMode = 2;
        var userId1 = "31028";
        var userId2 = "60682";
        var multiplayerGame = gameService.createMultiplayerGame(gameMode, userId1, userId2);

        assertEquals(String.format("%s_%s", userId1, userId2), multiplayerGame.getId());
        assertEquals(gameMode, multiplayerGame.getGameMode());
        assertEquals(userId1, multiplayerGame.getPlayer1().getId());
        assertEquals(userId2, multiplayerGame.getPlayer2().getId());
        assertEquals(0, multiplayerGame.getPlayer1().getScore());
        assertEquals(0, multiplayerGame.getPlayer2().getScore());
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 3})
    public void testCreateMultiplayerGame_invalidGameModes(int invalidGameMode) {
        assertThrows(IllegalArgumentException.class, () -> gameService.createMultiplayerGame(invalidGameMode, "123", "987"));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "this_is_a_very_long_string"})
    public void testCreateMultiplayerGame_invalidUserIds(String invalidUserId) {
        assertThrows(IllegalArgumentException.class, () -> gameService.createMultiplayerGame(0, invalidUserId, "123"));
        assertThrows(IllegalArgumentException.class, () -> gameService.createMultiplayerGame(0, "123", invalidUserId));
    }
}
