package com.lefrantguillaume.game;

import com.esotericsoftware.kryonet.Connection;

/**
 * Created by Styve on 19/03/2015.
 */

public class Player {
    private String id;
    private String pseudo;
    private EnumTanks tank;
    private Connection connection;
    private int nbDeaths = 0;
    private int nbShots = 0;
    private int nbHitSomebody = 0;
    private int nbGameObjectsDestroyed = 0;
    private int nbPeopleKilled = 0;
    private int health = 100;
    private int ammo = 1;
    private int timerAmmo = 2;
    private int ammoCooldown = 500;
    private boolean canShoot = true;

    public Player(String id, String pseudo, EnumTanks tank, Connection connection) {
        this.id = id;
        this.pseudo = pseudo;
        this.tank = tank;
        this.connection = connection;
    }

    public void hit(int damage) {health = (health - damage < 0 ? 0 : health - damage);}
    public void addKill() {nbPeopleKilled += 1;}
    public void addDeath() {nbDeaths += 1;}
    public void addShoot() {nbShots += 1;}
    public void addhitSomebody() {nbHitSomebody += 1;}
    public void addGameObjectDestroyed() {nbGameObjectsDestroyed += 1;}
    public void setTank(EnumTanks tank) {this.tank = tank;}
    public void setConnection(Connection connectionID) {this.connection = connectionID;}
    public void setCanShoot(boolean canShoot) {this.canShoot = canShoot;}
    public void setAmmo(int ammo) {this.ammo = ammo;}

    public String getId() {return id;}
    public String getPseudo() {return pseudo;}
    public int getKills() {return nbPeopleKilled;}
    public int getDeaths() {return nbDeaths;}
    public int getHealth() {return health;}
    public EnumTanks getTank() {return tank;}
    public Connection getConnection() {return connection;}
    public int getNbShots() {return nbShots;}
    public int getNbHitSomebody() {return nbHitSomebody;}
    public int getNbGameObjectsDestroyed() {return nbGameObjectsDestroyed;}
    public boolean isCanShoot() {return canShoot;}
    public int getTimerAmmo() {return timerAmmo;}
    public int getAmmo() {return ammo;}
    public int getAmmoCooldown() {return ammoCooldown;}
}
