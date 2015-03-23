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
    private int kills = 0;
    private int deaths = 0;
    private int health = 100;

    public Player(String id, String pseudo, EnumTanks tank, Connection connection) {
        this.id = id;
        this.pseudo = pseudo;
        this.tank = tank;
        this.connection = connection;
    }

    public void hit(int damage) {health = (health - damage < 0 ? 0 : health - damage);}
    public void addKill() {kills += 1;}
    public void addDeath() {deaths += 1;}

    public void setTank(EnumTanks tank) {this.tank = tank;}

    public String getId() {return id;}
    public String getPseudo() {return pseudo;}
    public int getKills() {return kills;}
    public int getDeaths() {return deaths;}
    public int getHealth() {return health;}
    public EnumTanks getTank() {return tank;}
    public Connection getConnection() {return connection;}
    public void setConnection(Connection connectionID) {this.connection = connectionID;}
}
