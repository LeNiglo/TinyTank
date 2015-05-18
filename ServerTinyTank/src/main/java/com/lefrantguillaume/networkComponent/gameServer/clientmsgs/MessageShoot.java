package com.lefrantguillaume.networkComponent.gameServer.clientmsgs;

/**
 * Created by Styve on 25/03/2015.
 */

public class MessageShoot extends MessageModel {
    private String shotId;
    private float angle;

    public MessageShoot() {}

    public String getShotId() { return shotId; }
    public float getAngle() {return angle;}
    public void setShootId(String shotId) { this.shotId = shotId; }
    public void setAngle(float angle) {this.angle = angle;}
}