package com.lefrantguillaume.gameComponent;

/**
 * Created by andres_k on 13/03/2015.
 */
public enum EnumGameObject {
    /* Admin */
    NULL("null"),
    UNBREAKABLE("unbreakable"),
    /* Tanks */
    TIGER("tiger"),
    SNIPER("sniper"),
    RUSHER("rusher"),
    TIGER_ENEMY("tigerEnemy"),
    SNIPER_ENEMY("sniperEnemy"),
    RUSHER_ENEMY("rusherEnemy"),
    /* Spells */
    SHIELD("shield"),
    INVISIBILITY("invisibility"),
    TELEPORT("teleport"),
    /* Shots */
    ROCKET("rocket"),
    LASER("laser"),
    MACHINE_GUN("machine_gun"),
    /* Obstacles */
    IRON_WALL("iron_wall"),
    PLASMA_WALL("plasma_wall"),
    MINE("mine"),
    MINE_ENEMY("mineEnemy"),
    /* Areas */
    SPAWN_AREA("spawn_area"),
    OBJECTIVE_AREA("objective_area");


    private final String value;

    EnumGameObject(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
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

    public static EnumGameObject getEnemyEnum(EnumGameObject type){
        EnumGameObject[] tanks = EnumGameObject.values();
        int valuesNumber = tanks.length;
        for (int i = 0; i < valuesNumber; i++) {
            EnumGameObject enemy = tanks[i];
            if (enemy.getValue().equals(type.getValue() + "Enemy"))
                return enemy;
        }
        return type;
    }
}
