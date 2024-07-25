package com.github.barbodh.madgridapi.game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameUpdate {
    private String gameId;
    private String playerId;
    private boolean result;
}
