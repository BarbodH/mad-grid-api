package com.github.barbodh.madgridapi.game;

import com.google.cloud.firestore.Firestore;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GameDao {
    private final Firestore firestore;
    @Value("${firebase.collection.name}")
    private final String collectionName;

    public MultiplayerGame save(MultiplayerGame multiplayerGame) {
        firestore.collection(collectionName)
                .document("lobby")
                .update(Collections.singletonMap("activeMultiplayerGames." + multiplayerGame.getId(), multiplayerGame));
        return multiplayerGame;
    }

    public Optional<MultiplayerGame> findById(String id) {
        return Optional.empty();
    }

    public void deleteById(String id) {

    }
}
