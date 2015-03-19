package com.lefrantguillaume.networkComponent.messages.msg;

import com.lefrantguillaume.networkComponent.messages.MessageModel;

/**
 * Created by andres_k on 12/03/2015.
 */
public class MessageChangeTeam extends MessageModel {
    private int idTeam;

    public MessageChangeTeam() {
    }
    public MessageChangeTeam(String pseudo, int id, int idTeam) {
        this.pseudo = pseudo;
        this.id = id;
        this.idTeam = idTeam;
    }

    public int getIdTeam() {
        return idTeam;
    }
}
