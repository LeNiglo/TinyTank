package com.lefrantguillaume.networkComponent.gameServerComponent.clientmsgs;

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
}