package com.lefrantguillaume.gameComponent.gameobjects.player;

import com.lefrantguillaume.WindowController;

import java.awt.*;

/**
 * Created by leniglo on 04/07/15.
 */
public class PlayerStats {

    private String id;
    private String pseudo;

    private int currentScore = 0;
    private int deaths = 0;
    private int kills = 0;

    private int nbShots = 0;
    private int nbHits = 0;
    private int nbGameObjectsDestroyed = 0;

    public PlayerStats(String id, String pseudo) {
        this.id = id;
        this.pseudo = pseudo;
    }

    public void addKill() {
        this.kills += 1;
    }

    public void addDeath() {
        this.deaths += 1;
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
        this.nbHits += 1;
    }

    public void addGameObjectDestroyed() {
        this.nbGameObjectsDestroyed += 1;
    }

    public void setCurrentScore(int score) {
        currentScore = score;
    }

    public int getKills() {
        return this.kills;
    }

    public int getDeaths() {
        return this.deaths;
    }

    public int getNbShots() {
        return this.nbShots;
    }

    public int getNbHits() {
        return this.nbHits;
    }

    public int getNbGameObjectsDestroyed() {
        return this.nbGameObjectsDestroyed;
    }
}
