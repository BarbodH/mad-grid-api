package com.github.barbodh.madgridapi.registry.service;

import com.github.barbodh.madgridapi.registry.dao.PlayerRegistryDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlayerRegistryImpl implements PlayerRegistryService {
    private final PlayerRegistryDao playerRegistryDao;

    @Override
    public void update(String id) {
        playerRegistryDao.update(id);
    }

    @Override
    public boolean exists(String id) {
        return playerRegistryDao.exists(id);
    }

    @Override
    public void delete(String id) {
        playerRegistryDao.delete(id);
    }
}
