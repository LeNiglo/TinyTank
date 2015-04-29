package com.lefrantguillaume.gestGame.gameComponent.playerData.data;

import com.lefrantguillaume.gestGame.Utils.stockage.Pair;
import com.lefrantguillaume.gestGame.Utils.stockage.Tuple;
import com.lefrantguillaume.gestGame.Utils.tools.MathTools;
import com.lefrantguillaume.gestGame.collisionComponent.CollisionController;
import com.lefrantguillaume.gestGame.gameComponent.gameObject.EnumType;
import com.lefrantguillaume.gestGame.gameComponent.gameObject.projectiles.Shot;
import com.lefrantguillaume.gestGame.gameComponent.gameObject.tanks.types.Tank;
import com.lefrantguillaume.gestGame.gameComponent.playerData.action.PlayerAction;
import com.lefrantguillaume.gestGame.gameComponent.playerData.action.PlayerActionController;

import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.UUID;

/**
 * Created by andres_k on 13/03/2015.
 */
public class Player extends Observable implements Observer{
    private List<Shot> shots;
    private UUID idTeam;
    private Tank tank;
    private PlayerActionController playerActionController;
    private User user;
    private boolean alive;

    public Player(User user, UUID idTeam, Tank tank, List<Shot> shots, float x, float y) {
        this.user = user;
        this.alive = true;
        this.shots = shots;
        this.idTeam = idTeam;
        this.tank = tank;
        this.tank.getTankState().setPositions(new Pair<Float, Float>(x, y));
        this.playerActionController = new PlayerActionController(this.shots, this.tank);
    }


    // FUNCTIONS

    @Override
    public void update(Observable o, Object arg) {
        Tuple<Float, Float, EnumType> order = (Tuple<Float, Float, EnumType>) arg;

        if (order.getV3() == EnumType.AREA) {
        }
    }

    public void doAction(PlayerAction playerAction, CollisionController collisionController) {
        this.playerActionController.doAction(playerAction, collisionController, this);
    }

    public void move(float delta) {
        if (this.alive == true) {
            Pair<Float, Float> coords = this.movePredict(delta);
            this.tank.getTankState().addX(coords.getV1());
            this.tank.getTankState().addY(coords.getV2());
        }
    }

    public Pair<Float, Float> movePredict(float delta) {
        return MathTools.movePredict(this.tank.getTankState().getDirection().getAngle(), this.tank.getTankState().getSpeed(), delta);
    }

    public Pair<Float, Float> coordPredict(float delta) {
        Pair<Float, Float> coords = this.movePredict(delta);
        coords.setV1(coords.getV1() + this.tank.getTankState().getX());
        coords.setV2(coords.getV2() + this.tank.getTankState().getY());
        return coords;
    }

    public boolean kill(){
        if (this.tank.getTankState().getCurrentLife() <= 0) {
            this.tank.explode();
            this.setChanged();
            this.notifyObservers(new Tuple<Boolean, Float, Float>(false, 0f, 0f));
            return true;
        }
        return false;
    }

    public void die(){
        this.alive = false;
    }

    public void revive(Pair<Float, Float> positions){
        this.alive = true;
        this.getTank().revive(positions);
        this.setChanged();
        this.notifyObservers(new Tuple<Boolean, Float, Float>(true, this.tank.getTankState().getPositions().getV1(), this.tank.getTankState().getPositions().getV2()));
    }

    // GETTERS

    public boolean isAlive(){
        return this.alive;
    }

    public Tank getTank() {
        return this.tank;
    }

    public UUID getIdTeam() {
        return this.idTeam;
    }

    public void setShots(List<Shot> shots) {
        this.shots = shots;
    }

    public User getUser() {
        return user;
    }
}
