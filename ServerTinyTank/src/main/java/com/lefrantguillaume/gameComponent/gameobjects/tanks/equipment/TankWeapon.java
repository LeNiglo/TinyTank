package com.lefrantguillaume.gameComponent.gameobjects.tanks.equipment;


import com.lefrantguillaume.gameComponent.EnumGameObject;
import com.lefrantguillaume.gameComponent.gameobjects.shots.Shot;

/**
 * Created by andres_k on 18/03/2015.
 */
public class TankWeapon {
    private EnumGameObject shotType;
    private final float damageShot;
    private final float speedShot;


    public TankWeapon(float speedShot, float damageShot, EnumGameObject shotType) {
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


    public Shot generateShot(String shotId, String playerId) {
        return new Shot(shotId, playerId, this.shotType, this.damageShot, this.speedShot);
    }

}
