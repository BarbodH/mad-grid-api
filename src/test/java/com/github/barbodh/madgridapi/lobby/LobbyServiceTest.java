package com.github.barbodh.madgridapi.lobby;

import com.github.barbodh.madgridapi.game.GameService;
import com.github.barbodh.madgridapi.game.MultiplayerGame;
import com.github.barbodh.madgridapi.util.ArgumentValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LobbyServiceTest {
    @Mock
    private LobbyDao lobbyDao;
    @Mock
    private GameService gameService;
    @InjectMocks
    private LobbyService lobbyService;

    @Test
    public void testMatchPlayer_exceptionHandling() throws ExecutionException, InterruptedException {
        try (var mockedArgumentValidator = mockStatic(ArgumentValidator.class)) {
            var incomingPlayer = new IncomingPlayer("123", 0);
            lobbyService.matchPlayer(incomingPlayer);

            mockedArgumentValidator.verify(() -> ArgumentValidator.validatePlayerId(incomingPlayer.getId()));
            mockedArgumentValidator.verify(() -> ArgumentValidator.validateGameMode(incomingPlayer.getGameMode()));
        }
    }

    @Test
    public void testMatchPlayer_opponentFound() throws ExecutionException, InterruptedException {
        var incomingPlayer = new IncomingPlayer("123", 0);
        var opponent = new IncomingPlayer("987", 0);
        var expectedMultiplayerGameInstance = new MultiplayerGame();
        when(gameService.createMultiplayerGame(incomingPlayer.getGameMode(), incomingPlayer.getId(), opponent.getId()))
                .thenReturn(expectedMultiplayerGameInstance);
        when(lobbyDao.getUnmatchedPlayer(incomingPlayer)).thenReturn(Optional.of(opponent));

        var multiplayerGame = lobbyService.matchPlayer(incomingPlayer);

        verify(gameService).createMultiplayerGame(incomingPlayer.getGameMode(), incomingPlayer.getId(), opponent.getId());
        verify(lobbyDao).removeUnmatchedPlayer(opponent);
        verify(lobbyDao, times(0)).queuePlayer(any(IncomingPlayer.class));
        assertTrue(multiplayerGame.isPresent());
        assertEquals(expectedMultiplayerGameInstance, multiplayerGame.get());
    }

    @Test
    public void testMatchPlayer_opponentNotFound() throws ExecutionException, InterruptedException {
        var incomingPlayer = new IncomingPlayer("123", 0);
        when(lobbyDao.getUnmatchedPlayer(incomingPlayer)).thenReturn(Optional.empty());

        var multiplayerGame = lobbyService.matchPlayer(incomingPlayer);

        verify(gameService, times(0)).createMultiplayerGame(anyInt(), anyString(), anyString());
        verify(lobbyDao, times(0)).removeUnmatchedPlayer(any(IncomingPlayer.class));
        verify(lobbyDao).queuePlayer(incomingPlayer);
        assertTrue(multiplayerGame.isEmpty());
    }
}
