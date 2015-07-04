package com.lefrantguillaume.components.gameComponent.gameObject.spells;

import com.lefrantguillaume.components.gameComponent.animations.Animator;
import com.lefrantguillaume.components.gameComponent.gameObject.EnumGameObject;
import com.lefrantguillaume.components.gameComponent.gameObject.obstacles.ObstacleConfigData;

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

    public static Spell copySpell(Spell spell){
        if (spell.getType() == EnumGameObject.SHIELD) {
            return new Shield((Shield)spell);
        } else if (spell.getType() == EnumGameObject.INVISIBILITY) {
            return new Invisibility((Invisibility)spell);
        } else if (spell.getType() == EnumGameObject.TELEPORT) {
            return new Teleport((Teleport)spell);
        }
        return null;
    }
}
