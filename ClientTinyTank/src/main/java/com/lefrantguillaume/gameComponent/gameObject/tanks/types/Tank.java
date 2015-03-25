package com.lefrantguillaume.gameComponent.gameObject.tanks.types;

import com.lefrantguillaume.Utils.stockage.Tuple;
import com.lefrantguillaume.gameComponent.animations.AnimatorGameData;
import com.lefrantguillaume.gameComponent.gameObject.projectiles.Shot;
import com.lefrantguillaume.gameComponent.animations.Animator;
import com.lefrantguillaume.gameComponent.gameObject.tanks.equipment.TankSpell;
import com.lefrantguillaume.gameComponent.gameObject.tanks.equipment.TankState;
import com.lefrantguillaume.gameComponent.gameObject.tanks.equipment.TankWeapon;
import com.lefrantguillaume.gameComponent.gameObject.tanks.tools.TankFactory;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * Created by andres_k on 13/03/2015.
 */
public class Tank {
    private TankWeapon tankWeapon;
    private TankState tankState;
    private TankSpell tankSpell;

    public Tank(JSONObject config, AnimatorGameData animatorGameData) throws JSONException {
        this.tankWeapon = TankFactory.createTankWeapon(config.getJSONObject("tankWeapon"), animatorGameData);
        this.tankState = TankFactory.createTankState(config.getJSONObject("tankState"), animatorGameData);
        this.tankSpell = TankFactory.createTankSpell(config.getJSONObject("tankSpell"), animatorGameData);
    }

    public Tank(Tank tank){
        this.tankWeapon = new TankWeapon(tank.tankWeapon);
        this.tankState = new TankState(tank.tankState);
        this.tankSpell = new TankSpell(tank.tankSpell);
    }

    // FUNCTIONS
    public Shot generateShot(String userId, float angle) {
        return this.tankWeapon.generateShot(userId, angle, this.tankState.getPositions());
    }

    // GETTERS
    public Animator getTankAnimator() {
        return this.getTankState().getBodyAnimator();
    }

    public Animator getTopAnimator() {
        return this.getTankState().getTopAnimator();
    }

    public Animator getShotAnimator() {
        return this.getTankWeapon().getShotAnimator();
    }

    public TankWeapon getTankWeapon(){
        return this.tankWeapon;
    }

    public TankState getTankState(){
        return this.tankState;
    }

}
