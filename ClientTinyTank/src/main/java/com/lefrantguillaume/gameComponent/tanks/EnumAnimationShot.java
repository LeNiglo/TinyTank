package com.lefrantguillaume.gameComponent.tanks;

/**
 * Created by andres_k on 13/03/2015.
 */
public enum EnumAnimationShot {
    HIT(0),
    EXPLODE(1);
    private final int id;

    EnumAnimationShot(int id) {
        this.id = id;
    }

    public int getValue() {
        return id;
    }
}
