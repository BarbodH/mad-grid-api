package com.github.barbodh.madgridapi.registry.dao;

import com.github.barbodh.madgridapi.registry.model.PlayerRegistry;
import com.github.barbodh.madgridapi.util.FirestoreUtil;
import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.Firestore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PlayerRegistryDaoImpl implements PlayerRegistryDao {
    private final String collectionName = "activePlayers";
    private final String documentName = "registry";
    private final Firestore firestore;

    @Override
    public void update(String id) {
        var future = firestore.collection(collectionName).document(documentName).update("ids", FieldValue.arrayUnion(id));
        FirestoreUtil.awaitCompletion(future);
    }

    @Override
    public boolean exists(String id) {
        var future = firestore.collection(collectionName).document(documentName).get();
        var documentSnapshot = FirestoreUtil.awaitCompletion(future);

        if (documentSnapshot.exists()) {
            var registry = documentSnapshot.toObject(PlayerRegistry.class);
            return registry != null && registry.getIds().contains(id);
        }

        return false;
    }

    @Override
    public void delete(String id) {
        var future = firestore.collection(collectionName).document(documentName).update("ids", FieldValue.arrayRemove(id));
        FirestoreUtil.awaitCompletion(future);
    }
}
