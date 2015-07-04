package com.lefrantguillaume.components.gameComponent.gameObject.tanks.equipment;

import com.lefrantguillaume.Utils.stockage.Pair;
import com.lefrantguillaume.components.gameComponent.animations.Animator;
import com.lefrantguillaume.components.gameComponent.gameObject.EnumGameObject;
import com.lefrantguillaume.components.gameComponent.gameObject.spells.Spell;
import com.lefrantguillaume.components.gameComponent.gameObject.spells.SpellFactory;

/**
 * Created by andres_k on 24/03/2015.
 */
public class TankSpell {
    private Spell spell;

    public TankSpell(Spell spell) {
        this.spell = spell;
    }

    public TankSpell(TankSpell tankSpell) {
        this.spell = SpellFactory.copySpell(tankSpell.spell);
    }

    // FUNCTIONS
    public Object activeCurrentSpell() {
        return this.spell.activeSpell();
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
}
