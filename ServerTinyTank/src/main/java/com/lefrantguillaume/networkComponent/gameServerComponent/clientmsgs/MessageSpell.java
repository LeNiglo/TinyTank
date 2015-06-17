package com.lefrantguillaume.networkComponent.gameServerComponent.clientmsgs;

import com.lefrantguillaume.gameComponent.EnumGameObject;

/**
 * Created by Styve on 25/03/2015.
 */

public class MessageSpell extends MessageModel {
    private String spellId;
    private EnumGameObject type;
    private float angle;
    private float posX;
    private float posY;

    public MessageSpell() {
    }

    // GETTERS
    public float getAngle() {
        return this.angle;
    }

    public float getPosX() {
        return this.posX;
    }

    public float getPosY() {
        return this.posY;
    }

    public String getSpellId(){
        return this.spellId;
    }

    public EnumGameObject getType(){
        return this.type;
    }

    // SETTERS
    public void setAngle(float angle) {
        this.angle = angle;
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }

    public void setSpellId(String spellId){
        this.spellId = spellId;
    }

    public void setType(EnumGameObject type){
        this.type = type;
    }
}