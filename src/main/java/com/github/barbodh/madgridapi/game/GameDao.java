package com.github.barbodh.madgridapi.game;

import com.google.cloud.firestore.Firestore;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class GameDao {
    private final Firestore firestore;
    @Value("${firebase.collection.name}")
    private final String collectionName;

    public MultiplayerGame createMultiplayerGame(String gameId, GameMode gameMode, String userId1, String userId2) {
        var multiplayerGame = new MultiplayerGame(
                gameId,
                gameMode,
                new Player(userId1, 0),
                new Player(userId2, 0)
        );
        firestore.collection(collectionName)
                .document("activeMultiplayerGames")
                .set(multiplayerGame);
        return multiplayerGame;
    }
}
