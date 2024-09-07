package com.github.barbodh.madgridapi.lobby.dao;

import com.github.barbodh.madgridapi.lobby.model.IncomingPlayer;
import com.google.cloud.firestore.Transaction;

import java.util.Optional;

public interface LobbyDao {
    void save(Transaction transaction, IncomingPlayer incomingPlayer);

    Optional<IncomingPlayer> findOpponent(IncomingPlayer incomingPlayer);

    void deleteById(String id);
}
