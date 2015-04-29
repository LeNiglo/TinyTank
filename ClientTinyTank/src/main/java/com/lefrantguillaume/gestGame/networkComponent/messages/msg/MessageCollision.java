package com.lefrantguillaume.gestGame.networkComponent.messages.msg;

import com.lefrantguillaume.gestGame.networkComponent.messages.MessageModel;

/**
 * Created by andres_k on 31/03/2015.
 */
public class MessageCollision extends MessageModel {
    private String shotId;
    private String targetId;

    public MessageCollision(String pseudo, String idPlayer, String shotId, String targetId) {
        this.pseudo = pseudo;
        this.id = idPlayer;
        this.shotId = shotId;
        this.targetId = targetId;
        this.playerAction = false;
    }
}
