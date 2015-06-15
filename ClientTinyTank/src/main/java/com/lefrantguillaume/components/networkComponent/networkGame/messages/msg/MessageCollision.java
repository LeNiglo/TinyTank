package com.lefrantguillaume.components.networkComponent.networkGame.messages.msg;

import com.lefrantguillaume.components.collisionComponent.EnumCollision;
import com.lefrantguillaume.components.networkComponent.networkGame.messages.MessageModel;

/**
 * Created by andres_k on 31/03/2015.
 */
public class MessageCollision extends MessageModel {
    private String hitterId;
    private String targetId;
    private EnumCollision type;

    public MessageCollision(String pseudo, String idPlayer, String hitterId, String targetId, EnumCollision type) {
        this.pseudo = pseudo;
        this.id = idPlayer;
        this.hitterId = hitterId;
        this.targetId = targetId;
        this.type = type;
        this.playerAction = false;
    }
}
