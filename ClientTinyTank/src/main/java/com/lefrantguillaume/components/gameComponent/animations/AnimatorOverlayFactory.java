package com.lefrantguillaume.components.gameComponent.animations;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * Created by andres_k on 13/03/2015.
 */
public class AnimatorOverlayFactory extends AnimatorFactory {
    public Animator getAnimator(EnumSprites index) throws SlickException {
        Animator animator = new Animator();

        if (index == EnumSprites.NEW_ROUND) {
            Animation animation = new Animation();
            Image img = new Image("assets/old/img/overlay/newRound.png");
            animation.addFrame(img, 150);
            animation.setLooping(false);
            animator.addAnimation(EnumAnimation.BASIC, animation);
        } else if (index == EnumSprites.STATE) {
            Animation animation = new Animation();
            for (int i = 4; i > 0; --i) {
                Image img = new Image("assets/old/img/overlay/roundCounter" + String.valueOf(i) + ".png");
                animation.addFrame(img, 1000);
            }
            Image img = new Image("assets/old/img/overlay/roundGo.png");
            animation.addFrame(img, 1000);
            animation.setLooping(false);
            animator.addAnimation(EnumAnimation.BASIC, animation);
        }
        return animator;
    }
}
