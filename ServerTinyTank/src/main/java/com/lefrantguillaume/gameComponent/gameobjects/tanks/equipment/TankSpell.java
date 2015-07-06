package com.lefrantguillaume.gameComponent.gameobjects.tanks.equipment;

import com.lefrantguillaume.gameComponent.EnumGameObject;
import com.lefrantguillaume.utils.ActivatedTimer;

/**
 * Created by andres_k on 24/03/2015.
 */
public class TankSpell {
    private ActivatedTimer activatedTimer;
    private EnumGameObject type;

    public TankSpell(EnumGameObject type, long cooldown) {
        this.type= type;
        this.activatedTimer = new ActivatedTimer(true, false, cooldown);
    }

    public TankSpell(TankSpell tankSpell) {
        this.type = tankSpell.type;
        this.activatedTimer = new ActivatedTimer(tankSpell.activatedTimer);
    }

    // FUNCTIONS
    public boolean activeCurrentSpell() {
        if (this.activatedTimer.isActivated()) {
            this.activatedTimer.setActivated(false);
            this.activatedTimer.startTimer();
            return true;
        }
        return false;
    }

    // GETTERS

    public EnumGameObject getType(){
        return this.type;
    }

    public long getCooldown() {
        return this.activatedTimer.getDelay();
    }

    public boolean isActivated() {
        return this.activatedTimer.isActivated();
    }
}
