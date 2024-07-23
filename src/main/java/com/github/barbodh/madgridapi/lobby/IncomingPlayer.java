package com.github.barbodh.madgridapi.lobby;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IncomingPlayer {
    private String id;
    private int gameMode;
}
