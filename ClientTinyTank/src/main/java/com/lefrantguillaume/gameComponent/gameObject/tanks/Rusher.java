package com.lefrantguillaume.gameComponent.gameObject.tanks;

import com.lefrantguillaume.gameComponent.animations.Animator;
import com.lefrantguillaume.gameComponent.gameObject.spells.Shield;

/**
 * Created by andres_k on 13/03/2015.
 */
public class Rusher extends Tank {
    public Rusher(Animator tankAnimator, Animator gunAnimator, Animator shotAnimator, Animator spellAnimator) {
        this.type = EnumTanks.RUSHER;
        this.tankAnimator = tankAnimator;
        this.shotAnimator = shotAnimator;
        this.gunAnimator = gunAnimator;
        this.tankWeapon = new TankWeapon(10, 20, new Shield(spellAnimator));
        this.tankState = new TankState(6, 10, 0);
    }
}
