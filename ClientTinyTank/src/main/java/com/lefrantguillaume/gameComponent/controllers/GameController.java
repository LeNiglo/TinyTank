package com.lefrantguillaume.gameComponent.controllers;

import com.lefrantguillaume.Utils.configs.CurrentUser;
import com.lefrantguillaume.Utils.configs.MasterConfig;
import com.lefrantguillaume.Utils.tools.Debug;
import com.lefrantguillaume.collisionComponent.CollisionController;
import com.lefrantguillaume.collisionComponent.CollisionObject;
import com.lefrantguillaume.gameComponent.RoundData.RoundController;
import com.lefrantguillaume.gameComponent.animations.AnimatorGameData;
import com.lefrantguillaume.gameComponent.gameObject.tanks.TankFactory;
import com.lefrantguillaume.gameComponent.playerData.data.Player;
import com.lefrantguillaume.gameComponent.playerData.action.PlayerAction;
import com.lefrantguillaume.gameComponent.gameObject.projectiles.Shot;
import com.lefrantguillaume.gameComponent.gameObject.EnumType;
import com.lefrantguillaume.gameComponent.RoundData.Team;
import com.lefrantguillaume.gameComponent.playerData.data.User;
import com.lefrantguillaume.networkComponent.messages.MessageModel;
import com.lefrantguillaume.networkComponent.messages.msg.MessagePlayerDelete;
import com.lefrantguillaume.networkComponent.messages.msg.MessagePlayerNew;
import com.lefrantguillaume.networkComponent.messages.msg.MessagePlayerUpdatePosition;
import com.lefrantguillaume.networkComponent.messages.msg.MessagePlayerUpdateState;
import org.newdawn.slick.SlickException;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by andres_k on 13/03/2015.
 */
public class GameController extends Observable implements Observer {
    private List<Player> players;
    private List<Shot> shots;
    private List<Team> teams;
    private CollisionController collisionController;
    private MapController mapController;
    private RoundController roundController;
    private AnimatorGameData animatorGameData;

    public GameController() throws SlickException {
        this.players = new ArrayList<Player>();
        this.shots = new ArrayList<Shot>();
        this.teams = new ArrayList<Team>();
        this.animatorGameData = null;
        this.roundController = new RoundController(this.players, this.teams);
        this.mapController = new MapController(this.collisionController, MasterConfig.getMapConfigFile());
        this.collisionController = new CollisionController(this.mapController);
    }

