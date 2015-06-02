package com.lefrantguillaume.components.networkComponent.networkGame.messages.msg;

import com.lefrantguillaume.components.gameComponent.gameObject.EnumGameObject;
import com.lefrantguillaume.components.networkComponent.networkGame.messages.MessageModel;

/**
 * Created by andres_k on 30/04/2015.
 */
public class MessagePutObstacle extends MessageModel {
    private float posX;
    private float posY;
    private float angle;
    private EnumGameObject type;
    private String obstacleId = "";

    public MessagePutObstacle(){

    }

    public MessagePutObstacle(String pseudo, String id,  EnumGameObject type, float posX, float posY, float angle){
        this.pseudo = pseudo;
        this.id = id;
        this.posX = posX;
        this.posY = posY;
        this.angle = angle;
        this.type = type;
    }


    public float getPosX() {
        return this.posX;
    }

    public float getPosY() {
        return this.posY;
    }

    public float getAngle() {
        return this.angle;
    }

    public EnumGameObject getType() {
        return this.type;
    }

    public String getObstacleId() {
        return this.obstacleId;
    }
}
