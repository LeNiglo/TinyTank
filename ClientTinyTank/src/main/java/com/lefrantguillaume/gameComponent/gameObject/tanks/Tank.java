package com.lefrantguillaume.gameComponent.gameObject.tanks;

import com.lefrantguillaume.Utils.stockage.Tuple;
import com.lefrantguillaume.gameComponent.gameObject.projectiles.Shot;
import com.lefrantguillaume.gameComponent.animations.Animator;

/**
 * Created by andres_k on 13/03/2015.
 */
public class Tank {
    protected TankWeapon tankWeapon;
    protected TankState tankState;
    protected EnumTanks type;
    protected Animator tankAnimator;
    protected Animator gunAnimator;
    protected Animator shotAnimator;


    // FUNCTIONS
    public Shot generateShot(String userId, Tuple<Float, Float, Float> coord) {
        Shot shot = new Shot(userId, this.tankWeapon.getDamageShot(), this.tankWeapon.getSpeedShot(), new Animator(this.shotAnimator), coord);
        return shot;
    }

    // GETTERS
    public EnumTanks getType() {
        return this.type;
    }

    public Animator getTankAnimator() {
        return this.tankAnimator;
    }

    public Animator getGunAnimator() {
        return this.gunAnimator;
    }

    public Animator getShotAnimator() {
        return this.shotAnimator;
    }

    public TankWeapon getTankWeapon(){
        return this.tankWeapon;
    }

    public TankState getTankState(){
        return this.tankState;
    }

    // SETTERS
    public void setTankAnimator(Animator animations) {
        this.tankAnimator = animations;
    }

    public void setGunAnimator(Animator animations) {
        this.gunAnimator = animations;
    }
}
