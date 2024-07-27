package com.github.barbodh.madgridapi.lobby;

import com.github.barbodh.madgridapi.game.GameService;
import com.github.barbodh.madgridapi.game.MultiplayerGame;
import com.github.barbodh.madgridapi.util.ArgumentValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LobbyService {
    private final GameService gameService;
    private final LobbyDao lobbyDao;

    public Optional<MultiplayerGame> matchPlayer(IncomingPlayer incomingPlayer) {
        ArgumentValidator.validatePlayerId(incomingPlayer.getId());
        ArgumentValidator.validateGameMode(incomingPlayer.getGameMode());

        return lobbyDao.findOpponent(incomingPlayer)
                .map(opponent -> {
                    lobbyDao.removeById(opponent.getId());
                    return gameService.createMultiplayerGame(incomingPlayer.getGameMode(), incomingPlayer.getId(), opponent.getId());
                })
                .or(() -> {
                    lobbyDao.save(incomingPlayer);
                    return Optional.empty();
                });
    }
}
