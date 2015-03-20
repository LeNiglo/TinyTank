package com.lefrantguillaume.gameComponent.gameObject.tanks;

import com.lefrantguillaume.gameComponent.animations.AnimatorGameData;
import com.lefrantguillaume.gameComponent.gameObject.projectiles.EnumShots;
import com.lefrantguillaume.gameComponent.gameObject.spells.EnumSpells;

/**
 * Created by andres_k on 13/03/2015.
 */
public class TankFactory {
    public static Tank createTank(EnumTanks type, AnimatorGameData animatorGameData) {
        Tank tank = null;
        if (type == EnumTanks.TIGER) {
            tank = new Tiger(animatorGameData.getTankAnimator(EnumTanks.TIGER), animatorGameData.getGunAnimator(EnumTanks.TIGER),
                    animatorGameData.getShotAnimator(EnumShots.ROCKET), animatorGameData.getSpellAnimator(EnumSpells.SHIELD));
        }
        if (type == EnumTanks.SNIPER) {
            tank = new Sniper(animatorGameData.getTankAnimator(EnumTanks.SNIPER), animatorGameData.getGunAnimator(EnumTanks.SNIPER),
                    animatorGameData.getShotAnimator(EnumShots.LASER), animatorGameData.getSpellAnimator(EnumSpells.INVISIBILITY));
        }
        if (type == EnumTanks.RUSHER) {
            tank = new Rusher(animatorGameData.getTankAnimator(EnumTanks.RUSHER), animatorGameData.getGunAnimator(EnumTanks.RUSHER),
                    animatorGameData.getShotAnimator(EnumShots.MACHINE_GUN), animatorGameData.getSpellAnimator(EnumSpells.TELEPORT));
        }
        return tank;
    }
}
