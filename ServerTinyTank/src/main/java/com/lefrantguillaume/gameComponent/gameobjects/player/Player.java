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
    private String teamId;
    private Connection connection;
    private Tank tank;
    private PlayerStats stats;

    private int ammo = 1;
    private int timerAmmo = 2;
    private int ammoCooldown = 100;
    private boolean canShoot;
    private Obstacle transportObjective;

    public Player(String id, String pseudo, String teamId, Tank tank, Connection connection) {
        this.stats = new PlayerStats(id, pseudo);
        this.tank = tank;
        this.connection = connection;
        this.teamId = teamId;
        this.canShoot = true;
        this.transportObjective = null;
    }

    public void init(){
        this.tank.revive();
    }

    public void addKill() {
        this.stats.addKill();
    }

    public void addDeath() {
        this.stats.addDeath();
    }

    public MessageModel addScore(int score){
        this.stats.addScore(score);
        return new MessageRoundScore(this.stats.getPseudo(), this.stats.getId(), this.teamId, EnumGameObject.NULL, this.stats.getCurrentScore());
    }

    public void addShoot() {
        this.stats.addShoot();
    }

    public void addHitSomebody() {
        this.stats.addHitSomebody();
    }

    public void addGameObjectDestroyed() {
        this.stats.addGameObjectDestroyed();
    }

    public void revive() {
        this.getTank().revive();
    }

    // SETTERS
    public void setTank(Tank tank) {
        this.tank = tank;
    }

    public void setCurrentScore(int score) {
        this.stats.setCurrentScore(score);
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
        return this.stats.getId();
    }

    public String getPseudo() {
        return this.stats.getPseudo();
    }

    public int getCurrentScore() {
        return this.stats.getCurrentScore();
    }

    public int getKills() {
        return this.stats.getKills();
    }

    public int getDeaths() {
        return this.stats.getDeaths();
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
        return this.stats.getNbShots();
    }

    public int getNbHitSomebody() {
        return this.stats.getNbHitSomebody();
    }

    public int getNbGameObjectsDestroyed() {
        return this.stats.getNbGameObjectsDestroyed();
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

    public PlayerStats getStats() {
        return this.stats;
    }
}
