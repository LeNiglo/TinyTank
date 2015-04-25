package com.lefrantguillaume.network.clientmsgs;

/**
 * Created by Styve on 25/03/2015.
 */

public class MessagePlayerUpdateState extends MessageModel {
    private float currentLife;
    private float armor;
    private float shieldEffect;
    private float slowEffect;
    private float boostEffect;

    public MessagePlayerUpdateState() {}

    public MessagePlayerUpdateState(String pseudo, String id, float currentLife, float armor, float shieldEffect, float slowEffect, float boostEffect){
        this.currentLife = currentLife;
        this.armor = armor;
        this.shieldEffect = shieldEffect;
        this.slowEffect = slowEffect;
        this.boostEffect = boostEffect;
        this.playerAction = false;
        this.id = id;
        this.pseudo = pseudo;
    }
    public float getBoostEffect() {return boostEffect;}
    public float getSlowEffect() {return slowEffect;}
    public float getShieldEffect() {return shieldEffect;}
    public float getArmor() {return armor;}
    public float getCurrentLife() {return currentLife;}
    public void setBoostEffect(float boostEffect) {this.boostEffect = boostEffect;}
    public void setSlowEffect(float slowEffect) {this.slowEffect = slowEffect;}
    public void setShieldEffect(float shieldEffect) {this.shieldEffect = shieldEffect;}
    public void setArmor(float armor) {this.armor = armor;}
    public void setCurrentLife(float currentLfe) {this.currentLife = currentLfe;}
}