package com.lefrantguillaume.gameComponent.gameObject.spells;

/**
 * Created by andres_k on 13/03/2015.
 */
public enum EnumSpells {
    SHIELD(0),
    INVISIBILITY(1),
    TELEPORT(2);

    private final int id;

    EnumSpells(int id) {
        this.id = id;
    }

    public int getValue() {
        return id;
    }
}
