package com.lefrantguillaume.networkComponent.messages.msg;

import com.lefrantguillaume.networkComponent.messages.MessageModel;

/**
 * Created by andres_k on 12/03/2015.
 */
public class MessageShoot extends MessageModel {
    private int valueKeyPressed;

    public MessageShoot() {
    }

    public MessageShoot(String pseudo, int id, int valueKeyPressed) {
        this.pseudo = pseudo;
        this.id = id;
        this.valueKeyPressed = valueKeyPressed;
    }

    public int getValueKeyPressed() {
        return valueKeyPressed;
    }

}
