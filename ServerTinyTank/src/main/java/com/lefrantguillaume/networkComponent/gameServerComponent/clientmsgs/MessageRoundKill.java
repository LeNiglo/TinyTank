package com.lefrantguillaume.networkComponent.gameServerComponent.clientmsgs;

/**
 * Created by andres_k on 27/06/2015.
 */
public class MessageRoundKill extends MessageModel {
    private String killer;
    private String target;
    private String killerTeam;
    private String targetTeam;

    public MessageRoundKill(){
    }

    public MessageRoundKill(String pseudo, String id, String killer, String target, String killerTeam, String targetTeam){
        this.pseudo = pseudo;
        this.id = id;
        this.killer = killer;
        this.target = target;
        this.killerTeam = killerTeam;
        this.targetTeam = targetTeam;
    }
}
