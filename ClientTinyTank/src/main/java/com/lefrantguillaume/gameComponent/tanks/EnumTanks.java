package com.lefrantguillaume.gameComponent.tanks;

/**
 * Created by andres_k on 13/03/2015.
 */
public enum EnumTanks {
    PANZER(0),
    SNIPER(1);

    private final int id;

    EnumTanks(int id) {
        this.id = id;
    }

    public int getValue() {
        return id;
    }
}