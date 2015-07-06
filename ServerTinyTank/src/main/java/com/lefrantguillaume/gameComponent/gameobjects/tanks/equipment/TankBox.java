package com.lefrantguillaume.gameComponent.gameobjects.tanks.equipment;


import com.lefrantguillaume.gameComponent.EnumGameObject;
import com.lefrantguillaume.utils.ActivatedTimer;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by andres_k on 04/07/2015.
 */
public class TankBox {
    private EnumGameObject type;
    private ActivatedTimer activatedTimer;
    private int boxes;
    private long cooldownGenerate;

    public TankBox(EnumGameObject type, int boxes, long cooldownPut, long cooldownGenerate) {
        this.type = type;
        this.boxes = boxes;
        this.cooldownGenerate = cooldownGenerate;
        this.activatedTimer = new ActivatedTimer(true, false, cooldownPut);
    }

    public TankBox(TankBox tankBox) {
        this.type = tankBox.type;
        this.boxes = tankBox.boxes;
        this.cooldownGenerate = tankBox.cooldownGenerate;
        this.activatedTimer = new ActivatedTimer(tankBox.activatedTimer);
    }

    // FUNCTIONS

    public boolean putBox() {
        if (this.boxes > 0 && this.isActivated()) {
            this.boxes -= 1;
            this.activatedTimer.setActivated(false);
            this.activatedTimer.startTimer();
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    boxes += 1;
                }
            }, this.cooldownGenerate);
            return true;
        }
        return false;
    }

    // GETTERS

    public EnumGameObject getType() {
        return this.type;
    }

    public boolean isActivated() {
        return this.activatedTimer.isActivated();
    }

    public int getBoxes() {
        return this.boxes;
    }
}
