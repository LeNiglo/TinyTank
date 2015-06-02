package com.lefrantguillaume.components.gameComponent.gameObject.spells;

import com.lefrantguillaume.components.gameComponent.animations.Animator;
import com.lefrantguillaume.components.gameComponent.gameObject.EnumGameObject;

/**
 * Created by andres_k on 18/03/2015.
 */
public class Shield extends Spell {

    public Shield(Animator animator) {
        this.type = EnumGameObject.SHIELD;
        this.animator = animator;
    }

    // FUNCTIONS

    @Override
    public int activeSpell(){
        return 0;
    }

    // GETTERS
    public Animator getAnimator() {
        return this.animator;
    }

    // SETTERS
    public void setAnimator(Animator animator) {
        this.animator = animator;
    }
}
