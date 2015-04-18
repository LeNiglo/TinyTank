package com.lefrantguillaume.gameComponent.playerData.data;

import com.lefrantguillaume.Utils.stockage.Pair;
import com.lefrantguillaume.Utils.stockage.Tuple;
import com.lefrantguillaume.Utils.tools.MathTools;
import com.lefrantguillaume.collisionComponent.CollisionController;
import com.lefrantguillaume.gameComponent.gameObject.EnumType;
import com.lefrantguillaume.gameComponent.gameObject.projectiles.Shot;
import com.lefrantguillaume.gameComponent.gameObject.tanks.types.Tank;
import com.lefrantguillaume.gameComponent.playerData.action.PlayerAction;
import com.lefrantguillaume.gameComponent.playerData.action.PlayerActionController;

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
    private boolean inGame;

    public Player(User user, UUID idTeam, Tank tank, List<Shot> shots, float x, float y) {
        this.user = user;
        this.inGame = false;
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

        if (order.getV3() == EnumType.SMOKE) {
        }
    }

    public void doAction(PlayerAction playerAction, CollisionController collisionController) {
        this.playerActionController.doAction(playerAction, collisionController, this);
    }

    public void move() {
        Pair<Float, Float> coords = this.movePredict();
        this.tank.getTankState().addX(coords.getV1());
        this.tank.getTankState().addY(coords.getV2());
    }

    public Pair<Float, Float> movePredict() {
        return MathTools.movePredict(this.tank.getTankState().getDirection().getAngle(), this.tank.getTankState().getSpeed());
    }

    public int getDamage(float damage) {
        return 0;
    }

    public int getEffect() {
        return 0;
    }

    // GETTERS

    public Tank getTank() {
        return this.tank;
    }

    public UUID getIdTeam() {
        return this.idTeam;
    }

    public boolean isInGame() {
        return this.inGame;
    }

    public void setInGame(boolean inGame) {
        this.inGame = inGame;
    }

    public void setShots(List<Shot> shots) {
        this.shots = shots;
    }

    public User getUser() {
        return user;
    }
}
