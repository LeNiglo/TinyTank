package com.lefrantguillaume.gameComponent.gameobjects.player;

import com.esotericsoftware.kryonet.Connection;
import com.lefrantguillaume.gameComponent.EnumGameObject;
import com.lefrantguillaume.gameComponent.gameobjects.tanks.Tank;

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

    private int nbDeaths = 0;
    private int nbShots = 0;
    private int nbHitSomebody = 0;
    private int nbGameObjectsDestroyed = 0;
    private int nbPeopleKilled = 0;
    private int ammo = 1;
    private int timerAmmo = 2;
    private int ammoCooldown = 100;
    private boolean canShoot = true;
    private boolean transportObjective = false;

    public Player(String id, String pseudo, String teamId, Tank tank, Connection connection) {
        this.id = id;
        this.pseudo = pseudo;
        this.tank = tank;
        this.connection = connection;
        this.teamId = teamId;
    }

    public void addKill() {
        nbPeopleKilled += 1;
    }

    public void addDeath() {
        nbDeaths += 1;
    }

    public void addShoot() {
        nbShots += 1;
    }

    public void addhitSomebody() {
        nbHitSomebody += 1;
    }

    public void addGameObjectDestroyed() {
        nbGameObjectsDestroyed += 1;
    }

    public void revive() {
        this.getTank().revive();
    }

    // SETTERS
    public void setTank(Tank tank) {
        this.tank = tank;
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

    public void setTransportObjective(boolean value){
        this.transportObjective = value;
    }

    // GETTERS
    public String getId() {
        return id;
    }

    public String getPseudo() {
        return pseudo;
    }

    public int getKills() {
        return nbPeopleKilled;
    }

    public int getDeaths() {
        return nbDeaths;
    }

    public boolean isTransportObjective(){
        return this.transportObjective;
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
