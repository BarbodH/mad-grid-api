package com.github.barbodh.madgridapi.game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MultiplayerGame {
    private final String id;
    private final GameMode gameMode;
    private final Player player1;
    private final Player player2;
}
