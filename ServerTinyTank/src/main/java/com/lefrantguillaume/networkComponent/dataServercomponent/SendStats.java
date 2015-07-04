package com.lefrantguillaume.networkComponent.dataServerComponent;

import com.lefrantguillaume.gameComponent.gameobjects.player.Player;
import com.lefrantguillaume.gameComponent.gameobjects.player.PlayerStats;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Styve on 30/03/2015.
 */

class SendStatsRcv {
    private String name;
    private boolean res;
    private String err;

    SendStatsRcv() {
    }

    public String getName() {
        return name;
    }

    public boolean getRes() {
        return res;
    }

    public String getErr() {
        return err;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRes(boolean res) {
        this.res = res;
    }

    public void setErr(String err) {
        this.err = err;
    }

    @Override
    public String toString() {
        return ("Name: " + this.name + " / Res: " + this.res + " / Err: " + this.err);
    }
}

class SendStatsSnd {
    private String serverId;
    private String gameName;
    private List<PlayerStats> players;

    public SendStatsSnd(String serverId, String gameName, HashMap<String, Player> players) {
        this.serverId = serverId;
        this.gameName = gameName;
        this.players = new ArrayList<>();
        for (Object o : players.entrySet()) {
            Map.Entry entry = (Map.Entry) o;
            this.players.add(((Player) entry.getValue()).getStats());
        }
    }

    public String getServerId() {
        return serverId;
    }

    public List<PlayerStats> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerStats> players) {
        this.players = players;
    }
}

