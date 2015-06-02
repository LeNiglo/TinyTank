package com.lefrantguillaume.components.networkComponent.networkGame.messages.msg;

import com.lefrantguillaume.components.gameComponent.gameObject.EnumGameObject;
import com.lefrantguillaume.components.networkComponent.networkGame.messages.MessageModel;

/**
 * Created by andres_k on 12/03/2015.
 */
public class MessagePlayerNew extends MessageModel {
    private EnumGameObject enumGameObject;
    private float posX;
    private float posY;

    public MessagePlayerNew() {
    }
    public MessagePlayerNew(String pseudo, String id, EnumGameObject enumGameObject) {
        this.enumGameObject = enumGameObject;
        this.pseudo = pseudo;
        this.id = id;
        this.playerAction = false;
    }

    public EnumGameObject getEnumGameObject() {
        return enumGameObject;
    }

    public float getPosX() {
        return this.posX;
    }

    public float getPosY() {
        return this.posY;
    }
}
