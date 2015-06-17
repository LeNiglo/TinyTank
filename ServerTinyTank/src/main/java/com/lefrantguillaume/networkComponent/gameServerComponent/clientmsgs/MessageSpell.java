package com.lefrantguillaume.networkComponent.gameServerComponent.clientmsgs;

/**
 * Created by Styve on 25/03/2015.
 */

public class MessageSpell extends MessageModel {
    private String idSpell;
    private float angle;
    private float x;
    private float y;

    public MessageSpell() {
    }

    // GETTERS
    public float getAngle() {
        return this.angle;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public String getIdSpell(){
        return this.idSpell;
    }

    // SETTERS
    public void setAngle(float angle) {
        this.angle = angle;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setIdSpell(String idSpell){
        this.idSpell = idSpell;
    }
}