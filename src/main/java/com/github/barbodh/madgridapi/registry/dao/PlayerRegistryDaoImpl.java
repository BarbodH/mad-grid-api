package com.github.barbodh.madgridapi.registry.dao;

import com.github.barbodh.madgridapi.registry.model.PlayerRegistry;
import com.github.barbodh.madgridapi.util.FirestoreUtil;
import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.HashMap;

@Repository
@RequiredArgsConstructor
public class PlayerRegistryDaoImpl implements PlayerRegistryDao {
    private final String collectionName = "activePlayers";
    private final String documentName = "registry";
    private final Firestore firestore;

    @Override
    public void update(Transaction transaction, String id) {
        var documentReference = firestore.collection(collectionName).document(documentName);
        var future = transaction.get(documentReference);
        var documentSnapshot = FirestoreUtil.awaitCompletion(future);

        if (documentSnapshot.exists()) {
            transaction.update(documentReference, "ids", FieldValue.arrayUnion(id));
        } else {
            var data = new HashMap<String, Object>();
            data.put("ids", Collections.singletonList(id));
            transaction.set(documentReference, data);
        }
    }

    @Override
    public boolean exists(Transaction transaction, String id) {
        var future = transaction.get(firestore.collection(collectionName).document(documentName));
        var documentSnapshot = FirestoreUtil.awaitCompletion(future);

        if (documentSnapshot.exists()) {
            var registry = documentSnapshot.toObject(PlayerRegistry.class);
            return registry != null && registry.getIds().contains(id);
        }

        return false;
    }

    @Override
    public void delete(Transaction transaction, String id) {
        transaction.update(firestore.collection(collectionName).document(documentName), "ids", FieldValue.arrayRemove(id));
    }
}
