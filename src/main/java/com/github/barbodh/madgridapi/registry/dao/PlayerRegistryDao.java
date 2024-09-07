package com.github.barbodh.madgridapi.registry.dao;

import com.google.cloud.firestore.Transaction;

public interface PlayerRegistryDao {
    void update(Transaction transaction, String id);

    boolean exists(Transaction transaction, String id);

    void delete(Transaction transaction, String id);
}
