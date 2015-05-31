package com.lefrantguillaume.components.gameComponent.gameObject.tanks.equipment;

/**
 * Created by andres_k on 13/03/2015.
 */
public enum EnumAnimationTank {
    BASIC(0),
    EXPLODE(1);
    private final int id;

    EnumAnimationTank(int id) {
        this.id = id;
    }

    public int getValue() {
        return id;
    }
}
