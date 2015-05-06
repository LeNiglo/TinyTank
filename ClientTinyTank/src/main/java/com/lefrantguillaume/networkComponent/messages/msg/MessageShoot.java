package com.lefrantguillaume.networkComponent.messages.msg;

import com.lefrantguillaume.networkComponent.messages.MessageModel;

/**
 * Created by andres_k on 12/03/2015.
 */
public class MessageShoot extends MessageModel {
    private String shotId = "";
    private float angle;

    public MessageShoot() {
    }

    public MessageShoot(String pseudo, String id, float angle) {
        this.pseudo = pseudo;
        this.id = id;
        this.playerAction = true;
        this.angle = angle;
    }

    public String getShotId() {
        return this.shotId;
    }

    public float getAngle() {
        return this.angle;
    }
}
