package com.lefrantguillaume.network.clientmsgs;

/**
 * Created by Styve on 25/03/2015.
 */

public class MessagePlayerUpdatePosition extends MessageModel {
    private float x;
    private float y;

    public MessagePlayerUpdatePosition() {}

    public float getX() {return x;}
    public float getY() {return y;}
    public void setX(float x) {this.x = x;}
    public void setY(float y) {this.y = y;}
}