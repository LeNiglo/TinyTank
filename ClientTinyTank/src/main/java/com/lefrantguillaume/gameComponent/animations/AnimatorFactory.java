package com.lefrantguillaume.gameComponent.animations;

import com.lefrantguillaume.gameComponent.gameObject.tanks.EnumTanks;
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

        if (index == EnumSprites.MAP){
            this.mapLoad(animator);
        }
        if (index.getId() == EnumTanks.PANZER.getValue()){
            this.panzerAnimator(animator, index);
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

    private void panzerAnimator(Animator animator, EnumSprites index) throws SlickException {
        if (index == EnumSprites.PANZER_BODY) {
            SpriteSheet spriteSheet = new SpriteSheet("assets/img/game/tank/tigerA_1.png", 70, 44);
            animator.addAnimation(this.loadAnimation(spriteSheet, 0, 1, 0, 200));
        }
        else if (index == EnumSprites.PANZER_ROCKET) {
            Animation animation = new Animation();
            Image img1 = new Image("assets/img/game/effect/rocket0.png");
            animation.addFrame(img1, 300);
            Image img2 = new Image("assets/img/game/effect/rocket1.png");
            animation.addFrame(img2, 300);
            Image img3 = new Image("assets/img/game/effect/rocket2.png");
            animation.addFrame(img3, 300);
            animator.addAnimation(animation);
            SpriteSheet spriteSheet = new SpriteSheet("assets/img/game/effect/explosionTiger.png", 54, 54);
            Animation animation2 = this.loadAnimation(spriteSheet, 0, 5, 0, 150);
            animation2.setLooping(false);
            animator.addAnimation(animation2);
        }
        else if (index == EnumSprites.PANZER_GUN){
            SpriteSheet spriteSheet = new SpriteSheet("assets/img/game/tank/tigerA_2.png", 70, 44);
            animator.addAnimation(loadAnimation(spriteSheet, 0, 1, 0, 200));
        }
        else if (index == EnumSprites.PANZER_SPELL){
            SpriteSheet spriteSheet = new SpriteSheet("assets/img/game/effect/tiger_shield.png", 90, 90);
            animator.addAnimation(loadAnimation(spriteSheet, 0, 1, 0, 200));
        }
    }

    private void mapLoad(Animator animator) throws SlickException {
        SpriteSheet spriteSheet = new SpriteSheet("assets/img/game/game_bg.png", 1280, 768);
        animator.addAnimation(this.loadAnimation(spriteSheet, 0, 1, 0, 200));
    }
}
