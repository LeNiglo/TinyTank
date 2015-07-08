package com.lefrantguillaume.components.gameComponent.gameObject.spells;

import com.lefrantguillaume.utils.stockage.Pair;
import com.lefrantguillaume.components.gameComponent.animations.Animator;
import com.lefrantguillaume.components.gameComponent.gameObject.EnumGameObject;
import com.lefrantguillaume.components.gameComponent.gameObject.tanks.equipment.TankState;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by andres_k on 18/03/2015.
 */
public class Spell {
    protected EnumGameObject type;
    protected Animator animator;
    protected Timer timer = null;
    protected long duration;
    protected boolean isActive;

    // FUNCTIONS
    public Object activeSpell() {
        return 0;
    }

    public void stopSpell(){
        if (timer != null) {
            this.timer.cancel();
            this.deleteSpell();
            this.timer = null;
        }
    }

    protected int deleteSpell() {
        return 0;
    }

    public void move(Pair<Float, Float> coords) {
    }


    public void init(TankState tankState){
    }

    // GETTERS
    public EnumGameObject getType() {
        return this.type;
    }

    public Animator getAnimator() {
        return this.animator;
    }

    public boolean isActive() {
        return this.isActive;
    }

    // CLASSES
    class myTask extends TimerTask {
        @Override
        public void run() {
            deleteSpell();
        }
    }
}
