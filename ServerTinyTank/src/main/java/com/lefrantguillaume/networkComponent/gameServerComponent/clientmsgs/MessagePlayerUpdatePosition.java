package com.lefrantguillaume.networkComponent.gameServerComponent.clientmsgs;

/**
 * Created by Styve on 25/03/2015.
 */

public class MessagePlayerUpdatePosition extends MessageModel {
    private float x;
    private float y;

    public MessagePlayerUpdatePosition() {}

    public MessagePlayerUpdatePosition(MessagePlayerUpdatePosition message) {
        this.x = message.x;
        this.y = message.y;
        this.id = message.id;
        this.pseudo = message.pseudo;
        this.playerAction = false;
    }

    public float getX() {return x;}
    public float getY() {return y;}
    public void setX(float x) {this.x = x;}
    public void setY(float y) {this.y = y;}
}