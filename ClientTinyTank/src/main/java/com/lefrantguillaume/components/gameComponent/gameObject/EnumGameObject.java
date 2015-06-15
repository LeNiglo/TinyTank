package com.lefrantguillaume.components.gameComponent.gameObject;

/**
 * Created by andres_k on 13/03/2015.
 */
public enum EnumGameObject {
    /* Admin */
    NULL(-1, "null"),
    UNBREAKABLE(-1, "unbreakable"),
    /* Tanks */
    TIGER(0, "tiger"),
    SNIPER(1, "sniper"),
    RUSHER(2, "rusher"),
    /* Spells */
    SHIELD(0, "shield"),
    INVISIBILITY(1, "invisibility"),
    TELEPORT(2, "teleport"),
    /* Shots */
    ROCKET(0, "rocket"),
    LASER(1, "laser"),
    MACHINE_GUN(2, "machine_gun"),
    /* Obstacles */
    IRON_WALL(0, "iron_wall"),
    PLASMA_WALL(1, "plasma_wall"),
    MINE(2, "mine"),
    /* Areas */
    KINGDOM_AREA(0, "kingdom_area"),
    TOUCHDOWN_AREA(1, "touchdown_area");


    private final int index;
    private final String value;

    EnumGameObject(int index, String value) {
        this.index = index;
        this.value = value;
    }

    EnumGameObject(EnumGameObject enumGameObject) {
        this.index = enumGameObject.index;
        this.value = enumGameObject.value;
    }

    public int getIndex() {
        return index;
    }

    public String getValue() {
        return value;
    }

    public static EnumGameObject getEnumByIndex(int index) {
        EnumGameObject[] tanks = EnumGameObject.values();
        int valuesNumber = tanks.length;
        for (int i = 0; i < valuesNumber; i++) {
            EnumGameObject type = tanks[i];
            if (type.getIndex() == index)
                return type;
        }
        return NULL;
    }

    public static EnumGameObject getEnumByValue(String value) {
        EnumGameObject[] tanks = EnumGameObject.values();
        int valuesNumber = tanks.length;
        for (int i = 0; i < valuesNumber; i++) {
            EnumGameObject type = tanks[i];
            if (type.getValue().equals(value))
                return type;
        }
        return NULL;
    }
}
