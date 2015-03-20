package com.lefrantguillaume.networkComponent.messages.msg;

import com.lefrantguillaume.networkComponent.messages.MessageModel;

/**
 * Created by andres_k on 19/03/2015.
 */
public class MessageSpell extends MessageModel {
    private int valueKeyPressed;
    private float angle;
    private float x;
    private float y;

    public MessageSpell() {
    }

    public MessageSpell(String pseudo, String id, int valueKeyPressed, float angle, float x, float y) {
        this.pseudo = pseudo;
        this.id = id;
        this.playerAction = true;
        this.valueKeyPressed = valueKeyPressed;
        this.angle = angle;
        this.x = x;
        this.y = y;
    }

    // GETTERS
    public int getValueKeyPressed() {
        return this.valueKeyPressed;
    }

    public float getAngle() {
        return this.angle;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }
}
