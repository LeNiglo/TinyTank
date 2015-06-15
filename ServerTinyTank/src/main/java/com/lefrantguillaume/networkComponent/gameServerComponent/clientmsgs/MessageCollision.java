package com.lefrantguillaume.networkComponent.gameServerComponent.clientmsgs;

import com.lefrantguillaume.gameComponent.EnumCollision;

/**
 * Created by andres_k on 15/06/2015.
 */
public class MessageCollision extends MessageModel {
    private String hitterId;
    private String targetId;
    private EnumCollision type;

    public MessageCollision() {
    }

    public String getHitterId() {
        return this.hitterId;
    }

    public String getTargetId() {
        return this.targetId;
    }

    public EnumCollision getType() {
        return this.type;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public void setHitterId(String hitterId) {
        this.hitterId = hitterId;
    }

    public void setType(EnumCollision type){
        this.type = type;
    }
}
