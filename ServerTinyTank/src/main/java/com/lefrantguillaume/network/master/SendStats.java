package com.lefrantguillaume.network.master;

import com.lefrantguillaume.gameComponent.gameobjects.player.Player;

import java.util.List;

/**
 * Created by Styve on 30/03/2015.
 */

class SendStatsRcv {
    private String name;
    private boolean res;
    private String err;

    SendStatsRcv() {}

    public String getName() { return name; }
    public boolean getRes() { return res; }
    public String getErr() { return err; }

    public void setName(String name) { this.name = name;}
    public void setRes(boolean res) { this.res = res; }
    public void setErr(String err) { this.err = err; }

    @Override
    public String toString() {
        return ("Name: " + this.name + " / Res: " + this.res + " / Err: " + this.err);
    }
}

class SendStatsSnd {
    private String serverId;
    private List<Player> players;

    SendStatsSnd() {}
    SendStatsSnd(String serverId, List<Player> players) {
        this.serverId = serverId;
        this.players = players;
    }

    public String getServerId() {return serverId;}

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }
}

