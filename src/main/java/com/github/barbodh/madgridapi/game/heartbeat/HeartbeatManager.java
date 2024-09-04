package com.github.barbodh.madgridapi.game.heartbeat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
@Slf4j
public class HeartbeatManager {
    private static final Map<String, Long> playerHeartbeats = new ConcurrentHashMap<>();
    private static final long PLAYER_TIMEOUT_THRESHOLD = 10000;
    private final ApplicationEventPublisher eventPublisher;

    @Scheduled(fixedRate = 5000)
    public void check() {
        long currentTime = System.currentTimeMillis();
        playerHeartbeats.forEach((playerId, lastHeartbeatTime) -> {
            if (currentTime - lastHeartbeatTime > PLAYER_TIMEOUT_THRESHOLD) {
                timeout(playerId);
            }
        });
    }

    public void update(String playerId) {
        playerHeartbeats.put(playerId, System.currentTimeMillis());
    }

    private void timeout(String playerId) {
        playerHeartbeats.remove(playerId);
        log.info("Player " + playerId + " has timed out.");
    }
}
