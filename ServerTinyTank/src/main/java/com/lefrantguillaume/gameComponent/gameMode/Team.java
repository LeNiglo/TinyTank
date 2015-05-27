package com.lefrantguillaume.gameComponent.gameMode;

import java.util.UUID;

/**
 * Created by andres_k on 13/05/2015.
 */
public class Team {
    private UUID id;
    private int currentScore;
    private int currentPlayers;

    public Team(UUID id) {
        this.currentPlayers = 0;
        this.id = id;
        this.init();
    }

    // FUNCTION

    public void addToCurrentScore(int score) {
        this.currentScore += score;
    }

    public void init() {
        this.currentPlayers = 0;
        this.currentScore = 0;
    }

    // GETTERS

    public UUID getId() {
        return this.id;
    }

    public int getCurrentPlayers() {
        return this.currentPlayers;
    }

    public int getCurrentScore() {
        return this.currentScore;
    }
}
