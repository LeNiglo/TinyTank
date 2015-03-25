package com.lefrantguillaume.networkComponent.messages.msg;

import com.lefrantguillaume.networkComponent.messages.MessageModel;

/**
 * Created by andres_k on 12/03/2015.
 */
public class MessageShoot extends MessageModel {
//change to shotId;
    private int valueKeyPressed;
    private float angle;

    public MessageShoot() {
    }

    public MessageShoot(String pseudo, String id, int valueKeyPressed, float angle) {
        this.pseudo = pseudo;
        this.id = id;
        this.playerAction = true;
        this.valueKeyPressed = valueKeyPressed;
        this.angle = angle;
    }

    public int getValueKeyPressed() {
        return this.valueKeyPressed;
    }

    public float getAngle() {
        return this.angle;
    }
}
