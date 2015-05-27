package com.lefrantguillaume.networkComponent.gameServerComponent.clientmsgs;

import com.lefrantguillaume.gameComponent.gameobjects.obstacle.EnumObstacles;

/**
 * Created by Styve on 26/03/2015.
 */
public class MessagePutObstacle extends MessageModel {
    private float posX;
    private float posY;
    private float angle;
    private EnumObstacles type;
    private String obstacleId = "";

    public MessagePutObstacle() {}

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

    public EnumObstacles getType() {
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

    public void setType(EnumObstacles type) {
        this.type = type;
    }

    public void setObstacleId(String obstacleId) {
        this.obstacleId = obstacleId;
    }
}
