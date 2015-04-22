package com.lefrantguillaume.game.gameobjects.tanks.types;

import com.lefrantguillaume.game.gameobjects.tanks.equipment.TankSpell;
import com.lefrantguillaume.game.gameobjects.tanks.equipment.TankState;
import com.lefrantguillaume.game.gameobjects.tanks.equipment.TankWeapon;
import com.lefrantguillaume.game.gameobjects.tanks.tools.TankFactory;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * Created by andres_k on 13/03/2015.
 */
public class Tank {
    private TankWeapon tankWeapon;
    private TankState tankState;
    private TankSpell tankSpell;

    public Tank(JSONObject config) throws JSONException {
        this.tankWeapon = TankFactory.createTankWeapon(config.getJSONObject("tankWeapon"));
        this.tankState = TankFactory.createTankState(config.getJSONObject("tankState"));
        this.tankSpell = TankFactory.createTankSpell(config.getJSONObject("tankSpell"));
    }

    public Tank(Tank tank){
        this.tankWeapon = new TankWeapon(tank.tankWeapon);
        this.tankState = new TankState(tank.tankState);
        this.tankSpell = new TankSpell(tank.tankSpell);
    }

    public TankWeapon getTankWeapon(){
        return this.tankWeapon;
    }

    public TankState getTankState(){
        return this.tankState;
    }

}
