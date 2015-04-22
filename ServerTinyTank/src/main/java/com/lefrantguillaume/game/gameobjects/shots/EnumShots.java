package com.lefrantguillaume.game.gameobjects.shots;

/**
 * Created by andres_k on 13/03/2015.
 */
public enum EnumShots {
    NULL(-1, "null"),
    ROCKET(0, "rocket"),
    LASER(1, "laser"),
    MACHINE_GUN(2, "machine_gun");

    private final int index;
    private final String value;

    EnumShots(int index, String value) {
        this.index = index;
        this.value = value;
    }

    public int getIndex() {
        return index;
    }

    public String getValue() {
        return value;
    }
    public static EnumShots getEnumByIndex(int index) {
        EnumShots[] shots = EnumShots.values();
        int valuesNumber = shots.length;
        for (int i = 0; i < valuesNumber; i++) {
            EnumShots type = shots[i];
            if (type.getIndex() == index)
                return type;
        }
        return NULL;
    }

    public static EnumShots getEnumByValue(String value) {
        EnumShots[] shots = EnumShots.values();
        int valuesNumber = shots.length;
        for (int i = 0; i < valuesNumber; i++) {
            EnumShots type = shots[i];
            if (type.getValue().equals(value))
                return type;
        }
        return NULL;
    }
}
