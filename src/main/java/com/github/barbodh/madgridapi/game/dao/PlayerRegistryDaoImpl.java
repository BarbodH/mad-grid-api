package com.github.barbodh.madgridapi.game.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PlayerRegistryDaoImpl implements PlayerRegistryDao {
    @Override
    public void update(String id) {

    }

    @Override
    public boolean exists(String id) {
        return false;
    }

    @Override
    public void delete(String id) {

    }
}
