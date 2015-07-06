package com.lefrantguillaume.gameComponent.gameobjects.tanks.equipment;


import com.lefrantguillaume.gameComponent.EnumGameObject;
import com.lefrantguillaume.gameComponent.gameobjects.shots.Shot;
import com.lefrantguillaume.utils.ActivatedTimer;

/**
 * Created by andres_k on 18/03/2015.
 */
public class TankWeapon {
    private EnumGameObject shotType;
    private final float damageShot;
    private final float speedShot;
    private ActivatedTimer activatedTimer;


    public TankWeapon(float speedShot, float damageShot, EnumGameObject shotType, long cooldownHit) {
        this.shotType = shotType;
        this.damageShot = damageShot;
        this.speedShot = speedShot;
        this.activatedTimer = new ActivatedTimer(true, false, cooldownHit);
    }

    public TankWeapon(TankWeapon tankWeapon) {
        this.shotType = tankWeapon.shotType;
        this.damageShot = tankWeapon.damageShot;
        this.speedShot = tankWeapon.speedShot;
        this.activatedTimer = new ActivatedTimer(tankWeapon.activatedTimer);
    }

    // FUNCTIONS
    public Shot generateShot(String shotId, String playerId) {
        if (this.activatedTimer.isActivated()) {
            Shot shot = new Shot(shotId, playerId, this.shotType, this.damageShot, this.speedShot);

            this.activatedTimer.setActivated(false);
            this.activatedTimer.startTimer();
            return shot;
        }
        return null;
    }

    // GETETRS
    public float getDamageShot() {
        return this.damageShot;
    }

    public float getSpeedShot() {
        return this.speedShot;
    }

    public long getCooldown(){
        return this.activatedTimer.getDelay();
    }

    public boolean isActivated(){
        return this.activatedTimer.isActivated();
    }

}
