package com.github.barbodh.madgridapi.lobby;

import com.github.barbodh.madgridapi.game.MultiplayerGame;
import lombok.Getter;

@Getter
public class LobbyNotification {
    private final MultiplayerGame multiplayerGame;
    private final boolean matched;

    public LobbyNotification() {
        multiplayerGame = null;
        matched = false;
    }

    public LobbyNotification(MultiplayerGame multiplayerGame) {
        this.multiplayerGame = multiplayerGame;
        matched = true;
    }

    @Override
    public String toString() {
        return "LobbyNotification{" +
                "multiplayerGame=" + multiplayerGame +
                ", matched=" + matched +
                '}';
    }
}
