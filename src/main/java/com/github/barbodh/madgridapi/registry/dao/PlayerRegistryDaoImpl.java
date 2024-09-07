package com.github.barbodh.madgridapi.registry.dao;

import com.github.barbodh.madgridapi.registry.model.ActivePlayer;
import com.github.barbodh.madgridapi.util.FirestoreUtil;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PlayerRegistryDaoImpl implements PlayerRegistryDao {
    private final String collectionName = "activePlayers";

    private final Firestore firestore;

    @Override
    public void update(Transaction transaction, String id) {
        transaction.set(firestore.collection(collectionName).document(id), new ActivePlayer(id));
    }

    @Override
    public boolean exists(Transaction transaction, String id) {
        var future = transaction.get(firestore.collection(collectionName));
        var querySnapshot = FirestoreUtil.awaitCompletion(future);

        for (var document : querySnapshot.getDocuments()) {
            var activePlayer = document.toObject(ActivePlayer.class);
            if (activePlayer.getId().equals(id)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void delete(Transaction transaction, String id) {
        transaction.delete(firestore.collection(collectionName).document(id));
    }
}
