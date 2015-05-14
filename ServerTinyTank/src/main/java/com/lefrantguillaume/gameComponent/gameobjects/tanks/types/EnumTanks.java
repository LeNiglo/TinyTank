package com.lefrantguillaume.gameComponent.gameobjects.tanks.types;

/**
 * Created by andres_k on 13/03/2015.
 */
public enum EnumTanks {
    NULL(-1, "null"),
    TIGER(0, "tiger"),
    SNIPER(1, "sniper"),
    RUSHER(2, "rusher");

    private final int index;
    private final String value;

    EnumTanks(int index, String value) {
        this.index = index;
        this.value = value;
    }

    EnumTanks(EnumTanks enumTanks){
        this.index = enumTanks.index;
        this.value = enumTanks.value;
    }
    public int getIndex() {
        return index;
    }

    public String getValue() {
        return value;
    }

    public static EnumTanks getEnumByIndex(int index) {
        EnumTanks[] tanks = EnumTanks.values();
        int valuesNumber = tanks.length;
        for (int i = 0; i < valuesNumber; i++) {
            EnumTanks type = tanks[i];
            if (type.getIndex() == index)
                return type;
        }
        return NULL;
    }

    public static EnumTanks getEnumByValue(String value) {
        EnumTanks[] tanks = EnumTanks.values();
        int valuesNumber = tanks.length;
        for (int i = 0; i < valuesNumber; i++) {
            EnumTanks type = tanks[i];
            if (type.getValue().equals(value))
                return type;
        }
        return NULL;
    }
}
