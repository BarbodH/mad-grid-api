package com.github.barbodh.madgridapi.lobby;

import com.google.firebase.database.DatabaseReference;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LobbyDao {
    private final DatabaseReference databaseReference;
    @Value("${firebase.database.node.queued-players}")
    private final String databaseNodeName;

    public void queuePlayer(IncomingPlayer incomingPlayer) {
        databaseReference.child(databaseNodeName)
                .child(incomingPlayer.getGameMode().toString()).setValueAsync(incomingPlayer);
    }
}
