package com.github.barbodh.madgridapi.game;

import com.google.cloud.firestore.Firestore;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.Collections;

@Repository
@RequiredArgsConstructor
public class GameDao {
    private final Firestore firestore;
    @Value("${firebase.collection.name}")
    private final String collectionName;

    public MultiplayerGame createMultiplayerGame(String gameId, int gameMode, String userId1, String userId2) {
        var multiplayerGame = new MultiplayerGame(
                gameId,
                gameMode,
                new Player(userId1, 0),
                new Player(userId2, 0)
        );
        firestore.collection(collectionName)
                .document("lobby")
                .update(Collections.singletonMap("activeMultiplayerGames." + multiplayerGame.getId(), multiplayerGame));
        return multiplayerGame;
    }
}
