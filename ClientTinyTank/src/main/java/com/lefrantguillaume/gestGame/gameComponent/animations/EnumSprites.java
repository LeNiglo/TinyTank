package com.lefrantguillaume.gestGame.gameComponent.animations;


/**
 * Created by andres_k on 13/03/2015.
 */
public enum EnumSprites {
    //index
    TIGER(1),
    SNIPER(2),
    RUSHER(3),
    WALL(4),
    //interface
    BACKGROUND(-1), BUTTONS(-1),
    STAT_RANK(-1), STAT_TIGER(-1), STAT_SNIPER(-1), STAT_RUSHER(-1),
    PREVIEW_TIGER(-1), PREVIEW_SNIPER(-1), PREVIEW_RUSHER(-1),
    //game
    MAP(-1),
    WALL_WOOD(WALL.getIndex()),
    TIGER_BODY(TIGER.getIndex()), TIGER_TOP(TIGER.getIndex()), TIGER_SPELL(TIGER.getIndex()), TIGER_HIT(TIGER.getIndex()),
    SNIPER_BODY(SNIPER.getIndex()), SNIPER_TOP(SNIPER.getIndex()), SNIPER_SPELL(SNIPER.getIndex()), SNIPER_HIT(SNIPER.getIndex()),
    RUSHER_BODY(RUSHER.getIndex()), RUSHER_TOP(RUSHER.getIndex()), RUSHER_SPELL(RUSHER.getIndex()), RUSHER_HIT(RUSHER.getIndex());



    private final int index;

    EnumSprites(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
