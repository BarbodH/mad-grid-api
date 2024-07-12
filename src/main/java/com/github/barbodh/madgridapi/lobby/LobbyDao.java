package com.github.barbodh.madgridapi.lobby;

import com.github.barbodh.madgridapi.game.GameMode;
import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.Firestore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
@RequiredArgsConstructor
public class LobbyDao {
    private final Firestore firestoreDb;

    // TODO: Implement this method
    public ArrayList<IncomingPlayer> getUnmatchedPlayers(GameMode gameMode) {
        return new ArrayList<>();
    }

    public void queuePlayer(IncomingPlayer incomingPlayer) {
        firestoreDb.collection("madGridData")
                .document("lobby")
                .update("CLASSIC", FieldValue.arrayUnion(incomingPlayer));
    }
}
