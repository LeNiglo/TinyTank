package com.lefrantguillaume.gameComponent.animations;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

/**
 * Created by andres_k on 13/03/2015.
 */
public class AnimatorFactory {
    public Animator getAnimator(EnumSprites index) throws SlickException {
        Animator animator = new Animator();
        if (index == EnumSprites.PANZER) {
            SpriteSheet spriteSheet = new SpriteSheet("assets/img/game/tank/tigerA.png", 70, 44);
            animator.addAnimation(loadAnimation(spriteSheet, 0, 1, 0, 100));
        }
        if (index == EnumSprites.ROCKET) {
            Animation animation = new Animation();
            Image img1 = new Image("assets/img/game/effect/rocket0.png");
            animation.addFrame(img1, 100);
            Image img2 = new Image("assets/img/game/effect/rocket1.png");
            animation.addFrame(img2, 100);
            Image img3 = new Image("assets/img/game/effect/rocket2.png");
            animation.addFrame(img3, 100);
            animator.addAnimation(animation);
            SpriteSheet spriteSheet = new SpriteSheet("assets/img/game/effect/explosionTiger.png", 54, 54);
            Animation animation2 = loadAnimation(spriteSheet, 0, 5, 0, 100);
            animation2.setLooping(false);
            animator.addAnimation(animation2);
        }
        return animator;
    }

    private Animation loadAnimation(SpriteSheet spriteSheet, int startX, int endX, int y, int speed) {
        Animation animation = new Animation();
        for (int x = startX; x < endX; x++) {
            animation.addFrame(spriteSheet.getSprite(x, y), speed);
        }
        return animation;
    }
}
