package com.github.barbodh.madgridapi.game.dao;

public interface PlayerRegistryDao {
    void update(String id);

    boolean exists(String id);

    void delete(String id);
}
