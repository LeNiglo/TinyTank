package com.lefrantguillaume.gameComponent.animations;

import com.lefrantguillaume.gameComponent.gameObject.tanks.EnumTanks;

/**
 * Created by andres_k on 13/03/2015.
 */
public enum EnumSprites {
    BACKGROUND(-1), BUTTONS(-1),
    STAT_RANK(-1), STAT_TIGER(-1), STAT_SNIPER(-1), STAT_RUSHER(-1),
    PREVIEW_TIGER(-1), PREVIEW_SNIPER(-1), PREVIEW_RUSHER(-1),
    MAP(-1),
    TIGER_BODY(EnumTanks.TIGER.getValue()), TIGER_GUN(EnumTanks.TIGER.getValue()), TIGER_SPELL(EnumTanks.TIGER.getValue()), TIGER_HIT(EnumTanks.TIGER.getValue()),
    SNIPER_BODY(EnumTanks.SNIPER.getValue()), SNIPER_GUN(EnumTanks.SNIPER.getValue()), SNIPER_SPELL(EnumTanks.SNIPER.getValue()), SNIPER_HIT(EnumTanks.SNIPER.getValue()),
    RUSHER_BODY(EnumTanks.RUSHER.getValue()), RUSHER_GUN(EnumTanks.RUSHER.getValue()), RUSHER_SPELL(EnumTanks.RUSHER.getValue()), RUSHER_HIT(EnumTanks.RUSHER.getValue());



    private final int id;

    EnumSprites(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
