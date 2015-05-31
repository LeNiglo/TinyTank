package com.lefrantguillaume.components.networkComponent.networkGame.messages.msg;

import com.lefrantguillaume.components.networkComponent.networkGame.messages.MessageModel;

/**
 * Created by andres_k on 12/03/2015.
 */
public class MessageChangeTeam extends MessageModel {
    private int idTeam;

    public MessageChangeTeam() {
    }
    public MessageChangeTeam(String pseudo, String id, int idTeam) {
        this.pseudo = pseudo;
        this.id = id;
        this.playerAction = true;
        this.idTeam = idTeam;
    }

    public int getIdTeam() {
        return idTeam;
    }
}
