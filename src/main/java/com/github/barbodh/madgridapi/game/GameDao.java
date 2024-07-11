package com.github.barbodh.madgridapi.game;

import com.google.firebase.database.DatabaseReference;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class GameDao {
    private final DatabaseReference databaseReference;
    @Value("${firebase.database.node.active-multiplayer-games}")
    private final String databaseNodeName;

    public MultiplayerGame createMultiplayerGame(String gameId, GameMode gameMode, String userId1, String userId2) {
        var multiplayerGame = new MultiplayerGame(
                gameId,
                gameMode,
                new Player(userId1, 0),
                new Player(userId2, 0)
        );
        databaseReference.child(databaseNodeName).setValueAsync(multiplayerGame);
        return multiplayerGame;
    }
}
