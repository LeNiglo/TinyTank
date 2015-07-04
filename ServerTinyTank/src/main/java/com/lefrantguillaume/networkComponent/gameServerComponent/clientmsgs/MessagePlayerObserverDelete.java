package com.lefrantguillaume.networkComponent.gameServerComponent.clientmsgs;

/**
 * Created by Styve on 25/03/2015.
 */

public class MessagePlayerObserverDelete extends MessageModel {
    public MessagePlayerObserverDelete() {}
    public MessagePlayerObserverDelete(String pseudo, String id) { this.id = id; this.pseudo = pseudo; }
}