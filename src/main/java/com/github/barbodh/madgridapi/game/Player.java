package com.github.barbodh.madgridapi.game;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Player {
    private String id;
    private int score;
    private boolean playing;

    public Player(String id) {
        this.id = id;
    }

    public void incrementScore() {
        score++;
    }
}
