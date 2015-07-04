package com.lefrantguillaume.components.gameComponent.gameObject.tanks.equipment;

import com.lefrantguillaume.components.gameComponent.gameObject.EnumGameObject;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.items.ActivatedTimer;

/**
 * Created by andres_k on 04/07/2015.
 */
public class TankBox {
    private EnumGameObject box;
    private ActivatedTimer activatedTimer;

    public TankBox(EnumGameObject box, long delay) {
        this.box = box;
        this.activatedTimer = new ActivatedTimer(true, false, delay);
    }

    public TankBox(TankBox tankBox) {
        this.box = tankBox.box;
        this.activatedTimer = new ActivatedTimer(tankBox.activatedTimer);
    }

    // GETTERS

    public EnumGameObject getBox() {
        return this.box;
    }

    public long getCooldown() {
        return this.activatedTimer.getDelay();
    }

    public boolean isActivated() {
        return this.activatedTimer.isActivated();
    }

}
