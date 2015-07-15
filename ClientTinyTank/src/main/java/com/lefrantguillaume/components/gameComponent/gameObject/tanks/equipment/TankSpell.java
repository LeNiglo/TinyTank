package com.lefrantguillaume.components.gameComponent.gameObject.tanks.equipment;

import com.lefrantguillaume.utils.stockage.Pair;
import com.lefrantguillaume.components.gameComponent.animations.Animator;
import com.lefrantguillaume.components.gameComponent.gameObject.EnumGameObject;
import com.lefrantguillaume.components.gameComponent.gameObject.spells.Spell;
import com.lefrantguillaume.components.gameComponent.gameObject.spells.SpellFactory;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.items.ActivatedTimer;

/**
 * Created by andres_k on 24/03/2015.
 */
public class TankSpell {
    private Spell spell;
    private ActivatedTimer activatedTimer;

    public TankSpell(Spell spell, long cooldown) {
        this.spell = spell;
        this.activatedTimer = new ActivatedTimer(true, false, cooldown);
    }

    public TankSpell(TankSpell tankSpell) {
        this.spell = SpellFactory.copySpell(tankSpell.spell);
        this.activatedTimer = new ActivatedTimer(tankSpell.activatedTimer);
    }

    // FUNCTIONS
    public Object activeCurrentSpell() {
        if (this.activatedTimer.isActivated()) {
            Object result = this.spell.activeSpell();
            if (result != null) {
                this.activatedTimer.setActivated(false);
                this.activatedTimer.startTimer();
                return result;
            }
        }
        return null;
    }

    public void stopCurrentSpell(){
        this.spell.stopSpell();
    }

    public void move(Pair<Float, Float> coords) {
        this.spell.move(coords);
    }

    public void init(TankState tankState){
        this.spell.init(tankState);
    }

    // GETTERS

    public EnumGameObject getType() {
        return this.spell.getType();
    }

    public Animator getAnimator(){
        return this.spell.getAnimator();
    }

    public boolean isActivate(){
        return this.spell.isActive();
    }

    public long getCooldown(){
        return this.activatedTimer.getDelay();
    }

    public boolean isActivated(){
        return this.activatedTimer.isActivated();
    }
}
