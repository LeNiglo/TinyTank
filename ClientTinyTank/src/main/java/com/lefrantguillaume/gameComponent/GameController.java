package com.lefrantguillaume.gameComponent;

import com.lefrantguillaume.collisionComponent.CollisionController;
import com.lefrantguillaume.collisionComponent.CollisionObject;
import com.lefrantguillaume.gameComponent.actions.Action;
import com.lefrantguillaume.gameComponent.actions.Shot;
import com.lefrantguillaume.gameComponent.tanks.EnumType;
import com.lefrantguillaume.networkComponent.messages.MessageModel;

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

    public GameController() {
        this.players = new ArrayList<Player>();
        this.shots = new ArrayList<Shot>();
        this.collisionController = new CollisionController();
        this.collisionController.addCollisionObject(new CollisionObject(250, 250, 80, 80, -5, -5, EnumType.OBSTACLE));
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void addPlayer(Player player) {

        this.players.add(player);
        this.collisionController.addCollisionObject(new CollisionObject(player.getPlayerState().getX(), player.getPlayerState().getY(),
                player.getTank().getTankAnimator().currentAnimation().getCurrentFrame().getWidth(),
                player.getTank().getTankAnimator().currentAnimation().getCurrentFrame().getHeight(), player.getPlayerState().getUserId(), player.getPlayerState().getUserId(), EnumType.TANK));
    }

    public Player getPlayer(int id) {
        for (int i = 0; i < this.players.size(); ++i)
            if (id == this.players.get(i).getPlayerState().getUserId()) {
                return this.players.get(i);
            }
        return null;
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

    public CollisionController getCollisionController(){
        return this.collisionController;
    }
    public List<Shot> getShots() {
        return shots;
    }
}
