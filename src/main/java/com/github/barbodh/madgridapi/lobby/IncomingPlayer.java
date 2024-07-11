package com.github.barbodh.madgridapi.lobby;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.barbodh.madgridapi.game.GameMode;
import com.github.barbodh.madgridapi.game.GameModeDeserializer;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IncomingPlayer {
    private String id;
    @JsonDeserialize(using = GameModeDeserializer.class)
    private GameMode gameMode;
}
