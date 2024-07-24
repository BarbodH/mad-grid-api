package com.github.barbodh.madgridapi.game;

import com.github.barbodh.madgridapi.util.ArgumentValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
            var userId1 = "31028";
            var userId2 = "60682";
            gameService.createMultiplayerGame(gameMode, userId1, userId2);

            mockedArgumentValidator.verify(() -> ArgumentValidator.validateGameMode(gameMode));
            mockedArgumentValidator.verify(() -> ArgumentValidator.validatePlayerId(userId1));
            mockedArgumentValidator.verify(() -> ArgumentValidator.validatePlayerId(userId2));
        }
    }

    @Test
    public void testCreateMultiplayerGame() {
        var gameMode = 2;
        var userId1 = "31028";
        var userId2 = "60682";
        when(gameDao.save(any(MultiplayerGame.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var multiplayerGame = gameService.createMultiplayerGame(gameMode, userId1, userId2);

        assertEquals(String.format("%s_%s", userId1, userId2), multiplayerGame.getId());
        assertEquals(gameMode, multiplayerGame.getGameMode());
        assertEquals(userId1, multiplayerGame.getPlayer1().getId());
        assertEquals(userId2, multiplayerGame.getPlayer2().getId());
        assertEquals(0, multiplayerGame.getPlayer1().getScore());
        assertEquals(0, multiplayerGame.getPlayer2().getScore());
    }
}
