package com.lefrantguillaume.networkComponent.gameServerComponent.clientmsgs;

/**
 * Created by andres_k on 27/06/2015.
 */
public class MessageRoundKill extends MessageModel {
    private String killer;
    private String target;
    private boolean ally;

    public MessageRoundKill(){
    }

    public MessageRoundKill(String pseudo, String id, String killer, String target, boolean ally){
        this.pseudo = pseudo;
        this.id = id;
        this.killer = killer;
        this.target = target;
        this.ally = ally;
    }
}
