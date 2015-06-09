package com.lefrantguillaume.gameComponent.target;

import com.lefrantguillaume.gameComponent.EnumGameObject;
import com.lefrantguillaume.gameComponent.gameobjects.obstacles.Obstacle;
import com.lefrantguillaume.gameComponent.gameobjects.player.Player;
import com.lefrantguillaume.gameComponent.gameobjects.shots.Shot;
import com.lefrantguillaume.gameComponent.maps.MapController;
import com.lefrantguillaume.networkComponent.gameServerComponent.clientmsgs.MessageModel;
import com.lefrantguillaume.networkComponent.gameServerComponent.clientmsgs.MessageObstacleUpdateState;
import com.lefrantguillaume.networkComponent.gameServerComponent.clientmsgs.MessageShotUpdateState;
import com.lefrantguillaume.utils.ServerConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by leniglo on 23/04/15.
 */
public class Targets {

    private HashMap<String, Player> players;
    private HashMap<String, Shot> shots;
    private HashMap<String, Obstacle> obstacles;

    public Targets() {
        this.players = new HashMap<>();
        this.shots = new HashMap<>();
        this.obstacles = new HashMap<>();
    }

    // FUNCTIONS

    public void initGame(MapController mapController){
        this.shots.clear();
        this.obstacles.clear();
        List<Obstacle> mapObstacles = mapController.getCurrentMap().getMapObstacles();
        if (mapObstacles != null) {
            for (Obstacle obstacle : mapObstacles){
                this.obstacles.put(obstacle.getId(), new Obstacle(obstacle, true));
            }
        }
    }

    public void clearAll(){
        this.obstacles.clear();
        this.players.clear();
        this.shots.clear();
    }

    public boolean isIgnored(List<EnumGameObject> items, EnumGameObject type) {
        return items.contains(type);
    }

    public List<MessageModel> doCollision(String hitterId, String targetId, String saveTeamId) {
        List<MessageModel> messages = new ArrayList<>();

        if (this.getShot(hitterId) != null) {
            Shot hitterShot = this.getShot(hitterId);
            float damage = hitterShot.getCurrentDamageShot();

            if (this.getPlayer(targetId) != null) {
                Player player = this.getPlayer(targetId);
                Player killer = this.getPlayer(hitterShot.getPlayerId());
                if (this.isIgnored(player.getIgnoredObjectList(), hitterShot.getType()) == false) {
                    hitterShot.getDamageByCollision(player.getTank().getTankState().getCurrentLife());
                    if (!(ServerConfig.friendlyFire == false && player.getTeamId().equals(killer.getTeamId()))) {
                        messages.add(player.getTank().getTankState().getHit(player, damage));
                        if (player.getTank().getTankState().getCurrentLife() == 0){
                            killer.addKill();
                            saveTeamId = killer.getTeamId();
                            player.addDeath();
                        }
                    }
                    if (hitterShot.getCurrentDamageShot() == 0) {
                        messages.add(this.deleteShot(hitterShot.getId()));
                    }

                }
            }
            if (this.getObstacle(targetId) != null) {
                Obstacle obstacle = this.getObstacle(targetId);

                if (this.isIgnored(obstacle.getIgnoredObjectList(), hitterShot.getType()) == false) {
                    if (obstacle.getType() == EnumGameObject.UNBREAKABLE) {
                        hitterShot.getDamageByCollision(hitterShot.getCurrentDamageShot());
                    } else {
                        hitterShot.getDamageByCollision(obstacle.getCurrentLife());
                        messages.add(obstacle.getHit(damage));
                    }
                    if (obstacle.getCurrentLife() == 0) {
                        this.obstacles.remove(obstacle.getId());
                    }
                    if (hitterShot.getCurrentDamageShot() == 0) {
                        messages.add(this.deleteShot(hitterShot.getId()));
                    }
                }
            }
            if (this.getShot(targetId) != null) {
                Shot shot = this.getShot(targetId);
                Player player = this.getPlayer(shot.getPlayerId());

                if (this.isIgnored(shot.getIgnoredObjectList(), hitterShot.getType()) == false) {
                    hitterShot.getDamageByCollision(shot.getCurrentDamageShot());
                    messages.add(shot.getHit(player, damage));
                    if (shot.getCurrentDamageShot() == 0) {
                        this.shots.remove(shot.getId());
                    }
                    if (hitterShot.getCurrentDamageShot() == 0) {
                        messages.add(this.deleteShot(hitterShot.getId()));
                    }
                }
            }
        } else if (this.getPlayer(hitterId) != null) {
            if (this.getObstacle(targetId) != null){
                Obstacle obstacle = this.getObstacle(targetId);

                if (obstacle.getType().equals(EnumGameObject.MINE)){
                    Player player = this.getPlayer(hitterId);
                    Player killer = this.getPlayer(obstacle.getPlayerId());
                    if (!(ServerConfig.friendlyFire == false && player.getTeamId().equals(killer.getTeamId()))) {
                        messages.add(player.getTank().getTankState().getHit(player, obstacle.getDamage()));
                        if (player.getTank().getTankState().getCurrentLife() == 0){
                            killer.addKill();
                            saveTeamId = killer.getTeamId();
                            player.addDeath();
                        }
                        messages.add(this.deleteObstacle(targetId));
                    }
                }
            }
        }
        return messages;
    }

    public void addPlayer(String id, Player player) {
        this.players.put(id, player);
    }

    public void addShot(String id, Shot shot) {
        this.shots.put(id, shot);
    }

    public void addObstacle(String id, Obstacle obstacle) {
        this.obstacles.put(id, obstacle);
    }

    public void deletePlayer(String playerId) {
        this.players.remove(playerId);
    }

    public MessageShotUpdateState deleteShot(String shotId) {
        Player player = this.getPlayer(this.getShot(shotId).getPlayerId());
        MessageShotUpdateState message = new MessageShotUpdateState(player.getPseudo(), player.getId(), shotId, 0);
        this.shots.remove(shotId);
        return message;
    }

    public MessageObstacleUpdateState deleteObstacle(String obstacleId) {
        Obstacle obstacle = this.getObstacle(obstacleId);
        MessageObstacleUpdateState message = new MessageObstacleUpdateState(obstacle.getPlayerPseudo(), obstacle.getPlayerId(), obstacleId, 0);
        this.shots.remove(obstacleId);
        return message;
    }

    // GETTERS
    public Player getPlayer(String id) {
        return this.players.get(id);
    }

    public Shot getShot(String id) {
        return this.shots.get(id);
    }

    public Obstacle getObstacle(String id) {
        return this.obstacles.get(id);
    }

    public HashMap<String, Player> getPlayers() {
        return this.players;
    }

    public HashMap<String, Shot> getShots() {
        return this.shots;
    }

    public HashMap<String, Obstacle> getObstacles() {
        return this.obstacles;
    }

    public List<String> getPlayersName() {
        List<String> playersName = new ArrayList<>();

        for (int i = 0; i < this.players.size(); ++i) {
            playersName.add(this.players.get(i).getPseudo());
        }
        return playersName;
    }
}
