package com.lefrantguillaume.components.gameComponent.gameObject.spells;

import com.lefrantguillaume.Utils.tools.Debug;
import com.lefrantguillaume.components.gameComponent.animations.Animator;
import com.lefrantguillaume.components.gameComponent.gameObject.EnumGameObject;
import com.lefrantguillaume.components.gameComponent.gameObject.tanks.equipment.TankState;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

/**
 * Created by andres_k on 18/03/2015.
 */
public class Teleport extends Spell {
    private TankState tankState;
    List<EnumGameObject> ignored;

    public Teleport(Animator animator) {
        this.ignored = new ArrayList<>();
        this.ignored.add(EnumGameObject.TIGER);
        this.ignored.add(EnumGameObject.SNIPER);
        this.ignored.add(EnumGameObject.RUSHER);
        this.ignored.add(EnumGameObject.IRON_WALL);
        this.ignored.add(EnumGameObject.PLASMA_WALL);
        this.ignored.add(EnumGameObject.MINE);

        this.type = EnumGameObject.TELEPORT;
        this.tankState = null;
        this.animator = new Animator(animator);
        this.duration = 100;
        this.isActive = false;
    }

    public Teleport(Teleport teleport) {
        this.ignored = new ArrayList<>();
        this.ignored.addAll(teleport.ignored);

        this.type = EnumGameObject.TELEPORT;
        this.tankState = null;
        this.animator = new Animator(teleport.animator);
        this.duration = teleport.duration;
        this.isActive = teleport.isActive;
    }

    // FUNCTIONS

    @Override
    public Object activeSpell() {
        if (tankState != null && this.isActive == false) {
            Debug.debug("ACTIVATED !");
            this.isActive = true;
            this.tankState.setBoostEffect(300);
            this.tankState.myNotify(this.ignored);
            this.timer = new Timer();
            this.timer.schedule(new myTask(), this.duration);
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected int deleteSpell() {
        if (tankState != null) {
            Debug.debug("DESACTIVATE");
            this.isActive = false;
            this.tankState.setBoostEffect(0);
            this.tankState.myNotify(new ArrayList<EnumGameObject>());
        }
        return 0;
    }

    @Override
    public void init(TankState tankState) {
        this.tankState = tankState;
    }
}
