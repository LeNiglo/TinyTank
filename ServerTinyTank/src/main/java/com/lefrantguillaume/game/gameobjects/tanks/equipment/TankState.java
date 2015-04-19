package com.lefrantguillaume.game.gameobjects.tanks.equipment;

import com.lefrantguillaume.game.gameobjects.tanks.types.EnumTanks;


/**
 * Created by andres_k on 18/03/2015.
 */
public class TankState {
    private final float speed;
    private final float maxLife;
    private float armor;
    private float currentLife;
    private float shieldEffect;
    private float slowEffect;
    private float boostEffect;
    private EnumTanks tankType;

    public TankState(float speed, float maxLife, float armor, EnumTanks tankType) {
        this.speed = speed;
        this.maxLife = maxLife;
        this.armor = armor;
        this.currentLife = maxLife;
        this.shieldEffect = 0;
        this.slowEffect = 0;
        this.boostEffect = 0;
        this.tankType = tankType;
    }

    public TankState(TankState tankState) {
        this.speed = tankState.speed;
        this.maxLife = tankState.maxLife;
        this.armor = tankState.armor;
        this.currentLife = tankState.maxLife;
        this.tankType = tankState.tankType;
        this.shieldEffect = 0;
        this.slowEffect = 0;
        this.boostEffect = 0;
    }

    public float getCurrentSpeed() {
        return this.speed - (this.slowEffect * this.speed / 100) + (this.boostEffect * this.speed / 100);
    }

    public float getCurrentLife() {
        return this.currentLife;
    }

    public float getMaxLife() {
        return this.maxLife;
    }

    public float getArmor() {
        return this.armor;
    }

    public float getBoostEffect() {
        return this.boostEffect;
    }

    public float getSlowEffect() {
        return this.slowEffect;
    }

    public float getShieldEffect() {
        return this.shieldEffect;
    }


    // SETTERS
    public void setCurrentLife(float currentLife) {
        this.currentLife = currentLife;
    }

    public void setShieldEffect(float shieldEffect) {
        this.shieldEffect = shieldEffect;
    }

    public void setSlowEffect(float slowEffect) {
        this.slowEffect = slowEffect;
    }

    public void setBoostEffect(float boostEffect) {
        this.boostEffect = boostEffect;
    }

    public void setArmor(float armor) {
        this.armor = armor;
    }

    public EnumTanks getTankType() {
        return tankType;
    }

}
