package com.lefrantguillaume.gameComponent.gameObject.obstacles;

/**
 * Created by andres_k on 13/03/2015.
 */
public enum EnumObstacles {
    BOX(0);

    private final int id;

    EnumObstacles(int id) {
        this.id = id;
    }

    public int getValue() {
        return id;
    }
}
