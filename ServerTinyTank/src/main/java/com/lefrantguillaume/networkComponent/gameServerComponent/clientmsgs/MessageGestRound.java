package com.lefrantguillaume.networkComponent.gameServerComponent.clientmsgs;

/**
 * Created by leniglo on 30/05/15.
 */
public class MessageGestRound extends MessageModel {
    private Integer action;
    private Object data;

    public MessageGestRound() {
    }

    public MessageGestRound(String pseudo, String id, Integer action) {
        this.pseudo = pseudo;
        this.id = id;
        this.action = action;
        this.data = null;
    }
    public MessageGestRound(String pseudo, String id, Integer action, Object data) {
        this.pseudo = pseudo;
        this.id = id;
        this.action = action;
        this.data = data;
    }

    public Integer getAction() {
        return this.action;
    }

    public Object getData() {
        return this.data;
    }

}
