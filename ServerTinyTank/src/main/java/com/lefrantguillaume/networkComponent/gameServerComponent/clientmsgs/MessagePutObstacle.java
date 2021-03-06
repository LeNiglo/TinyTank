package com.lefrantguillaume.networkComponent.gameServerComponent.clientmsgs;


import com.lefrantguillaume.gameComponent.EnumGameObject;

/**
 * Created by Styve on 26/03/2015.
 */
public class MessagePutObstacle extends MessageModel {
    private float posX;
    private float posY;
    private float angle;
    private EnumGameObject type;
    private String obstacleId;

    public MessagePutObstacle() {}

    public MessagePutObstacle(String pseudo, String id,  String obstacleId, EnumGameObject type, float posX, float posY, float angle){
        this.pseudo = pseudo;
        this.id = id;
        this.obstacleId = obstacleId;
        this.posX = posX;
        this.posY = posY;
        this.angle = angle;
        this.type = type;
    }

    // GETTERS
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

    // SETTERS
    public void setPosX(float posX) {
        this.posX = posX;
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public void setType(EnumGameObject type) {
        this.type = type;
    }

    public void setObstacleId(String obstacleId) {
        this.obstacleId = obstacleId;
    }
}
