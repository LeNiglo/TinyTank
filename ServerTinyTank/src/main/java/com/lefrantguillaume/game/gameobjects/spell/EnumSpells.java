package com.lefrantguillaume.game.gameobjects.spell;

/**
 * Created by andres_k on 13/03/2015.
 */
public enum EnumSpells {
    NULL(-1, "null"),
    SHIELD(0, "shield"),
    INVISIBILITY(1, "invisibility"),
    TELEPORT(2,  "teleport");

    private final int index;
    private final String value;

    EnumSpells(int index, String value) {
        this.index = index;
        this.value = value;
    }

    public int getIndex() {
        return index;
    }

    public String getValue() {
        return value;
    }

    public static EnumSpells getEnumByIndex(int index) {
        EnumSpells[] spells = EnumSpells.values();
        int valuesNumber = spells.length;
        for (int i = 0; i < valuesNumber; i++) {
            EnumSpells type = spells[i];
            if (type.getIndex() == index)
                return type;
        }
        return NULL;
    }

    public static EnumSpells getEnumByValue(String value) {
        EnumSpells[] spells = EnumSpells.values();
        int valuesNumber = spells.length;
        for (int i = 0; i < valuesNumber; i++) {
            EnumSpells type = spells[i];
            if (type.getValue().equals(value))
                return type;
        }
        return NULL;
    }
}
