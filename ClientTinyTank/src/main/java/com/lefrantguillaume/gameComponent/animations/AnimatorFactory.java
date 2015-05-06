package com.lefrantguillaume.gameComponent.animations;

import org.newdawn.slick.Animation;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

/**
 * Created by andres_k on 13/03/2015.
 */
public class AnimatorFactory {
    public Animator getAnimator(EnumSprites index) throws SlickException {
        return null;
    }

    protected Animation loadAnimation(SpriteSheet spriteSheet, int startX, int endX, int startY, int endY, int speed) {
        Animation animation = new Animation();
        for (int y = startY; y < endY; y++) {
            for (int x = startX; x < endX; x++) {
                animation.addFrame(spriteSheet.getSprite(x, y), speed);
            }

        }        return animation;
    }
}
