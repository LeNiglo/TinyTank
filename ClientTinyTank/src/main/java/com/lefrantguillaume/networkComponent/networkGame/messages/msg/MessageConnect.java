package com.lefrantguillaume.networkComponent.networkGame.messages.msg;

import com.lefrantguillaume.networkComponent.networkGame.messages.MessageModel;

/**
 * Created by andres_k on 12/03/2015.
 */
public class MessageConnect extends MessageModel {
    private String mapName;
    private String encodedMap;

    public MessageConnect() {
    }

    public MessageConnect(String pseudo, String id) {
        this.pseudo = pseudo;
        this.id = id;
        this.playerAction = false;
    }

    // GETTERS
    public String getMapName() {
        return mapName;
    }
    public String getEncodedMap() {
        return encodedMap;
    }

    //SETTERS
    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public void setEncodedMap(String encodedMap) {
        this.encodedMap = encodedMap;
    }
}