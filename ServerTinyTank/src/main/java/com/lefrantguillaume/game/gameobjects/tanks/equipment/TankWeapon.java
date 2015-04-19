package com.lefrantguillaume.game.gameobjects.tanks.equipment;


import com.lefrantguillaume.game.gameobjects.shots.EnumShots;

import java.util.List;

/**
 * Created by andres_k on 18/03/2015.
 */
public class TankWeapon {
    private EnumShots shotType;
    private final float damageShot;
    private final float speedShot;

    public TankWeapon(float speedShot, float damageShot, EnumShots shotType) {
        this.shotType = shotType;
        this.damageShot = damageShot;
        this.speedShot = speedShot;
    }

    public TankWeapon(TankWeapon tankWeapon) {
        this.shotType = tankWeapon.shotType;
        this.damageShot = tankWeapon.damageShot;
        this.speedShot = tankWeapon.speedShot;
    }

    // GETETRS
    public float getDamageShot() {
        return this.damageShot;
    }

    public float getSpeedShot() {
        return this.speedShot;
    }

}
