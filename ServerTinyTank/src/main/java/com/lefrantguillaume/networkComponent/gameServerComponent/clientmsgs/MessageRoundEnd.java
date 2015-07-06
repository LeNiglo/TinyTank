package com.lefrantguillaume.networkComponent.gameServerComponent.clientmsgs;

/**
 * Created by andres_k on 27/06/2015.
 */
public class MessageRoundEnd extends MessageModel {
    private String winnerTeam;

    public MessageRoundEnd(){
    }

    public MessageRoundEnd(String pseudo, String id, String winnerTeam){
        this.pseudo = pseudo;
        this.id = id;
        this.winnerTeam = winnerTeam;
    }
}
