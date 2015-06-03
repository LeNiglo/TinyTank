package com.lefrantguillaume.gameComponent.gameobjects.tanks;

import com.lefrantguillaume.gameComponent.gameobjects.obstacles.Obstacle;
import com.lefrantguillaume.gameComponent.gameobjects.tanks.equipment.TankBox;
import com.lefrantguillaume.gameComponent.gameobjects.tanks.equipment.TankSpell;
import com.lefrantguillaume.gameComponent.gameobjects.tanks.equipment.TankState;
import com.lefrantguillaume.gameComponent.gameobjects.tanks.equipment.TankWeapon;
import com.lefrantguillaume.gameComponent.gameobjects.tanks.tools.TankFactory;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * Created by andres_k on 13/03/2015.
 */
public class Tank {
    private TankWeapon tankWeapon;
    private TankState tankState;
    private TankSpell tankSpell;
    private TankBox tankBox;

    public Tank(JSONObject config) throws JSONException {
        this.tankWeapon = TankFactory.createTankWeapon(config.getJSONObject("tankWeapon"));
        this.tankState = TankFactory.createTankState(config.getJSONObject("tankState"));
        this.tankSpell = TankFactory.createTankSpell(config.getJSONObject("tankSpell"));
        this.tankBox = TankFactory.createTankBox(config.getJSONObject("tankBox"));
    }

    public Tank(Tank tank){
        this.tankWeapon = new TankWeapon(tank.tankWeapon);
        this.tankState = new TankState(tank.tankState);
        this.tankSpell = new TankSpell(tank.tankSpell);
        this.tankBox = new TankBox(tank.tankBox);
    }

    // FUNCTIONS

    public void revive(){
        this.tankState.init();
    }

    public Obstacle generateObstacle(String playerId, String playerPseudo, String obstacleId, float angle, float posX, float posY){
        Obstacle obstacle = this.tankBox.generateBox();

        obstacle.createObstacle(playerId, playerPseudo, obstacleId, angle, posX, posY);
        return obstacle;
    }

    // GETTERS

    public TankWeapon getTankWeapon(){
        return this.tankWeapon;
    }

    public TankState getTankState(){
        return this.tankState;
    }

}
