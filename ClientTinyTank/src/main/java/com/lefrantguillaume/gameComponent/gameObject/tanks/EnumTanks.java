package com.lefrantguillaume.gameComponent.gameObject.tanks;

/**
 * Created by andres_k on 13/03/2015.
 */
public enum EnumTanks {
    NULL(-1),
    TIGER(0),
    SNIPER(1),
    RUSHER(2);

    private final int id;

    EnumTanks(int id) {
        this.id = id;
    }

    public int getValue() {
        return id;
    }

    public static EnumTanks getTankById(int id){
        EnumTanks[] tanks = EnumTanks.values();
        int valuesNumber = tanks.length;
        for (int i = 0 ; i < valuesNumber; i++) {
            EnumTanks type = tanks[i];
            if (type.getValue() == id)
                return type;
        }
        return NULL;
    }
}
