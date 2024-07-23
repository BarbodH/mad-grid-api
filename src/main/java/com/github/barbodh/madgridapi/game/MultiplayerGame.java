package com.github.barbodh.madgridapi.game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MultiplayerGame {
    private String id;
    private int gameMode;
    private Player player1;
    private Player player2;
}
