package com.lefrantguillaume.gameComponent.controllers;

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
import com.lefrantguillaume.networkComponent.messages.msg.MessagePlayerUpdate;
import org.newdawn.slick.SlickException;
import sun.misc.MessageUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by andres_k on 13/03/2015.
 */
public class GameController  extends Observable implements Observer{
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
        this.collisionController = new CollisionController();
        this.mapController = new MapController(this.collisionController, MasterConfig.getMapConfigFile());
        this.roundController = new RoundController(this.players, this.teams);
        this.animatorGameData = null;
    }

    @Override
    public void update(Observable o, Object arg) {
        Debug.debug("Message receive");
        if (arg instanceof MessageModel) {
            MessageModel received = (MessageModel) arg;
            if (received.getPlayerAction() == true) {
                Player current = this.getPlayer(received.getId());
                if (current != null) {
                    current.doAction(new PlayerAction(received), this.collisionController);
                }
            }
            else {
                if (received instanceof MessagePlayerNew){
                    MessagePlayerNew task = (MessagePlayerNew) received;
                    if (this.animatorGameData != null)
                        this.addPlayer(new Player(new User(task.getPseudo(), task.getId()), null, TankFactory.createTank(task.getEnumTanks(), this.animatorGameData), this.getShots(), 15, 15));
                }
                if (received instanceof MessagePlayerDelete){
                    MessagePlayerDelete task = (MessagePlayerDelete) received;
                    this.deletePlayer(task.getId());
                }
                if (received instanceof MessagePlayerUpdate){
                    MessagePlayerUpdate task = (MessagePlayerUpdate) received;
                    this.updatePlayer(task.getId(), task.getPlayer());
                }
            }
        }
    }

    // FUNCTIONS
    public void addPlayer(Player player) {
        CollisionObject obj = new CollisionObject(true, player.getPlayerState().getAbsoluteX(), player.getPlayerState().getAbsoluteY(),
                player.getTank().getTankAnimator().currentSizeAnimation(),
                player.getPlayerState().getUser().getIdUser(), player.getPlayerState().getUser().getId(), EnumType.TANK, player.getPlayerState().getDirection().getAngle());
        obj.addObserver(player);
        collisionController.addCollisionObject(obj);
        this.players.add(player);
        this.collisionController.addCollisionObject(obj);
    }

    public void updatePlayer(String id, Player player){
        for (int i = 0; i < this.players.size(); ++i){
            if (this.players.get(i).getPlayerState().getUser().getIdUser().equals(id)){
                this.players.remove(i);
                this.players.set(i, player);
                this.players.get(i).setShots(this.shots);
                break;
            }
        }
    }

    public void deletePlayer(String id){
        for (int i = 0; i < this.players.size(); ++i){
            if (this.players.get(i).getPlayerState().getUser().getIdUser().equals(id)){
                this.players.remove(i);
                break;
            }
        }
    }

    public void clearData(){
        this.players.clear();
        this.shots.clear();
        this.teams.clear();
        this.collisionController.clearCollisionObjects();
        this.mapController.clearObstacles();
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
