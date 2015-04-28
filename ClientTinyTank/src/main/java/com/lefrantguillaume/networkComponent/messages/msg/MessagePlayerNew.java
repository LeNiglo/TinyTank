package com.lefrantguillaume.networkComponent.messages.msg;

import com.lefrantguillaume.gameComponent.gameObject.tanks.types.EnumTanks;
import com.lefrantguillaume.networkComponent.messages.MessageModel;

/**
 * Created by andres_k on 12/03/2015.
 */
public class MessagePlayerNew extends MessageModel {
    private EnumTanks enumTanks;
    private float posX;
    private float posY;

    public MessagePlayerNew() {
    }
    public MessagePlayerNew(String pseudo, String id, EnumTanks enumTanks) {
        this.enumTanks = enumTanks;
        this.pseudo = pseudo;
        this.id = id;
        this.playerAction = false;
    }

    public EnumTanks getEnumTanks() {
        return enumTanks;
    }

    public float getPosX() {
        return this.posX;
    }

    public float getPosY() {
        return this.posY;
    }
}
