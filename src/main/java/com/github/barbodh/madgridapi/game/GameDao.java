package com.github.barbodh.madgridapi.game;

import com.github.barbodh.madgridapi.util.FirestoreUtil;
import com.google.cloud.firestore.Firestore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GameDao {
    private final Firestore firestore;
    private final String collectionName = "activeGames";

    public void save(MultiplayerGame multiplayerGame) {
        var future = firestore.collection(collectionName).document(multiplayerGame.getId()).set(multiplayerGame);
        FirestoreUtil.awaitCompletion(future);
    }

    public Optional<MultiplayerGame> findById(String id) {
        var future = firestore.collection(collectionName).document(id).get();
        var documentSnapshot = FirestoreUtil.awaitCompletion(future);
        return Optional.ofNullable(documentSnapshot.toObject(MultiplayerGame.class));
    }

    public void deleteById(String id) {
        var future = firestore.collection(collectionName).document(id).delete();
        FirestoreUtil.awaitCompletion(future);
    }
}
