package com.lefrantguillaume.networkComponent.gameServer.clientmsgs;

/**
 * Created by Styve on 25/03/2015.
 */

public class MessagePlayerDelete extends MessageModel {
    public MessagePlayerDelete() {}
    public MessagePlayerDelete(String id, String pseudo) { this.id = id; this.pseudo = pseudo; }
}