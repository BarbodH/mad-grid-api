package com.github.barbodh.madgridapi.registry.service;

import com.github.barbodh.madgridapi.registry.dao.PlayerRegistryDao;
import com.github.barbodh.madgridapi.util.FirestoreUtil;
import com.google.cloud.firestore.Firestore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlayerRegistryImpl implements PlayerRegistryService {
    private final Firestore firestore;
    private final PlayerRegistryDao playerRegistryDao;

    @Override
    public void update(String id) {
        FirestoreUtil.runTransaction(firestore, transaction -> {
            playerRegistryDao.update(transaction, id);
            return null;
        });
    }

    @Override
    public boolean exists(String id) {
        return FirestoreUtil.runTransaction(firestore, transaction -> playerRegistryDao.exists(transaction, id));
    }

    @Override
    public void delete(String id) {
        FirestoreUtil.runTransaction(firestore, transaction -> {
            playerRegistryDao.delete(transaction, id);
            return null;
        });
    }
}
