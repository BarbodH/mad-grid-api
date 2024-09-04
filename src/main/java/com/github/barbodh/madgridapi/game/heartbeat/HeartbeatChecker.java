package com.github.barbodh.madgridapi.game.heartbeat;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class HeartbeatChecker {
    @Scheduled(fixedRate = 5000)
    public void checkPeriodically() {
        HeartbeatManager.check();
    }
}
