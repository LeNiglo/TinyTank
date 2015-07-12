package com.lefrantguillaume.components.gameComponent.gameObject.spells;

import com.lefrantguillaume.utils.tools.ConsoleWriter;
import com.lefrantguillaume.components.gameComponent.animations.Animator;
import com.lefrantguillaume.components.gameComponent.gameObject.EnumGameObject;
import com.lefrantguillaume.components.gameComponent.gameObject.tanks.equipment.TankState;
import org.newdawn.slick.Color;

import java.util.Timer;

/**
 * Created by andres_k on 18/03/2015.
 */
public class Invisibility extends Spell {
    TankState tankState;

    public Invisibility(Animator animator) {
        this.type = EnumGameObject.INVISIBILITY;
        this.tankState = null;
        this.animator = new Animator(animator);
        this.duration = 10000;
        this.isActive = false;
    }

    public Invisibility(Invisibility invisibility) {
        this.type = EnumGameObject.INVISIBILITY;
        this.tankState = null;
        this.animator = new Animator(invisibility.animator);
        this.duration = invisibility.duration;
        this.isActive = invisibility.isActive;
    }

    // FUNCTIONS

    @Override
    public Object activeSpell(){
        if (this.tankState != null && this.isActive == false) {
            this.isActive = true;
            Color filter;
            if (tankState.getCurrentTeam().getValue().contains("Enemy")) {
                filter = new Color(1f, 1f, 1f, 0f);
            } else {
                filter = new Color(0.5f, 1f, 1f, 0.7f);
            }
            ConsoleWriter.debug("filtre: " + filter);
            this.tankState.setFilter(filter);
            this.timer = new Timer();
            this.timer.schedule(new myTask(), this.duration);
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected int deleteSpell(){
        this.isActive = false;
        this.tankState.setFilter(new Color(1f, 1f, 1f));
        return 0;
    }

    @Override
    public void init(TankState tankState){
        this.tankState = tankState;
    }
}
