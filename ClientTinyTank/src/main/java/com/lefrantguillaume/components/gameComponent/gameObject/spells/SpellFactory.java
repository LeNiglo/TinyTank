package com.lefrantguillaume.components.gameComponent.gameObject.spells;

import com.lefrantguillaume.components.gameComponent.gameObject.EnumGameObject;
import com.lefrantguillaume.components.gameComponent.gameObject.obstacles.ObstacleConfigData;

/**
 * Created by andres_k on 17/06/2015.
 */
public class SpellFactory {
    public static Spell createSpell(EnumGameObject type, ObstacleConfigData obstacleConfigData){
        if (type == EnumGameObject.SHIELD){
            return new Shield(obstacleConfigData);
        }
        return null;
    }
}
