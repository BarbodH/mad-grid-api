package com.github.barbodh.madgridapi;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class IncomingPlayerClientPayload {
    private String id;
    private int gameMode;
}
