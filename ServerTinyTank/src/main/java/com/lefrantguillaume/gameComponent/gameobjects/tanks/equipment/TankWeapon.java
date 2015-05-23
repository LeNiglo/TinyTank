package com.lefrantguillaume.gameComponent.gameobjects.tanks.equipment;


import com.lefrantguillaume.gameComponent.gameobjects.shots.EnumShots;
import com.lefrantguillaume.gameComponent.gameobjects.shots.Shot;

/**
 * Created by andres_k on 18/03/2015.
 */
public class TankWeapon {
    private EnumShots shotType;
    private final float damageShot;
    private final float speedShot;
    private final float lifeShot;


    public TankWeapon(float speedShot, float damageShot, float lifeShot, EnumShots shotType) {
        this.shotType = shotType;
        this.damageShot = damageShot;
        this.speedShot = speedShot;
        this.lifeShot = lifeShot;
    }

    public TankWeapon(TankWeapon tankWeapon) {
        this.lifeShot = tankWeapon.lifeShot;
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

    public float getLifeShot() {
        return this.lifeShot;
    }

    public Shot generateShot(String shotId, String playerId) {
        return new Shot(shotId, playerId, shotType, damageShot, speedShot, lifeShot);
    }

}