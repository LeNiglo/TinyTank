package com.lefrantguillaume.networkComponent.gameServerComponent.clientmsgs;


import com.lefrantguillaume.gameComponent.EnumGameObject;

/**
 * Created by Styve on 25/03/2015.
 */

public class MessagePlayerNew extends MessageModel {
    private EnumGameObject enumTanks;
    private float posX;
    private float posY;
    private String teamId;

    public MessagePlayerNew() {
    }

    public MessagePlayerNew(MessagePlayerNew player) {
        this.enumTanks = player.enumTanks;
        this.posX = player.posX;
        this.posY = player.posY;
        this.id = player.id;
        this.pseudo = player.pseudo;
        this.teamId = player.teamId;
        this.playerAction = player.playerAction;
    }

    // GETTERS

    public EnumGameObject getEnumTanks() {
        return enumTanks;
    }

    public float getPosX() {
        return this.posX;
    }

    public float getPosY() {
        return this.posY;
    }

    public String getTeamId(){
        return this.teamId;
    }

    // SETTERS

    public void setEnumTanks(EnumGameObject enumTanks) {
        this.enumTanks = enumTanks;
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }

    public void setTeamId(String teamId){
        this.teamId = teamId;
    }
}