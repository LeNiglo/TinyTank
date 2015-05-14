package com.lefrantguillaume.gameComponent.gameMode;

import java.util.UUID;

/**
 * Created by andres_k on 13/05/2015.
 */
public class Team {
    private UUID id;
    private int currentScore;

    public Team(UUID id){
        this.id = id;
        this.init();
    }

    // FUNCTION

    public void addToCurrentScore(int score) {
        this.currentScore += score;
    }

    public void init(){
        this.currentScore = 0;
    }

    // GETTERS
    public UUID getId(){
        return this.id;
    }

    public int getCurrentScore() {
        return this.currentScore;
    }
}
