package com.github.barbodh.madgridapi.registry.service;

import com.github.barbodh.madgridapi.registry.dao.PlayerRegistryDao;
import com.github.barbodh.madgridapi.transaction.FirestoreTransactionContext;
import com.github.barbodh.madgridapi.transaction.FirestoreTransactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlayerRegistryServiceImpl implements PlayerRegistryService {
    private final PlayerRegistryDao playerRegistryDao;

    @FirestoreTransactional
    @Override
    public void update(String id) {
        var transaction = FirestoreTransactionContext.get();
        playerRegistryDao.update(transaction, id);
    }

    @FirestoreTransactional
    @Override
    public boolean exists(String id) {
        var transaction = FirestoreTransactionContext.get();
        return playerRegistryDao.exists(transaction, id);
    }

    @FirestoreTransactional
    @Override
    public void delete(String id) {
        var transaction = FirestoreTransactionContext.get();
        playerRegistryDao.delete(transaction, id);
    }
}
