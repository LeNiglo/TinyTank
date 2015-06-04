package com.lefrantguillaume.components.networkComponent.networkGame.messages.msg;

import com.lefrantguillaume.components.networkComponent.networkGame.messages.MessageModel;

/**
 * Created by andres_k on 25/03/2015.
 */

public class MessageObstacleUpdateState extends MessageModel {
    private float currentLife;
    private String obstacleId;

    public MessageObstacleUpdateState() {}

    public MessageObstacleUpdateState(String pseudo, String id, String obstacleId, float currentLife){
        this.currentLife = currentLife;
        this.obstacleId = obstacleId;
        this.playerAction = false;
        this.id = id;
        this.pseudo = pseudo;
    }

    public String getObstacleId() {
        return this.obstacleId;
    }

    public float getCurrentLife() {
        return this.currentLife;
    }
}