package com.lefrantguillaume.gameComponent.gameMode;

/**
 * Created by Styve on 19/03/2015.
 */
public enum EnumGameMode {
    FreeForAll(0), TeamDeathMatch(1), TouchDawn(2), Kingdom(3);

    private int index;

    EnumGameMode(int index) {
        this.index = index;
    }

    public int getIndex(){
        return this.index;
    }
}
