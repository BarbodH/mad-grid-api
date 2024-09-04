package com.github.barbodh.madgridapi.game.heartbeat;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class HeartbeatManager {
    private static final Map<String, Long> playerHeartbeats = new ConcurrentHashMap<>();
    private static final long PLAYER_TIMEOUT_THRESHOLD = 10000;

    public static void update(String playerId) {
        playerHeartbeats.put(playerId, System.currentTimeMillis());
    }

    public static void check() {
        long currentTime = System.currentTimeMillis();
        playerHeartbeats.forEach((playerId, lastHeartbeatTime) -> {
            if (currentTime - lastHeartbeatTime > PLAYER_TIMEOUT_THRESHOLD) {
                timeout(playerId);
            }
        });
    }

    private static void timeout(String playerId) {
        playerHeartbeats.remove(playerId);
        log.info("Player " + playerId + " has timed out.");
    }
}
