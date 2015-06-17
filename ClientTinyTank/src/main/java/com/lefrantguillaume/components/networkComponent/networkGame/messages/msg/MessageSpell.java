package com.lefrantguillaume.components.networkComponent.networkGame.messages.msg;

import com.lefrantguillaume.components.networkComponent.networkGame.messages.MessageModel;

/**
 * Created by andres_k on 19/03/2015.
 */
public class MessageSpell extends MessageModel {
    private String idSpell;
    private float angle;
    private float x;
    private float y;

    public MessageSpell() {
    }

    public MessageSpell(String pseudo, String id, float angle, float x, float y) {
        this.pseudo = pseudo;
        this.id = id;
        this.playerAction = true;
        this.angle = angle;
        this.x = x;
        this.y = y;
    }

    // GETTERS
    public float getAngle() {
        return this.angle;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public String getIdSpell(){
        return this.idSpell;
    }
}
