package com.lefrantguillaume.gameComponent.gameobjects.player;

/**
 * Created by leniglo on 04/07/15.
 */
public class PlayerStats {

    private String id;
    private String pseudo;

    private int currentScore = 0;
    private int nbDeaths = 0;
    private int nbPeopleKilled = 0;

    private int nbShots = 0;
    private int nbHit = 0;
    private int nbGameObjectsDestroyed = 0;

    public PlayerStats(String id, String pseudo) {
        this.id = id;
        this.pseudo = pseudo;
    }

    public void addKill() {
        this.nbPeopleKilled += 1;
    }

    public void addDeath() {
        this.nbDeaths += 1;
    }

    public void addScore(int score) {
        this.currentScore += score;
    }

    public String getPseudo() {
        return this.pseudo;
    }

    public String getId() {
        return this.id;
    }

    public int getCurrentScore() {
        return this.currentScore;
    }

    public void addShoot() {
        this.nbShots += 1;
    }

    public void addHit() {
        this.nbHit += 1;
    }

    public void addGameObjectDestroyed() {
        this.nbGameObjectsDestroyed += 1;
    }

    public void setCurrentScore(int score) {
        currentScore = score;
    }

    public int getKills() {
        return this.nbPeopleKilled;
    }

    public int getDeaths() {
        return this.nbDeaths;
    }

    public int getNbShots() {
        return this.nbShots;
    }

    public int getNbHit() {
        return this.nbHit;
    }

    public int getNbGameObjectsDestroyed() {
        return this.nbGameObjectsDestroyed;
    }
}
