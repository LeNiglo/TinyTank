package com.lefrantguillaume.networkComponent.gameServerComponent.clientmsgs;

/**
 * Created by Styve on 25/03/2015.
 */

public class MessageNeedMap extends MessageModel {
    private boolean value;

    public MessageNeedMap() {}
    public MessageNeedMap(boolean value) { this.value = value; }

    public boolean isValue() { return value; }
    public void setValue(boolean value) { this.value = value; }
}