package com.lefrantguillaume.gameComponent.gameObject.tanks;

import com.lefrantguillaume.gameComponent.animations.Animator;
import com.lefrantguillaume.gameComponent.gameObject.spells.Shield;

/**
 * Created by andres_k on 13/03/2015.
 */
public class Panzer extends Tank {
    public Panzer(Animator tankAnimator, Animator gunAnimator, Animator shotAnimator, Animator spellAnimator) {
        this.type = EnumTanks.PANZER;
        this.tankAnimator = tankAnimator;
        this.shotAnimator = shotAnimator;
        this.gunAnimator = gunAnimator;
        this.tankWeapon = new TankWeapon(10, 20, new Shield(spellAnimator));
        this.tankState = new TankState(2, 10, 0);
    }
}
