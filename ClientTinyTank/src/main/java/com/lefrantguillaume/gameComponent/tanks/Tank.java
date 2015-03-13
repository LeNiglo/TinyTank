package com.lefrantguillaume.gameComponent.tanks;

import com.lefrantguillaume.Utils.stockage.Tuple;
import com.lefrantguillaume.gameComponent.actions.Shot;
import com.lefrantguillaume.gameComponent.animations.Animator;

/**
 * Created by andres_k on 13/03/2015.
 */
public class Tank {
    protected EnumTanks idTank;
    protected Animator tankAnimator;
    protected Animator shotAnimator;
    protected float damageShot;
    protected float speedShot;
    protected float speedTank;

    public EnumTanks getIdTank() {
        return idTank;
    }

    public Animator getTankAnimator() {
        return tankAnimator;
    }

    public float getSpeedTank(){
        return this.speedTank;
    }
    public float getSpeedShot(){
        return this.speedShot;
    }
    public float getDamageShot(){
        return this.damageShot;
    }
    public void setTankAnimator(Animator animations) {
        this.tankAnimator = animations;
    }

    public Shot generateShot(int userId, int shotId, Tuple<Float, Float, Float> coord) {
        Shot shot = new Shot(this.damageShot, this.speedShot, new Animator(this.shotAnimator), coord, userId, shotId);
        return shot;
    }
}
