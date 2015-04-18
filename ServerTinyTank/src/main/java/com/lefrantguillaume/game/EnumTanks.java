package com.lefrantguillaume.game;

/**
 * Created by Styve on 23/03/2015.
 */
public enum EnumTanks {
    NULL(-1), TIGER(0), SNIPER(1), RUSHER(2);

    private final int id;

    EnumTanks(int id) { this.id = id;}

    public int getId() {return id;}
}