package com.lefrantguillaume.components.networkComponent.networkGame.messages.msg;

import com.lefrantguillaume.components.gameComponent.gameObject.EnumGameObject;
import com.lefrantguillaume.components.networkComponent.networkGame.messages.MessageModel;

/**
 * Created by andres_k on 19/03/2015.
 */
public class MessageSpell extends MessageModel {
    private String spellId;
    private EnumGameObject type;
    private float angle;
    private float posX;
    private float posY;

    public MessageSpell() {
    }

    public MessageSpell(String pseudo, String id, EnumGameObject type, float angle, float posX, float posY) {
        this.pseudo = pseudo;
        this.id = id;
        this.playerAction = true;
        this.angle = angle;
        this.posX = posX;
        this.posY = posY;
    }

    // GETTERS
    public float getAngle() {
        return this.angle;
    }

    public float getPosX() {
        return this.posX;
    }

    public float getPosY() {
        return this.posY;
    }

    public String getSpellId(){
        return this.spellId;
    }

    public EnumGameObject getType(){
        return this.type;
    }
}
