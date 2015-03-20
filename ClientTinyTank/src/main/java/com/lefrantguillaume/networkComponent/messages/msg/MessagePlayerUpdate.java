package com.lefrantguillaume.networkComponent.messages.msg;

import com.lefrantguillaume.gameComponent.playerData.data.Player;
import com.lefrantguillaume.networkComponent.messages.MessageModel;

/**
 * Created by andres_k on 12/03/2015.
 */
public class MessagePlayerUpdate extends MessageModel {
    private Player player;

    public MessagePlayerUpdate() {
    }
    public MessagePlayerUpdate(String pseudo, String id, Player player) {
        this.pseudo = pseudo;
        this.id = id;
        this.playerAction = false;
        this.player = player;
    }

    public Player getPlayer() {
        return this.player;
    }
}
