package com.github.barbodh.madgridapi.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class GameServiceTest {
    @Mock
    private GameDao gameDao;
    @InjectMocks
    private GameService gameService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        when(gameDao.save(any(MultiplayerGame.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    public void testCreateMultiplayerGame() {
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

    // TODO: Implement unit tests for edge cases and exception handling
}
