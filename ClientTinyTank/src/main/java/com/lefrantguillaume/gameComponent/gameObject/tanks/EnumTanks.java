package com.lefrantguillaume.gameComponent.gameObject.tanks;

/**
 * Created by andres_k on 13/03/2015.
 */
public enum EnumTanks {
    TIGER(0),
    SNIPER(1),
    RUSHER(2);

    private final int id;

    EnumTanks(int id) {
        this.id = id;
    }

    public int getValue() {
        return id;
    }
}
