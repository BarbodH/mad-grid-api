package com.github.barbodh.madgridapi.lobby.dao;

import com.github.barbodh.madgridapi.lobby.model.IncomingPlayer;
import com.google.cloud.firestore.Transaction;

import java.util.Optional;

public interface LobbyDao {
    void save(Transaction transaction, IncomingPlayer incomingPlayer);

Optional<IncomingPlayer> findOpponent(Transaction transaction, IncomingPlayer incomingPlayer);

    void deleteById(Transaction transaction, String id);
}
