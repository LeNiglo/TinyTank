package com.lefrantguillaume.game;

/**
 * Created by Styve on 25/03/2015.
 */
public enum EnumObjects {
    WALL(0), TANK(1), SHOT(2), ENTITY(3);
    private final int id;

    EnumObjects(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
