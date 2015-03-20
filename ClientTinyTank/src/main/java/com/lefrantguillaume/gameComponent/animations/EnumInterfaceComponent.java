package com.lefrantguillaume.gameComponent.animations;

/**
 * Created by andres_k on 20/03/2015.
 */
public enum EnumInterfaceComponent {
    TIGER(0), SNIPER(1), RUSHER(2), RANK(3),
    BACKGROUND_1(0),
    PREV(0), NEXT(1);

    private final int index;

    EnumInterfaceComponent(int index) {
        this.index = index;
    }

    public int getIndex() {
        return this.index;
    }
}
