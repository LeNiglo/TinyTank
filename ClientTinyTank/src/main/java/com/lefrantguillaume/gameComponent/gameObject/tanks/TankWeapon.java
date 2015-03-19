package com.lefrantguillaume.gameComponent.gameObject.tanks;

import com.lefrantguillaume.gameComponent.gameObject.spells.Spell;

/**
 * Created by andres_k on 18/03/2015.
 */
public class TankWeapon {
    private final float damageShot;
    private final float speedShot;
    private final Spell spell;

    public TankWeapon(float damageShot, float speedShot, Spell spell) {
        this.damageShot = damageShot;
        this.speedShot = speedShot;
        this.spell = spell;
    }

    // GETETRS
    public float getDamageShot() {
        return this.damageShot;
    }

    public float getSpeedShot() {
        return this.speedShot;
    }

    public Spell getSpell(){
        return this.spell;
    }
}
