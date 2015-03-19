package com.lefrantguillaume.gameComponent.controllers;

import com.lefrantguillaume.Utils.configs.MasterConfig;
import com.lefrantguillaume.collisionComponent.CollisionController;
import com.lefrantguillaume.collisionComponent.CollisionObject;
import com.lefrantguillaume.gameComponent.playerData.Player;
import com.lefrantguillaume.gameComponent.actions.Action;
import com.lefrantguillaume.gameComponent.gameObject.projectiles.Shot;
import com.lefrantguillaume.gameComponent.gameObject.EnumType;
import com.lefrantguillaume.networkComponent.messages.MessageModel;
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
    private CollisionController collisionController;
    private MapController mapController;
    private RoundController roundController;

    public GameController() throws SlickException {
        this.players = new ArrayList<Player>();
        this.shots = new ArrayList<Shot>();
        this.collisionController = new CollisionController();
        this.mapController = new MapController(this.collisionController, MasterConfig.getMapConfigFile());
        this.roundController = new RoundController(this.players);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof MessageModel) {
            MessageModel action = (MessageModel) arg;
            Player current = this.getPlayer(action.getId());
            if (current != null) {
                current.doAction(new Action(action), this.collisionController);
            }
        }
    }

    // FUNCTIONS
    public void addPlayer(Player player) {


        CollisionObject obj = new CollisionObject(true, player.getPlayerState().getX(), player.getPlayerState().getY(),
                player.getTank().getTankAnimator().currentAnimation().getCurrentFrame().getWidth(),
                player.getTank().getTankAnimator().currentAnimation().getCurrentFrame().getHeight(),
                player.getPlayerState().getUser().getId(), player.getPlayerState().getUser().getId(), EnumType.TANK);
        obj.addObserver(player);
        collisionController.addCollisionObject(obj);
        this.players.add(player);
        this.collisionController.addCollisionObject(obj);
    }

    // GETTERS
    public List<Player> getPlayers() {
        return players;
    }

    public CollisionController getCollisionController() {
        return this.collisionController;
    }

    public MapController getMapController(){return this.mapController;}

    public RoundController getRoundController() {
        return roundController;
    }

    public List<Shot> getShots() {
        return shots;
    }

    public Player getPlayer(int id) {
        for (int i = 0; i < this.players.size(); ++i) {
            if (id == this.players.get(i).getPlayerState().getUser().getId()) {
                return this.players.get(i);
            }
        }
        return null;
    }


}
