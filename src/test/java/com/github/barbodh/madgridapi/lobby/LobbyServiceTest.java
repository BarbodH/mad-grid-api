package com.github.barbodh.madgridapi.lobby;

import com.github.barbodh.madgridapi.game.service.GameService;
import com.github.barbodh.madgridapi.game.model.MultiplayerGame;
import com.github.barbodh.madgridapi.lobby.dao.LobbyDao;
import com.github.barbodh.madgridapi.lobby.model.IncomingPlayer;
import com.github.barbodh.madgridapi.lobby.service.LobbyService;
import com.github.barbodh.madgridapi.util.ArgumentValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

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
    public void testMatchPlayer_exceptionHandling() {
        try (var mockedArgumentValidator = mockStatic(ArgumentValidator.class)) {
            var incomingPlayer = new IncomingPlayer("123", 0);
            lobbyService.matchPlayer(incomingPlayer);

            mockedArgumentValidator.verify(() -> ArgumentValidator.validatePlayerId(incomingPlayer.getId()));
            mockedArgumentValidator.verify(() -> ArgumentValidator.validateGameMode(incomingPlayer.getGameMode()));
        }
    }

    @Test
    public void testMatchPlayer_opponentFound() {
        var incomingPlayer = new IncomingPlayer("123", 0);
        var opponent = new IncomingPlayer("987", 0);
        var expectedMultiplayerGameInstance = new MultiplayerGame();
        when(gameService.create(incomingPlayer.getGameMode(), incomingPlayer.getId(), opponent.getId()))
                .thenReturn(expectedMultiplayerGameInstance);
        when(lobbyDao.findOpponent(incomingPlayer)).thenReturn(Optional.of(opponent));

        var multiplayerGame = lobbyService.matchPlayer(incomingPlayer);

        verify(gameService).create(incomingPlayer.getGameMode(), incomingPlayer.getId(), opponent.getId());
        verify(lobbyDao).removeById(opponent.getId());
        verify(lobbyDao, times(0)).save(any(IncomingPlayer.class));
        assertTrue(multiplayerGame.isPresent());
        assertEquals(expectedMultiplayerGameInstance, multiplayerGame.get());
    }

    @Test
    public void testMatchPlayer_opponentNotFound() {
        var incomingPlayer = new IncomingPlayer("123", 0);
        when(lobbyDao.findOpponent(incomingPlayer)).thenReturn(Optional.empty());

        var multiplayerGame = lobbyService.matchPlayer(incomingPlayer);

        verify(gameService, times(0)).create(anyInt(), anyString(), anyString());
        verify(lobbyDao, times(0)).removeById(any(String.class));
        verify(lobbyDao).save(incomingPlayer);
        assertTrue(multiplayerGame.isEmpty());
    }
}
