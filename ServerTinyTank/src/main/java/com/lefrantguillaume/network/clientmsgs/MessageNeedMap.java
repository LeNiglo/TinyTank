package com.lefrantguillaume.network.clientmsgs;

/**
 * Created by Styve on 25/03/2015.
 */

public class MessageNeedMap {
    private boolean value;

    public MessageNeedMap() {}
    public MessageNeedMap(boolean value) { this.value = value; }

    public boolean isValue() { return value; }
    public void setValue(boolean value) { this.value = value; }
}