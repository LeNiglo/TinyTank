package com.lefrantguillaume.components.gameComponent.gameObject.projectiles;

/**
 * Created by andres_k on 13/03/2015.
 */
public enum EnumAnimationShot {
    HIT(0),
    EXPLODE(1);
    private final int index;

    EnumAnimationShot(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
