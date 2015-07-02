package com.lefrantguillaume.gameComponent.gameobjects.player;

import com.esotericsoftware.kryonet.Connection;
import com.lefrantguillaume.gameComponent.EnumGameObject;
import com.lefrantguillaume.gameComponent.gameobjects.obstacles.Obstacle;
import com.lefrantguillaume.gameComponent.gameobjects.tanks.Tank;
import com.lefrantguillaume.networkComponent.gameServerComponent.clientmsgs.MessageModel;
import com.lefrantguillaume.networkComponent.gameServerComponent.clientmsgs.MessageRoundScore;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Styve on 19/03/2015.
 */

public class Player {
    private String id;
    private String pseudo;
    private String teamId;
    private Connection connection;
    private Tank tank;

    private int currentScore = 0;
    private int nbDeaths = 0;
    private int nbShots = 0;
    private int nbHitSomebody = 0;
    private int nbGameObjectsDestroyed = 0;
    private int nbPeopleKilled = 0;
    private int ammo = 1;
    private int timerAmmo = 2;
    private int ammoCooldown = 100;
    private boolean canShoot;
    private Obstacle transportObjective;

    public Player(String id, String pseudo, String teamId, Tank tank, Connection connection) {
        this.id = id;
        this.pseudo = pseudo;
        this.tank = tank;
        this.connection = connection;
        this.teamId = teamId;
        this.canShoot = true;
        this.transportObjective = null;
    }

    public void init(){
        this.currentScore = 0;
        this.tank.revive();
    }

    public void addKill() {
        this.nbPeopleKilled += 1;
    }

    public void addDeath() {
        this.nbDeaths += 1;
    }

    public MessageModel addScore(int score){
        this.currentScore += score;
        return new MessageRoundScore(this.pseudo, this.id, this.teamId, EnumGameObject.NULL, this.currentScore);
    }

    public void addShoot() {
        this.nbShots += 1;
    }

    public void addhitSomebody() {
        this.nbHitSomebody += 1;
    }

    public void addGameObjectDestroyed() {
        this.nbGameObjectsDestroyed += 1;
    }

    public void revive() {
        this.getTank().revive();
    }

    // SETTERS
    public void setTank(Tank tank) {
        this.tank = tank;
    }

    public void setCurrentScore(int score) {
        this.currentScore = score;
    }

    public void setConnection(Connection connectionID) {
        this.connection = connectionID;
    }

    public void setCanShoot(boolean canShoot) {
        this.canShoot = canShoot;
    }

    public void setAmmo(int ammo) {
        this.ammo = ammo;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public void setTransportObjective(Obstacle value) {
        this.transportObjective = value;
    }

    // GETTERS
    public String getId() {
        return id;
    }

    public String getPseudo() {
        return pseudo;
    }

    public int getCurrentScore() {
        return this.currentScore;
    }

    public int getKills() {
        return nbPeopleKilled;
    }

    public int getDeaths() {
        return nbDeaths;
    }

    public Obstacle getTransportObjective() {
        return this.transportObjective;
    }

    public boolean isTransportObjective() {
        if (this.transportObjective == null) {
            return false;
        } else {
            return true;
        }
    }

    public Tank getTank() {
        return tank;
    }

    public Connection getConnection() {
        return connection;
    }

    public int getNbShots() {
        return nbShots;
    }

    public int getNbHitSomebody() {
        return nbHitSomebody;
    }

    public int getNbGameObjectsDestroyed() {
        return nbGameObjectsDestroyed;
    }

    public boolean isCanShoot() {
        return canShoot;
    }

    public int getTimerAmmo() {
        return timerAmmo;
    }

    public int getAmmo() {
        return ammo;
    }

    public int getAmmoCooldown() {
        return ammoCooldown;
    }

    public String getTeamId() {
        return this.teamId;
    }

    public List<EnumGameObject> getIgnoredObjectList() {
        List<EnumGameObject> types = new ArrayList<>();

        return types;
    }
}
