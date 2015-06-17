package com.lefrantguillaume.components.gameComponent.gameObject.spells;

import com.lefrantguillaume.components.gameComponent.animations.Animator;
import com.lefrantguillaume.components.gameComponent.gameObject.EnumGameObject;
import com.lefrantguillaume.components.gameComponent.gameObject.obstacles.ObstacleConfigData;
import com.lefrantguillaume.components.gameComponent.gameObject.tanks.Tank;

/**
 * Created by andres_k on 17/06/2015.
 */
public class SpellFactory {
    public static Spell createSpell(EnumGameObject type, ObstacleConfigData obstacleConfigData, Animator animator) {
        if (type == EnumGameObject.SHIELD) {
            return new Shield(obstacleConfigData);
        } else if (type == EnumGameObject.INVISIBILITY) {
            return new Invisibility(animator);
        } else if (type == EnumGameObject.TELEPORT) {
            return new Teleport(animator);
        }
        return null;
    }
}
