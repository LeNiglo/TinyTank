package com.lefrantguillaume.components.gameComponent.gameObject.tanks.equipment;

import com.lefrantguillaume.Utils.stockage.Pair;
import com.lefrantguillaume.components.gameComponent.gameObject.spells.Spell;

/**
 * Created by andres_k on 24/03/2015.
 */
public class TankSpell {
    private Spell spell;

    public TankSpell(Spell spell){
        this.spell = spell;
    }

    public TankSpell(TankSpell tankSpell){
        this.spell = tankSpell.spell;
    }

    // FUNCTIONS
    public Object activeCurrentSpell(){
        return this.spell.activeSpell();
    }

    public void move(Pair<Float, Float> coords){
        this.spell.move(coords);
    }
}
