package com.lefrantguillaume.game;

import javafx.util.Pair;

/**
 * Created by Styve on 19/03/2015.
 */

public class Player {
    private List<Shot> shots;
    private Team team;
    private Tank tank;
    private ActionController actionController;
    private PlayerState playerState;
    private String userName;

    public Player(Team team, Tank tank, List<Shot> shots, int x, int y) {
        this.shots = shots;
        this.team = team;
        this.tank = tank;
        this.userName = User.getPseudo();
        this.playerState = new PlayerState(x, y, User.getId());
        this.actionController = new ActionController(this.playerState, this.shots, this.tank);
        this.actionController.addObserver(tank.getTankAnimator());
    }

    public String getUserName() {
        return this.userName;
    }

    public void doAction(Action action, CollisionController collisionController) {
        this.actionController.doAction(action, collisionController);
    }

    public PlayerState getPlayerState() {
        return this.playerState;
    }

    public Tank getTank() {
        return tank;
    }

    public Team getTeam() {
        return team;
    }

    public void move(int delta){
        Pair<Float, Float> coords = movePredict(delta);
        this.getPlayerState().setX(coords.getKey());
        this.getPlayerState().setY(coords.getValue());
    }

    public Pair<Float, Float> movePredict(int delta){
        float x = this.getPlayerState().getX();
        float y = this.getPlayerState().getY();
        if (this.getPlayerState().isMove()) {
            switch (this.getPlayerState().getDirection()) {
                case UP:
                    y = y - ((this.getTank().getSpeedTank() / 10) * delta);
                    break;
                case RIGHT:
                    x = x + ((this.getTank().getSpeedTank() / 10) * delta);
                    break;
                case DOWN:
                    y = y + ((this.getTank().getSpeedTank() / 10) * delta);
                    break;
                case LEFT:
                    x = x - ((this.getTank().getSpeedTank() / 10) * delta);;
                    break;
            }
        }
        return new Pair<Float, Float>(x, y);
    }

}
