package com.lefrantguillaume.gameComponent.playerData.data;

import com.lefrantguillaume.Utils.stockage.Pair;
import com.lefrantguillaume.Utils.stockage.Tuple;
import com.lefrantguillaume.collisionComponent.CollisionController;
import com.lefrantguillaume.gameComponent.gameObject.EnumType;
import com.lefrantguillaume.gameComponent.gameObject.projectiles.Shot;
import com.lefrantguillaume.gameComponent.gameObject.tanks.Tank;
import com.lefrantguillaume.gameComponent.playerData.action.PlayerAction;
import com.lefrantguillaume.gameComponent.playerData.action.PlayerActionController;

import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.UUID;

/**
 * Created by andres_k on 13/03/2015.
 */
public class Player implements Observer{
    private List<Shot> shots;
    private UUID idTeam;
    private Tank tank;
    private PlayerActionController playerActionController;
    private PlayerState playerState;
    private boolean inGame;

    public Player(User user, UUID idTeam, Tank tank, List<Shot> shots, float x, float y) {
        this.inGame = false;
        this.shots = shots;
        this.idTeam = idTeam;
        this.tank = tank;
        this.playerState = new PlayerState(user, x, y, tank.getTankAnimator().currentSizeAnimation());
        this.playerActionController = new PlayerActionController(this.playerState, this.shots, this.tank);
        //this.actionController.addObserver();
    }


    // FUNCTIONS

    @Override
    public void update(Observable o, Object arg) {
        Tuple<Float, Float, EnumType> order = (Tuple<Float, Float, EnumType>) arg;

        if (order.getV3() == EnumType.SMOKE) {
        }
    }

    public void doAction(PlayerAction playerAction, CollisionController collisionController) {
        this.playerActionController.doAction(playerAction, collisionController);
    }

    public void move(int delta) {
        Pair<Float, Float> coords = movePredict(delta);
        this.getPlayerState().setX(coords.getV1());
        this.getPlayerState().setY(coords.getV2());
    }

    public Pair<Float, Float> movePredict(int delta) {
        double addX = Math.cos(this.getPlayerState().getDirection().getAngle() * Math.PI / 180);
        double addY = Math.sin(this.getPlayerState().getDirection().getAngle() * Math.PI / 180);
        float x = this.getPlayerState().getAbsoluteX() + (((float)addX / 10) * delta);
        float y = this.getPlayerState().getAbsoluteY() + (((float)addY / 10) * delta);
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
}
