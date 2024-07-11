package com.github.barbodh.madgridapi.lobby;

import com.github.barbodh.madgridapi.game.MultiplayerGame;
import lombok.Getter;

@Getter
public class LobbyNotification {
    private final MultiplayerGame multiplayerGame;
    private final boolean matched;
    private final String message;

    public LobbyNotification() {
        multiplayerGame = null;
        matched = false;
        message = "You have successfully joined the lobby. Waiting for an opponent...";
    }

    public LobbyNotification(MultiplayerGame multiplayerGame) {
        this.multiplayerGame = multiplayerGame;
        matched = true;
        message = "You have been matched with an opponent.";
    }
}
