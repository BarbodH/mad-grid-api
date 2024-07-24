package com.github.barbodh.madgridapi.lobby;

import com.github.barbodh.madgridapi.game.GameService;
import com.github.barbodh.madgridapi.game.MultiplayerGame;
import com.github.barbodh.madgridapi.util.ArgumentValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class LobbyService {
    private final GameService gameService;
    private final LobbyDao lobbyDao;

    public Optional<MultiplayerGame> matchPlayer(IncomingPlayer incomingPlayer) throws ExecutionException, InterruptedException {
        ArgumentValidator.validateIncomingPlayer(incomingPlayer);

        return lobbyDao.getUnmatchedPlayer(incomingPlayer)
                .map(opponent -> {
                    lobbyDao.removeUnmatchedPlayer(opponent);
                    return gameService.createMultiplayerGame(incomingPlayer.getGameMode(), incomingPlayer.getId(), opponent.getId());
                })
                .or(() -> {
                    lobbyDao.queuePlayer(incomingPlayer);
                    return Optional.empty();
                });
    }
}
