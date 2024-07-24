package com.github.barbodh.madgridapi.lobby;

import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.Firestore;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Repository
@RequiredArgsConstructor
public class LobbyDao {
    private final Firestore firestore;
    @Value("${firebase.collection.name}")
    private final String collectionName;

    public Optional<IncomingPlayer> getUnmatchedPlayer(IncomingPlayer incomingPlayer) throws ExecutionException, InterruptedException {
        var future = firestore.collection(collectionName).document("lobby").get();
        var document = future.get();
        var unmatchedPlayers = (ArrayList<?>) document.get("unmatchedPlayers");

        if (unmatchedPlayers != null && !unmatchedPlayers.isEmpty()) {
            for (Object unmatchedPlayerObject : unmatchedPlayers) {
                var unmatchedPlayerMap = (HashMap<?, ?>) unmatchedPlayerObject;
                var id = (String) unmatchedPlayerMap.get("id");
                var gameMode = ((Long) unmatchedPlayerMap.get("gameMode")).intValue();

                if (!incomingPlayer.getId().equals(id) && incomingPlayer.getGameMode() == gameMode) {
                    return Optional.of(new IncomingPlayer(id, gameMode));
                }
            }
        }

        return Optional.empty();
    }

    public void removeUnmatchedPlayer(IncomingPlayer unmatchedPlayer) {
        firestore.collection(collectionName)
                .document("lobby")
                .update("unmatchedPlayers", FieldValue.arrayRemove(unmatchedPlayer));
    }

    public void queuePlayer(IncomingPlayer incomingPlayer) {
        firestore.collection(collectionName)
                .document("lobby")
                .update("unmatchedPlayers", FieldValue.arrayUnion(incomingPlayer));
    }
}