    public void clearData() {
        this.players.clear();
        this.shots.clear();
        this.teams.clear();
        this.collisionController.clearCollisionObjects();
        this.mapController.clearObstacles();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof MessageModel) {
            MessageModel received = (MessageModel) arg;
            if (received.getPlayerAction() == true) {
                Player current = this.getPlayer(received.getId());
                if (current != null) {
                    current.doAction(new PlayerAction(received), this.collisionController);
                }
            } else {
                if (received instanceof MessagePlayerNew) {
                    MessagePlayerNew task = (MessagePlayerNew) received;
                    Debug.debug("NEW PLAYER");
                    if (this.animatorGameData != null) {
                        this.addPlayer(new Player(new User(task.getPseudo(), task.getId()), null, TankFactory.createTank(task.getEnumTanks(), this.animatorGameData), this.getShots(), 50, 50));
                        if (task.getId().equals(CurrentUser.getId())) {
                            CurrentUser.setInGame(true);
                            this.collisionController.createWorld(this.mapController);
                        } else {
                            Debug.debug("My position send -> [" + this.getPlayer(CurrentUser.getId()).getPlayerState().getX() + "," +
                                    this.getPlayer(CurrentUser.getId()).getPlayerState().getY() + "]");
                            MessageModel request = new MessagePlayerUpdatePosition(CurrentUser.getPseudo(), CurrentUser.getId(),
                                    this.getPlayer(CurrentUser.getId()).getPlayerState().getX(),
                                    this.getPlayer(CurrentUser.getId()).getPlayerState().getY());
                            this.setChanged();
                            this.notifyObservers(request);
                        }
                    }
                }
                if (received instanceof MessagePlayerDelete) {
                    Debug.debug("DELETE PLAYER");
                    MessagePlayerDelete task = (MessagePlayerDelete) received;
                    this.deletePlayer(task.getId());
                }
                if (received instanceof MessagePlayerUpdateState) {
                    Debug.debug("UPDATE PLAYER");
                    MessagePlayerUpdateState task = (MessagePlayerUpdateState) received;
                    this.updateStatePlayer(task);
                }
                if (received instanceof MessagePlayerUpdatePosition) {
                    Debug.debug("UPDATE POS PLAYER");
                    MessagePlayerUpdatePosition task = (MessagePlayerUpdatePosition) received;
                    this.updatePositionPlayer(task);
                }
            }
        }
    }

    // FUNCTIONS
    public void addPlayer(Player player) {
        Debug.debug("add player: [" + player.getPlayerState().getX() + "," + player.getPlayerState().getY() + "] ?= [" + player.getPlayerState().getGraphicalX() + "," + player.getPlayerState().getGraphicalY() + "]");
        CollisionObject obj = new CollisionObject(true, player.getPlayerState().getGraphicalX(), player.getPlayerState().getGraphicalY(),
                player.getTank().getTankAnimator().currentSizeAnimation(),
                player.getPlayerState().getUser().getIdUser(), player.getPlayerState().getUser().getId(), EnumType.TANK, player.getPlayerState().getDirection().getAngle());
        obj.addObserver(player);
        collisionController.addCollisionObject(obj);
        this.players.add(player);
        this.collisionController.addCollisionObject(obj);
    }

    public void updateStatePlayer(MessagePlayerUpdateState task) {
        for (int i = 0; i < this.players.size(); ++i) {
            if (this.players.get(i).getPlayerState().getUser().getIdUser().equals(task.getId())) {
                this.players.remove(i);
                this.players.get(i).getTank().getTankState().setCurrentLife(task.getCurrentLife());
                this.players.get(i).getTank().getTankState().setBoostEffect(task.getBoostEffect());
                this.players.get(i).getTank().getTankState().setShieldEffect(task.getShieldEffect());
                this.players.get(i).getTank().getTankState().setSlowEffect(task.getSlowEffect());
                this.players.get(i).getTank().getTankState().setArmor(task.getArmor());
                this.players.get(i).setShots(this.shots);
                break;
            }
        }
    }

    public void updatePositionPlayer(MessagePlayerUpdatePosition task) {
        Debug.debug("new pos [" + task.getX() + "," + task.getY() + "] : id="+task.getId());
        for (int i = 0; i < this.players.size(); ++i) {
            if (this.players.get(i).getPlayerState().getUser().getIdUser().equals(task.getId())) {
                this.players.get(i).getPlayerState().setX(task.getX());
                this.players.get(i).getPlayerState().setY(task.getY());
                this.collisionController.getCollisionObject(this.players.get(i).getPlayerState().getUser().getId()).setX(task.getX());
                this.collisionController.getCollisionObject(this.players.get(i).getPlayerState().getUser().getId()).setY(task.getY());
                break;
            }
        }
    }

    public void deletePlayer(String id) {
        for (int i = 0; i < this.players.size(); ++i) {
            if (this.players.get(i).getPlayerState().getUser().getIdUser().equals(id)) {
                this.players.remove(i);
                break;
            }
        }
    }

    // GETTERS
    public List<Player> getPlayers() {
        return this.players;
    }

    public CollisionController getCollisionController() {
        return this.collisionController;
    }

    public MapController getMapController() {
        return this.mapController;
    }

    public RoundController getRoundController() {
        return roundController;
    }

    public List<Shot> getShots() {
        return shots;
    }

    public Player getPlayer(String id) {
        for (int i = 0; i < this.players.size(); ++i) {
            if (id.equals(this.players.get(i).getPlayerState().getUser().getIdUser())) {
                return this.players.get(i);
            }
        }
        return null;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setAnimatorGameData(AnimatorGameData animatorGameData) {
        this.animatorGameData = animatorGameData;
    }
}
