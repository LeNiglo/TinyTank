package com.lefrantguillaume.networkComponent.gameServerComponent.clientmsgs;

/**
 * Created by Styve on 25/03/2015.
 */

public class MessageDisconnect extends MessageModel {
    private boolean success;

    public MessageDisconnect() {}

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
}