package com.lefrantguillaume.game;

/**
 * Created by Styve on 25/03/2015.
 */
public enum EnumAttack {
    BASIC(0), SPELL(1);
    private final int id;

    EnumAttack(int id) { this.id = id;}

    public int getId() {return id;}
}
