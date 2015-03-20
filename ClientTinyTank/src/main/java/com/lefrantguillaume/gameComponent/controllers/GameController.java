package com.lefrantguillaume.gameComponent.controllers;

import com.lefrantguillaume.Utils.configs.MasterConfig;
import com.lefrantguillaume.Utils.tools.Debug;
import com.lefrantguillaume.collisionComponent.CollisionController;
import com.lefrantguillaume.collisionComponent.CollisionObject;
import com.lefrantguillaume.gameComponent.RoundData.RoundController;
import com.lefrantguillaume.gameComponent.playerData.data.Player;
import com.lefrantguillaume.gameComponent.playerData.action.PlayerAction;
import com.lefrantguillaume.gameComponent.gameObject.projectiles.Shot;
import com.lefrantguillaume.gameComponent.gameObject.EnumType;
import com.lefrantguillaume.gameComponent.RoundData.Team;
import com.lefrantguillaume.networkComponent.messages.MessageModel;
import com.lefrantguillaume.networkComponent.messages.msg.MessageInitGame;
import org.newdawn.slick.SlickException;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by andres_k on 13/03/2015.
 */
public class GameController implements Observer {
    private List<Player> players;
    private List<Shot> shots;
    private List<Team> teams;
    private CollisionController collisionController;
    private MapController mapController;
    private RoundController roundController;

    public GameController() throws SlickException {
        this.players = new ArrayList<Player>();
        this.shots = new ArrayList<Shot>();
        this.teams = new ArrayList<Team>();
        this.collisionController = new CollisionController();
        this.mapController = new MapController(this.collisionController, MasterConfig.getMapConfigFile());
        this.roundController = new RoundController(this.players, this.teams);
    }

    @Override
    public void update(Observable o, Object arg) {
        Debug.debug("Message receive");
        if (arg instanceof MessageModel) {
            MessageModel action = (MessageModel) arg;
            if (action.getPlayerAction() == true) {
                Player current = this.getPlayer(action.getId());
                if (current != null) {
                    current.doAction(new PlayerAction(action), this.collisionController);
                }
            }
            else {
                if (action instanceof MessageInitGame){
                    MessageInitGame task = (MessageInitGame) action;

                    this.clearData();
                    this.players = task.getPlayers();
                    this.shots = task.getShots();

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

    private void clearData(){
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
}
