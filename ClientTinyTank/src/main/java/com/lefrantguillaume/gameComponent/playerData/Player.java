package com.lefrantguillaume.gameComponent.playerData;

import com.lefrantguillaume.Utils.stockage.Pair;
import com.lefrantguillaume.Utils.stockage.Tuple;
import com.lefrantguillaume.collisionComponent.CollisionController;
import com.lefrantguillaume.gameComponent.actions.Action;
import com.lefrantguillaume.gameComponent.actions.ActionController;
import com.lefrantguillaume.gameComponent.gameObject.EnumType;
import com.lefrantguillaume.gameComponent.gameObject.projectiles.EnumAnimationShot;
import com.lefrantguillaume.gameComponent.gameObject.projectiles.Shot;
import com.lefrantguillaume.gameComponent.gameObject.tanks.Tank;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by andres_k on 13/03/2015.
 */
public class Player implements Observer{
    private List<Shot> shots;
    private Team team;
    private Tank tank;
    private ActionController actionController;
    private PlayerState playerState;
    private boolean inGame;

    public Player(User user, Team team, Tank tank, List<Shot> shots, float x, float y) {
        this.inGame = false;
        this.shots = shots;
        this.team = team;
        this.tank = tank;
        this.playerState = new PlayerState(user, x, y);
        this.actionController = new ActionController(this.playerState, this.shots, this.tank);
        //this.actionController.addObserver();
    }


    // FUNCTIONS

    @Override
    public void update(Observable o, Object arg) {
        Tuple<Float, Float, EnumType> order = (Tuple<Float, Float, EnumType>) arg;

        if (order.getV3() == EnumType.SMOKE) {
        }
    }

    public void doAction(Action action, CollisionController collisionController) {
        this.actionController.doAction(action, collisionController);
    }

    public void move(int delta) {
        Pair<Float, Float> coords = movePredict(delta);
        this.getPlayerState().setX(coords.getV1());
        this.getPlayerState().setY(coords.getV2());
    }

    public Pair<Float, Float> movePredict(int delta) {
        float x = this.getPlayerState().getX();
        float y = this.getPlayerState().getY();
        if (this.getPlayerState().isMove()) {
            switch (this.getPlayerState().getDirection()) {
                case UP:
                    y = y - ((this.getTank().getTankState().getCurrentSpeed() / 10) * delta);
                    break;
                case RIGHT:
                    x = x + ((this.getTank().getTankState().getCurrentSpeed() / 10) * delta);
                    break;
                case DOWN:
                    y = y + ((this.getTank().getTankState().getCurrentSpeed() / 10) * delta);
                    break;
                case LEFT:
                    x = x - ((this.getTank().getTankState().getCurrentSpeed() / 10) * delta);
                    break;
            }
        }
        return new Pair<Float, Float>(x, y);
    }

    public int getDamage(float damage){
        return 0;
    }

    public int getEffect(){
        return 0;
    }

    // GETTERS

    public PlayerState getPlayerState() {
        return this.playerState;
    }

    public Tank getTank() {
        return tank;
    }

    public Team getTeam() {
        return team;
    }

    public boolean isInGame() {
        return inGame;
    }

    public void setInGame(boolean inGame) {
        this.inGame = inGame;
    }
}
