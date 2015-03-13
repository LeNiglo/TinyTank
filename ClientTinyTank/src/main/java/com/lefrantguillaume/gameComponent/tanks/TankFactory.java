package com.lefrantguillaume.gameComponent.tanks;

import com.lefrantguillaume.gameComponent.animations.Animator;
import com.lefrantguillaume.gameComponent.animations.AnimatorData;

/**
 * Created by andres_k on 13/03/2015.
 */
public class TankFactory {
    public static Tank createTank(String type, AnimatorData animatorData){
        Tank tank = null;
        if (type.equals("panzer")){
            tank = new Panzer(animatorData.getTankAnimator(EnumTanks.PANZER), animatorData.getShotAnimator(EnumShots.ROCKET));
        }
        return tank;
    }
}
