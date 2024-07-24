package com.github.barbodh.madgridapi.util;

import com.github.barbodh.madgridapi.lobby.IncomingPlayer;

public class ArgumentValidator {
    public static void validateGameMode(int gameMode) {
        if (gameMode < 0 || gameMode > 2) {
            throw new IllegalArgumentException("Game mode should be within the range of 0 to 2. Provided: " + gameMode);
        }
    }

    public static void validateUserId(String userId) {
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("User ID should not be empty. Provided: " + userId);
        }
        if (userId.length() > 10) {
            throw new IllegalArgumentException("User ID should not exceed 10 characters. Provided: " + userId);
        }
    }

    public static void validateIncomingPlayer(IncomingPlayer incomingPlayer) {
        validateGameMode(incomingPlayer.getGameMode());
        validateUserId(incomingPlayer.getId());
    }
}
