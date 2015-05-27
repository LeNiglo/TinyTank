package com.lefrantguillaume.networkComponent.gameServerComponent.clientmsgs;

/**
 * Created by Styve on 25/03/2015.
 */
public class MessageCollision extends MessageModel {
    private String targetId;
    private String shotId;

    public MessageCollision() {}

    public String getTargetId() {return targetId;}
    public String getShotId() {return shotId;}

    public void setTargetId(String targetId) {this.targetId = targetId;}
    public void setShotId(String shotId) {this.shotId = shotId;}
}
