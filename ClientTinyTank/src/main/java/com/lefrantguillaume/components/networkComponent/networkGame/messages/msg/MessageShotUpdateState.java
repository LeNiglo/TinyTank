package com.lefrantguillaume.components.networkComponent.networkGame.messages.msg;

import com.lefrantguillaume.components.networkComponent.networkGame.messages.MessageModel;

/**
 * Created by andres_k on 25/03/2015.
 */

public class MessageShotUpdateState extends MessageModel {
    private float currentDamageShot;
    private String shotId;

    public MessageShotUpdateState() {}

    public MessageShotUpdateState(String pseudo, String id, String shotId, float currentDamageShot){
        this.currentDamageShot = currentDamageShot;
        this.shotId = shotId;
        this.playerAction = false;
        this.id = id;
        this.pseudo = pseudo;
    }

    public float getCurrentDamageShot() {
        return this.currentDamageShot;
    }

    public String getShotId() {
        return this.shotId;
    }
}