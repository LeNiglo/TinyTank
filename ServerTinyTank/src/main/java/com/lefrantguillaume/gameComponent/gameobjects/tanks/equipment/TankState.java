package com.lefrantguillaume.gameComponent.gameobjects.tanks.equipment;

import com.lefrantguillaume.gameComponent.EnumGameObject;
import com.lefrantguillaume.gameComponent.gameobjects.shots.Shot;
import com.lefrantguillaume.networkComponent.gameServerComponent.clientmsgs.MessagePlayerUpdateState;
import com.lefrantguillaume.utils.Block;

import java.util.ArrayList;
import java.util.List;


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
    private EnumGameObject tankType;
    private List<Block> collisionObject;

    public TankState(float speed, float maxLife, float armor, EnumGameObject tankType) {
        this.speed = speed;
        this.maxLife = maxLife;
        this.armor = armor;
        this.currentLife = maxLife;
        this.tankType = tankType;
        this.shieldEffect = 0;
        this.slowEffect = 0;
        this.boostEffect = 0;
        this.collisionObject = new ArrayList<>();
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
        this.collisionObject = new ArrayList<Block>();
        for (int i = 0; i < tankState.collisionObject.size(); ++i) {
            this.collisionObject.add(tankState.collisionObject.get(i));
        }
    }

    // FUNCTIONS

    public void addCollisionObject(Block block) {
        this.collisionObject.add(block);
    }

    public MessagePlayerUpdateState getHit(String pseudo, String id, Shot shot) {
        MessagePlayerUpdateState msg;

        this.currentLife -= shot.getDamageShot();
        if (this.currentLife < 0) {
            this.currentLife = 0;
        }
        msg = new MessagePlayerUpdateState(pseudo, id, this.currentLife, this.armor, this.shieldEffect, this.slowEffect, this.boostEffect);
        return msg;
    }

    public void init() {
        this.shieldEffect = 0;
        this.slowEffect = 0;
        this.boostEffect = 0;
        this.currentLife = this.maxLife;
    }
    // GETTERS

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

    public List<Block> getCollisionObject(){
        return this.collisionObject;
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

    public EnumGameObject getTankType() {
        return tankType;
    }

}
