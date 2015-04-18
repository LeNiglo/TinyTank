package com.lefrantguillaume.network.clientmsgs;

/**
 * Created by Styve on 25/03/2015.
 */

public class MessageSpell extends MessageModel {
    private int valueKeyPressed;
    private float angle;
    private float x;
    private float y;

    public MessageSpell() {}

    public int getValueKeyPressed() { return valueKeyPressed; }
    public float getAngle() {return angle;}
    public float getX() {return x;}
    public float getY() {return y;}
    public void setValueKeyPressed(int valueKeyPressed) { this.valueKeyPressed = valueKeyPressed; }
    public void setAngle(float angle) {this.angle = angle;}
    public void setX(float x) {this.x = x;}
    public void setY(float y) {this.y = y;}
}