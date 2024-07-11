package com.github.barbodh.madgridapi.lobby;

import com.github.barbodh.madgridapi.game.GameService;
import com.github.barbodh.madgridapi.game.MultiplayerGame;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class LobbyService {
    private final Set<IncomingPlayer> unmatchedPlayers;
    private final GameService gameService;
    private final LobbyDao lobbyDao;

    public Optional<MultiplayerGame> matchPlayer(IncomingPlayer incomingPlayer) {
        return unmatchedPlayers.stream()
                .filter(p -> p.getGameMode() == incomingPlayer.getGameMode())
                .findFirst()
                .map(p -> gameService.createMultiplayerGame(p.getGameMode(), p.getId(), incomingPlayer.getId()))
                .or(() -> {
                    lobbyDao.queuePlayer(incomingPlayer);
                    return Optional.empty();
                });
    }
}
