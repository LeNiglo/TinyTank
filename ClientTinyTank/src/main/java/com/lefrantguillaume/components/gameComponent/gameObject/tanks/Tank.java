package com.lefrantguillaume.components.gameComponent.gameObject.tanks;

import com.lefrantguillaume.Utils.stockage.Pair;
import com.lefrantguillaume.Utils.tools.RandomTools;
import com.lefrantguillaume.components.gameComponent.animations.Animator;
import com.lefrantguillaume.components.gameComponent.animations.AnimatorGameData;
import com.lefrantguillaume.components.gameComponent.gameObject.EnumGameObject;
import com.lefrantguillaume.components.gameComponent.gameObject.obstacles.ObstacleConfigData;
import com.lefrantguillaume.components.gameComponent.gameObject.projectiles.Shot;
import com.lefrantguillaume.components.gameComponent.gameObject.tanks.equipment.TankSpell;
import com.lefrantguillaume.components.gameComponent.gameObject.tanks.equipment.TankState;
import com.lefrantguillaume.components.gameComponent.gameObject.tanks.equipment.TankWeapon;
import com.lefrantguillaume.components.gameComponent.gameObject.tanks.tools.TankFactory;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * Created by andres_k on 13/03/2015.
 */
public class Tank {
    private TankWeapon tankWeapon;
    private TankState tankState;
    private TankSpell tankSpell;
    private EnumGameObject tankBox;

    public Tank(JSONObject config, AnimatorGameData animatorGameData, ObstacleConfigData obstacleConfigData) throws JSONException {
        this.tankWeapon = TankFactory.createTankWeapon(config.getJSONObject("tankWeapon"), animatorGameData);
        this.tankState = TankFactory.createTankState(config.getJSONObject("tankState"), animatorGameData);
        this.tankSpell = TankFactory.createTankSpell(config.getJSONObject("tankSpell"), animatorGameData, obstacleConfigData);
        this.tankBox = TankFactory.createTankBox(config.getJSONObject("tankBox"), animatorGameData);
    }

    public Tank(Tank tank) {
        this.tankWeapon = new TankWeapon(tank.tankWeapon);
        this.tankState = new TankState(tank.tankState);
        this.tankSpell = new TankSpell(tank.tankSpell);
        this.tankBox = tank.tankBox;
        this.tankSpell.init(this.tankState);
    }

    // FUNCTIONS
    public Shot generateShot(String userId, String id, float angle) {
        return this.tankWeapon.generateShot(userId, id, angle, this.tankState.getPositions());
    }

    public Object activeSpell() {
        return this.tankSpell.activeCurrentSpell();
    }

    public float predictAngleHit() {
        float angle = this.tankState.getGunAngle();
        float deviate = (100 - this.tankState.getAccuracy()) * 90 / 100;

        if (deviate != 0) {
            angle += RandomTools.getInt((int) deviate) - (deviate / 2);
        }
        return angle;
    }

    public void myNotify(Object object){
        this.tankState.myNotify(object);
    }

    public void explode() {
        this.tankState.explode();
        this.tankSpell.stopCurrentSpell();
    }

    public void revive(Pair<Float, Float> positions) {
        this.tankState.init(positions);
    }

    // GETTERS
    public Animator getBodyAnimator() {
        return this.getTankState().getBodyAnimator();
    }

    public Animator getTopAnimator() {
        return this.getTankState().getTopAnimator();
    }

    public Animator getShotAnimator() {
        return this.getTankWeapon().getShotAnimator();
    }

    public Animator getSpellAnimator(){
        return this.tankSpell.getAnimator();
    }

    public TankWeapon getTankWeapon() {
        return this.tankWeapon;
    }

    public TankState getTankState() {
        return this.tankState;
    }

    public TankSpell getTankSpell() {
        return this.tankSpell;
    }

    public EnumGameObject getTankBox() {
        return this.tankBox;
    }

    public boolean isSpellActivated(){
        return this.tankSpell.isActivate();
    }
}
