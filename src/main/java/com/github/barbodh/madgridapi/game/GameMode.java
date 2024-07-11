package com.github.barbodh.madgridapi.game;

import lombok.Getter;

@Getter
public enum GameMode {
    CLASSIC(0),
    REVERSE(1),
    MESSY(2);

    private final int value;

    GameMode(int value) {
        this.value = value;
    }

    public static GameMode fromValue(int value) {
        for (GameMode mode : GameMode.values()) {
            if (mode.value == value) {
                return mode;
            }
        }
        throw new IllegalArgumentException("Invalid game mode: " + value);
    }
}
