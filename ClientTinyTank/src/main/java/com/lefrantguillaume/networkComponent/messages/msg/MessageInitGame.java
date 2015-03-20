package com.lefrantguillaume.networkComponent.messages.msg;

import com.lefrantguillaume.gameComponent.gameObject.projectiles.Shot;
import com.lefrantguillaume.gameComponent.playerData.data.Player;
import com.lefrantguillaume.networkComponent.messages.MessageModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andres_k on 12/03/2015.
 */
public class MessageInitGame extends MessageModel {
    private List<Player> players;
    private List<Shot> shots;

    public MessageInitGame() {
    }

    public MessageInitGame(String pseudo, String id) {
        this.pseudo = pseudo;
        this.id = id;
        this.playerAction = false;
        this.players = new ArrayList<Player>();
        this.shots = new ArrayList<Shot>();
    }

    //GETTERS
    public List<Player> getPlayers() {
        return this.players;
    }

    public List<Shot> getShots() {
        return this.shots;
    }

    // SETTERS
    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public void setShots(List<Shot> shots) {
        this.shots = shots;
    }
}