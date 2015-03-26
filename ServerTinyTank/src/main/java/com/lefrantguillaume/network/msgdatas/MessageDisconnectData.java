package com.lefrantguillaume.network.msgdatas;

import com.esotericsoftware.kryonet.Connection;
import com.lefrantguillaume.game.Player;

/**
 * Created by Styve on 25/03/2015.
 */
public class MessageDisconnectData {
    private Connection connection;
    private String pseudo;
    private String playerId;

    public MessageDisconnectData(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }
}
