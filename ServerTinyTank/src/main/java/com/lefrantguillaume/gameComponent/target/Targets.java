package com.lefrantguillaume.gameComponent.target;


import com.esotericsoftware.kryonet.Connection;
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
    private List<Pair<MessagePlayerNew, Connection>> waitingPeople;
    private List<Pair<MessagePlayerObserverNew, Connection>> observerPeople;

    public Targets() {
        this.players = new HashMap<>();
        this.shots = new HashMap<>();
        this.obstacles = new HashMap<>();
        this.waitingPeople = new ArrayList<>();
        this.observerPeople = new ArrayList<>();
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
        for (Map.Entry<String, Player> entry : this.players.entrySet()) {
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

        WindowController.addConsoleMsg("DoCollision->\n shot?" + (this.getShot(hitterId) != null) + "\nplayer?" + (this.getPlayer(hitterId) != null));
        if (this.getShot(hitterId) != null && type == EnumCollision.IN) {
            messages.addAll(this.collisionInWithShotHitter(this.getShot(hitterId), targetId, gameModeController));
        } else if (this.getPlayer(hitterId) != null) {
            messages.addAll(this.collisionWithPlayerHitter(this.getPlayer(hitterId), targetId, type, gameModeController));
        }
        return messages;
    }

    private List<MessageModel> collisionInWithShotHitter(Shot hitterShot, String targetId, GameModeController gameModeController) {
        List<MessageModel> messages = new ArrayList<>();
        float damage = hitterShot.getCurrentDamageShot();

        WindowController.addConsoleMsg("\nSHOT COLLISION WITH: ");
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

            WindowController.addConsoleMsg("Obstacle: " + obstacle);
            if (this.isIgnored(obstacle.getIgnoredObjectList(), hitterShot.getType()) == false) {
                WindowController.addConsoleMsg("a");
                if (obstacle.getType() == EnumGameObject.UNBREAKABLE) {
                    WindowController.addConsoleMsg("unbreak");
                    hitterShot.getDamageByCollision(hitterShot.getCurrentDamageShot());
                } else {
                    hitterShot.getDamageByCollision(obstacle.getCurrentLife());
                    messages.add(obstacle.getHit(damage));
                }
                WindowController.addConsoleMsg("obstacleLife: " + obstacle.getCurrentLife());
                if (obstacle.getCurrentLife() == 0) {
                    this.obstacles.remove(obstacle.getId());
                    Player player = this.getPlayer(hitterShot.getPlayerId());
                    if (player != null) {
                        player.addGameObjectDestroyed();
                    }
                }
                WindowController.addConsoleMsg("hitterLife: " + hitterShot.getCurrentDamageShot());
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
        return messages;
    }

    private List<MessageModel> collisionWithPlayerHitter(Player player, String targetId, EnumCollision type, GameModeController gameModeController) {
        List<MessageModel> messages = new ArrayList<>();

        if (this.getObstacle(targetId) != null) {
            Obstacle obstacle = this.getObstacle(targetId);

            if (obstacle.getType().equals(EnumGameObject.MINE) && type == EnumCollision.IN) {
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
                if (result instanceof List) {
                    messages.addAll((List<MessageModel>) result);
                }
            } else if (obstacle.getType().equals(EnumGameObject.OBJECTIVE_AREA)) {
                WindowController.addConsoleMsg("PLAYER VS OBJECTIVE");
                Object result = gameModeController.doTask(new Pair<>(EnumAction.getEnumByOther(type), new Pair<>(player, obstacle)), this);
                if (result instanceof List) {
                    messages.addAll((List<MessageModel>) result);
                }
            }
        }
        return messages;
    }

    private List<MessageModel> doAKill(Player killer, Player target, GameModeController gameModeController) {
        List<MessageModel> messages = new ArrayList<>();
        Object result = gameModeController.doTask(new Pair<>(EnumAction.KILL, killer), target);

        killer.addHitSomebody();
        if (result instanceof List) {
            messages.addAll((List<MessageModel>) result);
        }
        if (target.isTransportObjective()) {
            messages.add(this.addObstacle(target.getTransportObjective()));
            target.setTransportObjective(null);
        }
        messages.add(new MessageRoundKill("admin", "admin", killer.getPseudo(), target.getPseudo(), killer.getTeamId(), target.getTeamId()));
        return messages;
    }

    // ADD
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

    public void addWaitingPeople(Pair<MessagePlayerNew, Connection> people) {
        this.waitingPeople.add(people);
    }

    public void addObserverPeople(Pair<MessagePlayerObserverNew, Connection> people) {
        this.observerPeople.add(people);
    }

    // DELETE
    public MessagePlayerDelete deletePlayer(String playerId) {
        Player player = this.getPlayer(playerId);
        if (player != null) {
            MessagePlayerDelete message = new MessagePlayerDelete(player.getPseudo(), playerId);
            this.players.remove(playerId);
            return message;
        }
        return null;
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
        if (obstacle != null) {
            MessageObstacleUpdateState message = new MessageObstacleUpdateState(obstacle.getPlayerPseudo(), obstacle.getPlayerId(), obstacleId, 0);
            this.obstacles.remove(obstacleId);
            return message;
        }
        return null;
    }

    public MessagePlayerObserverDelete deleteWaitingPlayer(String id) {
        MessagePlayerObserverDelete message;
        for (int i = 0; i < this.waitingPeople.size(); ++i) {
            Pair<MessagePlayerNew, Connection> player = this.waitingPeople.get(i);
            if (player.getKey().getId().equals(id)) {
                message = new MessagePlayerObserverDelete(player.getKey().getPseudo(), player.getKey().getId());
                this.waitingPeople.remove(i);
                return message;
            }
        }
        return null;
    }

    public MessagePlayerObserverDelete deleteObserverPlayer(String id) {
        MessagePlayerObserverDelete message;
        for (int i = 0; i < this.observerPeople.size(); ++i) {
            Pair<MessagePlayerObserverNew, Connection> player = this.observerPeople.get(i);
            if (player.getKey().getId().equals(id)) {
                message = new MessagePlayerObserverDelete(player.getKey().getPseudo(), player.getKey().getId());
                this.waitingPeople.remove(i);
                return message;
            }
        }
        return null;
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

    public List<Pair<MessagePlayerNew, Connection>> getWaitingPeople() {
        return this.waitingPeople;
    }

    public List<Pair<MessagePlayerObserverNew, Connection>> getObserverPeople() {
        return this.observerPeople;
    }

    public Pair<MessagePlayerNew, Connection> getFirstWaitingPlayer() {
        if (this.waitingPeople.size() > 0) {
            Pair<MessagePlayerNew, Connection> player = this.waitingPeople.get(0);
            this.waitingPeople.remove(0);
            return player;
        }
        return null;
    }
}
