package com.lefrantguillaume.gameComponent.target;

import com.lefrantguillaume.WindowController;
import com.lefrantguillaume.gameComponent.EnumCollision;
import com.lefrantguillaume.gameComponent.EnumGameObject;
import com.lefrantguillaume.gameComponent.gameMode.EnumAction;
import com.lefrantguillaume.gameComponent.gameMode.GameModeController;
import com.lefrantguillaume.gameComponent.gameobjects.obstacles.Obstacle;
import com.lefrantguillaume.gameComponent.gameobjects.player.Player;
import com.lefrantguillaume.gameComponent.gameobjects.shots.Shot;
import com.lefrantguillaume.gameComponent.maps.MapController;
import com.lefrantguillaume.networkComponent.gameServerComponent.clientmsgs.*;
import com.lefrantguillaume.utils.ServerConfig;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public void initGame(MapController mapController, List<Obstacle> moreObstacles) {
        this.shots.clear();
        this.obstacles.clear();
        List<Obstacle> mapObstacles = mapController.getCurrentMap().getMapObstacles();
        if (mapObstacles != null) {
            this.addObstacles(mapObstacles);
        }
        if (moreObstacles != null) {
            this.addObstacles(moreObstacles);
        }
        for (Map.Entry<String, Player> entry : this.players.entrySet()){
            entry.getValue().init();
        }
    }

    public void clearAll() {
        this.obstacles.clear();
        this.players.clear();
        this.shots.clear();
    }

    public boolean isIgnored(List<EnumGameObject> items, EnumGameObject type) {
        return items.contains(type);
    }

    public List<MessageModel> doCollision(String hitterId, String targetId, EnumCollision type, GameModeController gameModeController) {
        List<MessageModel> messages = new ArrayList<>();

        if (this.getShot(hitterId) != null && type == EnumCollision.IN) {
            Shot hitterShot = this.getShot(hitterId);
            float damage = hitterShot.getCurrentDamageShot();

            if (this.getPlayer(targetId) != null) {
                Player player = this.getPlayer(targetId);
                Player killer = this.getPlayer(hitterShot.getPlayerId());
                if (this.isIgnored(player.getIgnoredObjectList(), hitterShot.getType()) == false) {
                    hitterShot.getDamageByCollision(player.getTank().getTankState().getCurrentLife());
                    if (!(ServerConfig.friendlyFire == false && player.getTeamId().equals(killer.getTeamId()))) {
                        messages.add(player.getTank().getTankState().getHit(player, damage));

                        if (player.getTank().getTankState().getCurrentLife() == 0) {
                            messages.addAll(this.doAKill(killer, player, gameModeController));
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
            Player player = this.getPlayer(hitterId);

            if (this.getObstacle(targetId) != null) {
                Obstacle obstacle = this.getObstacle(targetId);

                if (obstacle.getType().equals(EnumGameObject.MINE)) {
                    Player killer = this.getPlayer(obstacle.getPlayerId());
                    if (killer != null) {
                        if (!(ServerConfig.friendlyFire == false && player.getTeamId().equals(killer.getTeamId()))) {
                            messages.add(player.getTank().getTankState().getHit(player, obstacle.getDamage()));
                            if (player.getTank().getTankState().getCurrentLife() == 0) {
                                messages.addAll(this.doAKill(killer, player, gameModeController));
                            }
                            messages.add(this.deleteObstacle(targetId));
                        }
                    } else {
                        messages.add(this.deleteObstacle(targetId));
                    }
                } else if (obstacle.getType().equals(EnumGameObject.BOMB_AREA)) {
                    WindowController.addConsoleMsg("PLAYER VS BOMB");
                    Object result = gameModeController.doTask(new Pair<>(EnumAction.getEnumByOther(type), new Pair<>(player, obstacle)), this);
                    if (result instanceof List){
                        messages.addAll((List<MessageModel>)result);
                    }
                } else if (obstacle.getType().equals(EnumGameObject.OBJECTIVE_AREA)) {
                    WindowController.addConsoleMsg("PLAYER VS OBJECTIVE");
                    Object result = gameModeController.doTask(new Pair<>(EnumAction.getEnumByOther(type), new Pair<>(player, obstacle)), this);
                    if (result instanceof List){
                        messages.addAll((List<MessageModel>)result);
                    }
                }
            }
        }
        return messages;
    }

    public void addPlayer(Player player) {
        this.players.put(player.getId(), player);
    }

    public void addShot(Shot shot) {
        this.shots.put(shot.getId(), shot);
    }

    public MessagePutObstacle addObstacle(Obstacle obstacle) {
        this.obstacles.put(obstacle.getId(), obstacle);
        return new MessagePutObstacle(obstacle.getPlayerId(), obstacle.getPlayerPseudo(), obstacle.getId(), obstacle.getType(), obstacle.getX(), obstacle.getY(), obstacle.getAngle());
    }

    public void addObstacles(List<Obstacle> obstacles) {
        for (Obstacle obstacle : obstacles) {
            this.obstacles.put(obstacle.getId(), obstacle);
        }
    }

    public void deletePlayer(String playerId) {
        this.players.remove(playerId);
    }

    public MessageShotUpdateState deleteShot(String shotId) {
        Player player = this.getPlayer(this.getShot(shotId).getPlayerId());
        if (player != null) {
            MessageShotUpdateState message = new MessageShotUpdateState(player.getPseudo(), player.getId(), shotId, 0);
            this.shots.remove(shotId);
            return message;
        }
        return null;
    }

    public MessageObstacleUpdateState deleteObstacle(String obstacleId) {
        Obstacle obstacle = this.getObstacle(obstacleId);
        MessageObstacleUpdateState message = new MessageObstacleUpdateState(obstacle.getPlayerPseudo(), obstacle.getPlayerId(), obstacleId, 0);
        this.obstacles.remove(obstacleId);
        return message;
    }

    private List<MessageModel> doAKill(Player killer, Player target, GameModeController gameModeController){
        List<MessageModel> messages = new ArrayList<>();
        Object result = gameModeController.doTask(new Pair<>(EnumAction.KILL, killer), target);

        if (result instanceof List){
            messages.addAll((List<MessageModel>)result);
        }
        if (target.isTransportObjective()){
            messages.add(this.addObstacle(target.getTransportObjective()));
            target.setTransportObjective(null);
        }
        messages.add(new MessageRoundKill("admin", "admin", killer.getPseudo(), target.getPseudo(), killer.getTeamId(), target.getTeamId()));
        return messages;
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
