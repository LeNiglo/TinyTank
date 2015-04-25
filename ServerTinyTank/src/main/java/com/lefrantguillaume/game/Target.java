package com.lefrantguillaume.game;

import com.lefrantguillaume.game.gameobjects.player.Player;
import com.lefrantguillaume.game.gameobjects.shots.Shot;
import com.lefrantguillaume.network.clientmsgs.MessageModel;

import java.util.*;
import java.util.Map;

/**
 * Created by leniglo on 23/04/15.
 */
public class Target {

    private HashMap<String, Player> players = null;
    private HashMap<String, Shot> shots = null;

    public Target() {
        this.players = new HashMap<String, Player>();
        this.shots = new HashMap<String, Shot>();
    }

    public MessageModel doCollision(String shotId, String targetId){
        MessageModel msg = null;

        if (this.getShot(shotId) != null) {
            if (this.getPlayer(targetId) != null){
                msg = this.getPlayer(targetId).getTank().getTankState().getHit(this.getPlayer(targetId).getPseudo(), targetId, this.getShot(shotId));
            }
            /* pour box ext
            else if (this.getBox(targetId) != null){
                this.getBox(targetId).getHit(this.getShot(shotId));
            }
            */
        }
        return msg;
    }

    public void addPlayer(String k, Player p) {
        this.players.put(k, p);
    }

    public void addShot(String k, Shot s) {
        this.shots.put(k, s);
    }

    public void deletePlayer(String playerId) {
        this.players.remove(playerId);
    }

    public void deleteShot(String shotId) {
        this.shots.remove(shotId);
    }

    public Player getPlayer(String playerId) {
        return this.players.get(playerId);
    }

    public Shot getShot(String shotId) {
        return this.shots.get(shotId);
    }

    public HashMap<String, Player> getPlayers() {
        return this.players;
    }

    public HashMap<String, Shot> getShots() {
        return this.shots;
    }

}
