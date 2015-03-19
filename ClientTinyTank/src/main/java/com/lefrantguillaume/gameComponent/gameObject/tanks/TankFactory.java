package com.lefrantguillaume.gameComponent.gameObject.tanks;

import com.lefrantguillaume.gameComponent.animations.AnimatorData;
import com.lefrantguillaume.gameComponent.gameObject.projectiles.EnumShots;
import com.lefrantguillaume.gameComponent.gameObject.spells.EnumSpells;

/**
 * Created by andres_k on 13/03/2015.
 */
public class TankFactory {
    public static Tank createTank(String type, AnimatorData animatorData) {
        Tank tank = null;
        if (type.equals("panzer")) {
            tank = new Panzer(animatorData.getTankAnimator(EnumTanks.PANZER), animatorData.getGunAnimator(EnumTanks.PANZER),
                    animatorData.getShotAnimator(EnumShots.ROCKET), animatorData.getSpellAnimator(EnumSpells.SHIELD));
        }
        return tank;
    }
}
