package com.lefrantguillaume.components.gameComponent.gameObject.obstacles;

/**
 * Created by andres_k on 13/03/2015.
 */
public enum EnumAnimationObstacle {
    FULL(0),
    HALF(1),
    LOW(2),
    EXPLODE(3);
    private final int index;

    EnumAnimationObstacle(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
