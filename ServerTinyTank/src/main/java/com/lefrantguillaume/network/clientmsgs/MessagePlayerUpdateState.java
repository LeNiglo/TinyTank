package com.lefrantguillaume.network.clientmsgs;

/**
 * Created by Styve on 25/03/2015.
 */

public class MessagePlayerUpdateState extends MessageModel {
    private float currentLfe;
    private float armor;
    private float shieldEffect;
    private float slowEffect;
    private float boostEffect;

    public MessagePlayerUpdateState() {}

    public float getBoostEffect() {return boostEffect;}
    public float getSlowEffect() {return slowEffect;}
    public float getShieldEffect() {return shieldEffect;}
    public float getArmor() {return armor;}
    public float getCurrentLfe() {return currentLfe;}
    public void setBoostEffect(float boostEffect) {this.boostEffect = boostEffect;}
    public void setSlowEffect(float slowEffect) {this.slowEffect = slowEffect;}
    public void setShieldEffect(float shieldEffect) {this.shieldEffect = shieldEffect;}
    public void setArmor(float armor) {this.armor = armor;}
    public void setCurrentLfe(float currentLfe) {this.currentLfe = currentLfe;}
}