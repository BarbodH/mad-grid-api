package com.github.barbodh.madgridapi.lobby.service;

import com.github.barbodh.madgridapi.game.service.GameService;
import com.github.barbodh.madgridapi.game.model.MultiplayerGame;
import com.github.barbodh.madgridapi.lobby.dao.LobbyDao;
import com.github.barbodh.madgridapi.lobby.model.IncomingPlayer;
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
                    return gameService.create(incomingPlayer.getGameMode(), incomingPlayer.getId(), opponent.getId());
                })
                .or(() -> {
                    lobbyDao.save(incomingPlayer);
                    return Optional.empty();
                });
    }
}
