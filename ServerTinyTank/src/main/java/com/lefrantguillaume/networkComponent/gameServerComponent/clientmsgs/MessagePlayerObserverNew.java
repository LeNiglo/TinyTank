package com.lefrantguillaume.networkComponent.gameServerComponent.clientmsgs;

/**
 * Created by andres_k on 12/03/2015.
 */
public class MessagePlayerObserverNew extends MessageModel {
    private String teamId;

    public MessagePlayerObserverNew() {
    }

    public MessagePlayerObserverNew(String pseudo, String id, String teamId) {
        this.pseudo = pseudo;
        this.id = id;
        this.teamId = teamId;
        this.playerAction = false;
    }

    public String getTeamId(){
        return this.teamId;
    }
}
