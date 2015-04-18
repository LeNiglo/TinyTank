package com.lefrantguillaume.network.clientmsgs;

import com.lefrantguillaume.game.EnumTanks;

/**
 * Created by Styve on 25/03/2015.
 */

public class MessagePlayerNew extends MessageModel {
    private EnumTanks enumTanks;

    public MessagePlayerNew() {}

    public EnumTanks getEnumTanks() {return enumTanks;}
    public void setEnumTanks(EnumTanks enumTanks) {this.enumTanks = enumTanks;}
}