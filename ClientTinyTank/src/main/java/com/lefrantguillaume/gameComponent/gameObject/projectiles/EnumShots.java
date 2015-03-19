package com.lefrantguillaume.gameComponent.gameObject.projectiles;

/**
 * Created by andres_k on 13/03/2015.
 */
public enum EnumShots {
    ROCKET(0),
    LASER(1);

    private final int id;

    EnumShots(int id) {
        this.id = id;
    }

    public int getValue() {
        return id;
    }
}
