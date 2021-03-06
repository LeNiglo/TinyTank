package com.lefrantguillaume.gameComponent.gameMode;

/**
 * Created by andres_k on 13/05/2015.
 */
public class Team {
    private String id;
    private String name;
    private int currentScore;
    private int currentPlayers;

    public Team(String id, String name) {
        this.currentPlayers = 0;
        this.id = id;
        this.name = name;
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

    public void changeCurrentPlayers(int value){
        this.currentPlayers += value;
    }

    // GETTERS

    public String getName() {
        return this.name;
    }

    public String getId() {
        return this.id;
    }

    public int getCurrentPlayers() {
        return this.currentPlayers;
    }

    public int getCurrentScore() {
        return this.currentScore;
    }
}
