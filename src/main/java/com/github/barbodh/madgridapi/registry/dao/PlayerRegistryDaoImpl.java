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
        var future = documentReference.get();
        var documentSnapshot = FirestoreUtil.awaitCompletion(future);

        if (documentSnapshot.exists()) {
            documentReference.update("ids", FieldValue.arrayUnion(id));
        } else {
            var data = new HashMap<String, Object>();
            data.put("ids", Collections.singletonList(id));
            documentReference.set(data);
        }
    }

    @Override
    public boolean exists(Transaction transaction, String id) {
        var future = firestore.collection(collectionName).document(documentName).get();
        var documentSnapshot = FirestoreUtil.awaitCompletion(future);

        if (documentSnapshot.exists()) {
            var registry = documentSnapshot.toObject(PlayerRegistry.class);
            return registry != null && registry.getIds().contains(id);
        }

        return false;
    }

    @Override
    public void delete(Transaction transaction, String id) {
        var future = firestore.collection(collectionName).document(documentName).update("ids", FieldValue.arrayRemove(id));
        FirestoreUtil.awaitCompletion(future);
    }
}
