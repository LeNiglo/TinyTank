package com.lefrantguillaume.networkComponent.gameServerComponent.clientmsgs;

/**
 * Created by andres_k on 28/04/2015.
 */
public class MessagePlayerRevive extends MessageModel {
    private float posX;
    private float posY;

    public MessagePlayerRevive(){

    }

    public MessagePlayerRevive(String pseudo, String id, float posX, float posY){
        this.pseudo = pseudo;
        this.id = id;
        this.posX = posX;
        this.posY = posY;
    }
}
