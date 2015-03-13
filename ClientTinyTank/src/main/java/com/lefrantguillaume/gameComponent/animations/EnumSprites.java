package com.lefrantguillaume.gameComponent.animations;

/**
 * Created by andres_k on 13/03/2015.
 */
public enum EnumSprites {
    PANZER(0), ROCKET(1);
    private final int id;

    EnumSprites(int id) {
        this.id = id;
    }

    public int getValue() {
        return id;
    }
}
