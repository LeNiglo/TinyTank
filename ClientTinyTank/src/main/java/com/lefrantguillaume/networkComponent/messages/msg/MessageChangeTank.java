package com.lefrantguillaume.networkComponent.messages.msg;

import com.lefrantguillaume.gameComponent.gameObject.tanks.EnumTanks;
import com.lefrantguillaume.networkComponent.messages.MessageModel;

/**
 * Created by andres_k on 12/03/2015.
 */
public class MessageChangeTank extends MessageModel {
    private EnumTanks tank;

    public MessageChangeTank() {
    }
    public MessageChangeTank(String pseudo, int id, EnumTanks tank) {
        this.pseudo = pseudo;
        this.id = id;
        this.tank = tank;
    }

    public EnumTanks getTank() {
        return tank;
    }
}
