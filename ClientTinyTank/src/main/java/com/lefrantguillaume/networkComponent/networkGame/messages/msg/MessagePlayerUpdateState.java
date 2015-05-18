package com.lefrantguillaume.networkComponent.networkGame.messages.msg;

import com.lefrantguillaume.networkComponent.networkGame.messages.MessageModel;

/**
 * Created by andres_k on 12/03/2015.
 */
public class MessagePlayerUpdateState extends MessageModel {
    private float armor;
    private float currentLife;
    private float shieldEffect;
    private float slowEffect;
    private float boostEffect;

    public MessagePlayerUpdateState() {
    }
    public MessagePlayerUpdateState(String pseudo, String id, float armor, float currentLife, float shieldEffect, float slowEffect, float boostEffect) {
        this.armor = armor;
        this.currentLife = currentLife;
        this.shieldEffect = shieldEffect;
        this.slowEffect = slowEffect;
        this.boostEffect = boostEffect;
        this.pseudo = pseudo;
        this.id = id;
        this.playerAction = false;
    }

    public float getArmor() {
        return armor;
    }

    public float getCurrentLife() {
        return currentLife;
    }

    public float getShieldEffect() {
        return shieldEffect;
    }

    public float getSlowEffect() {
        return slowEffect;
    }

    public float getBoostEffect() {
        return boostEffect;
    }
}
