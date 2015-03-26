package com.lefrantguillaume.network.clientmsgs;

import com.lefrantguillaume.game.EnumObjects;

/**
 * Created by Styve on 25/03/2015.
 */
public class MessageCollision {
    private String playerId;
    private String shotId;
    private EnumObjects type;
    private float posX;
    private float posY;
    private long timestamp;

    public MessageCollision() {}

    public String getPlayerId() {return playerId;}
    public String getShotId() {return shotId;}
    public EnumObjects getType() {return type;}
    public float getPosX() {return posX;}
    public float getPosY() {return posY;}
    public long getTimestamp() {return timestamp;}

    public void setPlayerId(String playerId) {this.playerId = playerId;}
    public void setShotId(String shotId) {this.shotId = shotId;}
    public void setType(EnumObjects type) {this.type = type;}
    public void setPosX(float posX) {this.posX = posX;}
    public void setPosY(float posY) {this.posY = posY;}
    public void setTimestamp(long timestamp) {this.timestamp = timestamp;}
}
