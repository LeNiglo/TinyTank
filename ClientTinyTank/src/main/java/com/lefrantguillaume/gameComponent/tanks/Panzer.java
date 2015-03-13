package com.lefrantguillaume.gameComponent.tanks;

import com.lefrantguillaume.gameComponent.animations.Animator;

/**
 * Created by andres_k on 13/03/2015.
 */
public class Panzer extends Tank {
    public Panzer(Animator tankAnimator, Animator shotAnimator) {
        this.idTank = EnumTanks.PANZER;
        this.tankAnimator = tankAnimator;
        this.shotAnimator = shotAnimator;
        this.damageShot = 10;
        this.speedShot = 20;
        this.speedTank = 2;
    }
}
