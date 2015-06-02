package com.lefrantguillaume.components.gameComponent.playerData.data;

import com.lefrantguillaume.Utils.stockage.Pair;
import com.lefrantguillaume.Utils.tools.MathTools;
import com.lefrantguillaume.components.gameComponent.gameObject.EnumGameObject;
import com.lefrantguillaume.components.gameComponent.gameObject.tanks.Tank;
import com.lefrantguillaume.components.gameComponent.playerData.action.PlayerActionController;
import com.lefrantguillaume.Utils.stockage.Tuple;
import com.lefrantguillaume.components.collisionComponent.CollisionController;
import com.lefrantguillaume.components.gameComponent.gameObject.projectiles.Shot;
import com.lefrantguillaume.components.gameComponent.playerData.action.PlayerAction;

import java.util.*;

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
        Tuple<Float, Float, EnumGameObject> order = (Tuple<Float, Float, EnumGameObject>) arg;

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

    public Tuple<Float, Float, Float> predictCreateBox(CollisionController collisionController){
        Tuple<Float, Float, Float> boxValues = new Tuple<Float, Float, Float>(0f, 0f, 0f);
        float canonAngle = this.tank.getTankState().getGunAngle();
        double boxAngle = canonAngle + 90;
        //TODO check collision
        //TODO trouver un new point
        boxValues.setV1(15f);
        boxValues.setV2(15f);
        boxValues.setV3((float)boxAngle);
        return boxValues;
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

    public List<EnumGameObject> getIgnoredObjectList(){
        List<EnumGameObject> types = new ArrayList<>();

        return types;
    }
}
