package com.github.barbodh.madgridapi.lobby.dao;


import com.github.barbodh.madgridapi.lobby.model.IncomingPlayer;
import com.github.barbodh.madgridapi.util.FirestoreUtil;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class LobbyDaoImpl implements LobbyDao {
    private final String collectionName = "lobby";
    private final Firestore firestore;

    @Override
    public void save(Transaction transaction, IncomingPlayer incomingPlayer) {
        transaction.set(firestore.collection(collectionName).document(incomingPlayer.getId()), incomingPlayer);
    }

    @Override
    public Optional<IncomingPlayer> findOpponent(Transaction transaction, IncomingPlayer incomingPlayer) {
        var collection = firestore.collection(collectionName);
        var future = transaction.get(collection);
        var querySnapshot = FirestoreUtil.awaitCompletion(future);

        for (var document : querySnapshot.getDocuments()) {
            var queuedPlayer = document.toObject(IncomingPlayer.class);
            System.out.println();

            if (!incomingPlayer.getId().equals(queuedPlayer.getId()) && incomingPlayer.getGameMode() == queuedPlayer.getGameMode()) {
                return Optional.of(queuedPlayer);
            }
        }

        return Optional.empty();
    }

    @Override
    public void deleteById(Transaction transaction, String id) {
        transaction.delete(firestore.collection(collectionName).document(id));
    }
}
