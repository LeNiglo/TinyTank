package com.lefrantguillaume.networkComponent.gameServerComponent.clientmsgs;

/**
 * Created by Styve on 25/03/2015.
 */

public class MessagePlayerDelete extends MessageModel {
    public MessagePlayerDelete() {}
    public MessagePlayerDelete(String pseudo, String id) { this.id = id; this.pseudo = pseudo; }
}