package com.lefrantguillaume.gestGame.gameComponent.animations;

import com.lefrantguillaume.gestGame.gameComponent.gameObject.tanks.types.EnumTanks;

/**
 * Created by andres_k on 13/03/2015.
 */
public enum EnumSprites {
    BACKGROUND(-1), BUTTONS(-1),
    STAT_RANK(-1), STAT_TIGER(-1), STAT_SNIPER(-1), STAT_RUSHER(-1),
    PREVIEW_TIGER(-1), PREVIEW_SNIPER(-1), PREVIEW_RUSHER(-1),
    MAP(-1),
    TIGER_BODY(EnumTanks.TIGER.getIndex()), TIGER_TOP(EnumTanks.TIGER.getIndex()), TIGER_SPELL(EnumTanks.TIGER.getIndex()), TIGER_HIT(EnumTanks.TIGER.getIndex()),
    SNIPER_BODY(EnumTanks.SNIPER.getIndex()), SNIPER_TOP(EnumTanks.SNIPER.getIndex()), SNIPER_SPELL(EnumTanks.SNIPER.getIndex()), SNIPER_HIT(EnumTanks.SNIPER.getIndex()),
    RUSHER_BODY(EnumTanks.RUSHER.getIndex()), RUSHER_TOP(EnumTanks.RUSHER.getIndex()), RUSHER_SPELL(EnumTanks.RUSHER.getIndex()), RUSHER_HIT(EnumTanks.RUSHER.getIndex());



    private final int id;

    EnumSprites(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
