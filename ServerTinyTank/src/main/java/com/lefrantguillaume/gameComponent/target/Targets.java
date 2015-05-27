package com.lefrantguillaume.gameComponent.target;

import com.lefrantguillaume.WindowController;
import com.lefrantguillaume.gameComponent.gameobjects.player.Player;
import com.lefrantguillaume.gameComponent.gameobjects.shots.Shot;
import com.lefrantguillaume.networkComponent.gameServerComponent.clientmsgs.MessageModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by leniglo on 23/04/15.
 */
public class Targets {

    private HashMap<String, Player> players;
    private HashMap<String, Shot> shots;

    public Targets() {
        this.players = new HashMap<>();
        this.shots = new HashMap<>();
    }

    public MessageModel doCollision(String shotId, String targetId) {
        MessageModel msg = null;

        WindowController.addConsoleMsg("shots:" + this.shots.size() + "SHOT_ID:" + shotId);
        if (this.getShot(shotId) != null) {
            WindowController.addConsoleMsg("players:" + this.players.size() + "PLAYER_ID: " + targetId);
            if (this.getPlayer(targetId) != null) {
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

    public void addPlayer(String id, Player p) {
        this.players.put(id, p);
    }

    public void addShot(String id, Shot s) {
        this.shots.put(id, s);
    }

    public void deletePlayer(String playerId) {
        this.players.remove(playerId);
    }

    public void deleteShot(String shotId) {
        this.shots.remove(shotId);
    }

    public void clear() {
        this.players.clear();
        this.shots.clear();
    }

    // GETTERS
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

    public List<String> getPlayersName() {
        List<String> playersName = new ArrayList<>();

        for (int i = 0; i < this.players.size(); ++i) {
            playersName.add(this.players.get(i).getPseudo());
        }
        return playersName;
    }
}
