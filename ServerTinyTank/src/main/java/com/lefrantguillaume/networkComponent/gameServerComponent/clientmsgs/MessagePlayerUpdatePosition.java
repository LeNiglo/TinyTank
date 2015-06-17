package com.lefrantguillaume.networkComponent.gameServerComponent.clientmsgs;

/**
 * Created by andres_k 15/06/2015.
 */

public class MessagePlayerUpdatePosition extends MessageModel {
    private float x;
    private float y;
    private boolean resetMove;

    public MessagePlayerUpdatePosition() {
        this.resetMove = false;
        this.playerAction = false;
    }

    public MessagePlayerUpdatePosition(MessagePlayerUpdatePosition message) {
        this.x = message.x;
        this.y = message.y;
        this.resetMove = message.resetMove;
        this.id = message.id;
        this.pseudo = message.pseudo;
        this.playerAction = false;
    }

    // GETTERS
    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public boolean isResetMove(){
        return this.resetMove;
    }

    // SETTERS
    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setResetMove(boolean value){
        this.resetMove = value;
    }
}