package com.github.barbodh.madgridapi.lobby;

import com.github.barbodh.madgridapi.game.GameService;
import com.github.barbodh.madgridapi.game.MultiplayerGame;
import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.Firestore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class LobbyService {
    private final Firestore firestore;
    private final GameService gameService;
    private final LobbyDao lobbyDao;

    public Optional<MultiplayerGame> matchPlayer(IncomingPlayer incomingPlayer) {
        MultiplayerGame game = null;
        var future = firestore.collection("madGridData").document("lobby").get();

        try {
            var document = future.get();
            var unmatchedPlayers = (ArrayList<?>) document.get("unmatchedPlayers");

            if (unmatchedPlayers != null && !unmatchedPlayers.isEmpty()) {
                for (Object unmatchedPlayerObject : unmatchedPlayers) {
                    var unmatchedPlayerMap = (HashMap<?, ?>) unmatchedPlayerObject;
                    var id = (String) unmatchedPlayerMap.get("id");
                    var gameMode = ((Long) unmatchedPlayerMap.get("gameMode")).intValue();

                    if (!incomingPlayer.getId().equals(id) && incomingPlayer.getGameMode() == gameMode) {
                        var opponent = new IncomingPlayer(id, gameMode);
                        game = gameService.createMultiplayerGame(incomingPlayer.getGameMode(), incomingPlayer.getId(), opponent.getId());
                        firestore.collection("madGridData").document("lobby").update("unmatchedPlayers", FieldValue.arrayRemove(unmatchedPlayerObject));
                    }
                }
            }

            if (game == null) {
                lobbyDao.queuePlayer(incomingPlayer);
            }
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return Optional.ofNullable(game);
    }
}
