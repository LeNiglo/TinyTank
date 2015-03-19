package com.lefrantguillaume.gameComponent.animations;

import com.lefrantguillaume.gameComponent.gameObject.tanks.EnumTanks;

/**
 * Created by andres_k on 13/03/2015.
 */
public enum EnumSprites {
    MAP(-1),
    PANZER_BODY(EnumTanks.PANZER.getValue()), PANZER_GUN(EnumTanks.PANZER.getValue()), PANZER_SPELL(EnumTanks.PANZER.getValue()), PANZER_ROCKET(EnumTanks.PANZER.getValue());


    private final int id;

    EnumSprites(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
