package com.lefrantguillaume.components.gameComponent.animations;


/**
 * Created by andres_k on 13/03/2015.
 */
public enum EnumSprites {
    //index
    TIGER(1),
    SNIPER(2),
    RUSHER(3),
    WALL(4),
    AREA(5),
    OBJECTIVE(AREA.getIndex()),
    BOMB(AREA.getIndex()),
    //interface
    BACKGROUND(-1), NEXT(-1), PREV(-1),
    STAT_RANK(-1), STAT_TIGER(-1), STAT_SNIPER(-1), STAT_RUSHER(-1),
    PREVIEW_TIGER(-1), PREVIEW_SNIPER(-1), PREVIEW_RUSHER(-1),
    //gameComponent
    MAP(-1),
    IRON_WALL(WALL.getIndex()), PLASMA_WALL(WALL.getIndex()), MINE(WALL.getIndex()),
    TIGER_BODY(TIGER.getIndex()), TIGER_TOP(TIGER.getIndex()), TIGER_SPELL(TIGER.getIndex()), TIGER_HIT(TIGER.getIndex()),
    TIGER_BODY_ENEMY(TIGER.getIndex()), TIGER_TOP_ENEMY(TIGER.getIndex()),
    SNIPER_BODY(SNIPER.getIndex()), SNIPER_TOP(SNIPER.getIndex()), SNIPER_SPELL(SNIPER.getIndex()), SNIPER_HIT(SNIPER.getIndex()),
    SNIPER_BODY_ENEMY(SNIPER.getIndex()), SNIPER_TOP_ENEMY(SNIPER.getIndex()),
    RUSHER_BODY(RUSHER.getIndex()), RUSHER_TOP(RUSHER.getIndex()), RUSHER_SPELL(RUSHER.getIndex()), RUSHER_HIT(RUSHER.getIndex()),
    RUSHER_BODY_ENEMY(RUSHER.getIndex()), RUSHER_TOP_ENEMY(RUSHER.getIndex());


    private final int index;

    EnumSprites(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
