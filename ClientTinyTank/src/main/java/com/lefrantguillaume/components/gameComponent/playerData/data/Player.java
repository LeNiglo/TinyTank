package com.lefrantguillaume.components.gameComponent.playerData.data;

import com.lefrantguillaume.utils.stockage.Pair;
import com.lefrantguillaume.utils.stockage.Tuple;
import com.lefrantguillaume.utils.tools.MathTools;
import com.lefrantguillaume.components.collisionComponent.CollisionController;
import com.lefrantguillaume.components.gameComponent.gameObject.EnumGameObject;
import com.lefrantguillaume.components.gameComponent.gameObject.obstacles.Obstacle;
import com.lefrantguillaume.components.gameComponent.gameObject.obstacles.ObstacleConfigData;
import com.lefrantguillaume.components.gameComponent.gameObject.projectiles.Shot;
import com.lefrantguillaume.components.gameComponent.gameObject.tanks.Tank;
import com.lefrantguillaume.components.gameComponent.playerData.action.PlayerAction;
import com.lefrantguillaume.components.gameComponent.playerData.action.PlayerActionController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andres_k on 13/03/2015.
 */
public class Player {
    private List<Shot> shots;
    private String idTeam;
    private Tank tank;
    private PlayerActionController playerActionController;
    private User user;
    private boolean alive;
    private boolean canDoAction;
    private boolean explose;

    public Player(User user, String idTeam, Tank tank, List<Shot> shots, float x, float y) {
        this.user = user;
        this.alive = true;
        this.canDoAction = true;
        this.explose = false;
        this.shots = shots;
        this.idTeam = idTeam;
        this.tank = tank;
        this.tank.getTankState().setPositions(new Pair<>(x, y));
        this.playerActionController = new PlayerActionController(this.shots);
    }


    // FUNCTIONS

    public List<Object> doAction(PlayerAction playerAction, CollisionController collisionController) {
        if (this.canDoAction == true)
            return this.playerActionController.doAction(playerAction, collisionController, this);
        return null;
    }

    public void move(float delta) {
        if (this.alive == true && this.canDoAction == true) {
            Pair<Float, Float> coords = this.movePredict(delta);
            this.tank.getTankState().addX(coords.getV1());
            this.tank.getTankState().addY(coords.getV2());
            this.tank.getTankSpell().move(new Pair<>(this.tank.getTankState().getX(), this.tank.getTankState().getY()));
        }
    }

    public Pair<Float, Float> movePredict(float delta) {
        return MathTools.movePredict(this.tank.getTankState().getDirection().getAngle(), this.tank.getTankState().getCurrentSpeed(), delta);
    }

    public Pair<Float, Float> coordPredict(float delta) {
        Pair<Float, Float> coords = this.movePredict(delta);
        coords.setV1(coords.getV1() + this.tank.getTankState().getX());
        coords.setV2(coords.getV2() + this.tank.getTankState().getY());
        return coords;
    }

    public boolean kill() {
        if (this.tank.getTankState().getCurrentLife() <= 0 && this.explose == false) {
            this.canDoAction = false;
            this.explose = true;
            this.tank.explode();
            this.tank.myNotify(new Tuple<>(false, 0f, 0f));
            return true;
        }
        return false;
    }

    public void die() {
        this.alive = false;
        this.canDoAction = false;
        //this.tank.myNotify(new Tuple<>(false, 0f, 0f));
    }

    public void revive(Pair<Float, Float> positions) {
        if (this.alive == false) {
            this.alive = true;
            this.canDoAction = true;
            this.explose = false;
            this.getTank().revive(positions);
            this.tank.myNotify(new Tuple<>(true, this.tank.getTankState().getPositions().getV1(), this.tank.getTankState().getPositions().getV2()));
        }
    }

    public Tuple<Float, Float, Float> predictCreateBox(CollisionController collisionController, ObstacleConfigData obstacleConfigData) {
        Tuple<Float, Float, Float> boxValues = new Tuple<>(0f, 0f, 0f);
        float canonAngle = this.tank.getTankState().getGunAngle();
        float boxAngle = canonAngle + 90;

        Pair<Float, Float> newPoint = new Pair<>(this.getTank().getTankState().getX() + 80, this.getTank().getTankState().getY());
        MathTools.rotate(new Pair<>(this.getTank().getTankState().getX(), this.getTank().getTankState().getY()), newPoint, canonAngle);
        boxValues.setV1(newPoint.getV1());
        boxValues.setV2(newPoint.getV2());
        boxValues.setV3(boxAngle);
        Obstacle obstacle = obstacleConfigData.getObstacle(this.getTank().getTankBox().getType());
        for (int i = 0; i < obstacle.getCollisionObject().size(); ++i) {
            if (collisionController.checkCollision(newPoint, obstacle.getCollisionObject().get(i).getSizes(), boxAngle, obstacle.getIgnoredObjectList(), obstacle.getType())) {
                return null;
            }
        }
        return boxValues;
    }

    // GETTERS

    public boolean isAlive() {
        return this.alive;
    }

    public boolean isCanDoAction(){
        return this.canDoAction;
    }

    public Tank getTank() {
        return this.tank;
    }

    public String getIdTeam() {
        return this.idTeam;
    }

    public User getUser() {
        return this.user;
    }

    public List<EnumGameObject> getIgnoredObjectList() {
        List<EnumGameObject> types = new ArrayList<>();
        return types;
    }

    // SETTERS
    public void setShots(List<Shot> shots) {
        this.shots = shots;
    }

    public void setCanDoAction(boolean value){
        this.canDoAction = value;
    }
}

