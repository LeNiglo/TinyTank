package com.lefrantguillaume.gameComponent.gameObject.obstacles;

/**
 * Created by andres_k on 13/03/2015.
 */
public enum EnumObstacles {
    WALL_WOOD(0);

    private final int index;

    EnumObstacles(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
