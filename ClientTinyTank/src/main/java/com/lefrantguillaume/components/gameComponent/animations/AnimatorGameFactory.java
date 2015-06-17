package com.lefrantguillaume.components.gameComponent.animations;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

/**
 * Created by andres_k on 13/03/2015.
 */
public class AnimatorGameFactory extends AnimatorFactory {

    public Animator getAnimator(EnumSprites index) throws SlickException {
        Animator animator = new Animator();

        if (index == EnumSprites.MAP) {
            this.mapLoad(animator);
        }
        if (index.getIndex() == EnumSprites.TIGER.getIndex()) {
            this.tigerAnimator(animator, index);
        }
        if (index.getIndex() == EnumSprites.SNIPER.getIndex()) {
            this.sniperAnimator(animator, index);
        }
        if (index.getIndex() == EnumSprites.RUSHER.getIndex()) {
            this.rusherAnimator(animator, index);
        }
        if (index.getIndex() == EnumSprites.WALL.getIndex()) {
            this.wallAnimator(animator, index);
        }
        return animator;
    }

    private void wallAnimator(Animator animator, EnumSprites index) throws SlickException {
        if (index == EnumSprites.IRON_WALL) {
            Animation animation1 = new Animation();
            Image img1 = new Image("assets/img/game/box/box1.png");
            animation1.addFrame(img1, 150);
            Animation animation2 = new Animation();
            Image img2 = new Image("assets/img/game/box/box2.png");
            animation2.addFrame(img2, 150);
            Animation animation3 = new Animation();
            Image img3 = new Image("assets/img/game/box/box3.png");
            animation3.addFrame(img3, 150);
            animation1.setLooping(false);
            animation2.setLooping(false);
            animation3.setLooping(false);
            animator.addAnimation(EnumAnimation.BASIC, animation1);
            animator.addAnimation(EnumAnimation.BASIC, animation2);
            animator.addAnimation(EnumAnimation.BASIC, animation3);

            SpriteSheet spriteSheet2 = new SpriteSheet("assets/img/game/box/boomBox.png", 88, 63);
            Animation animation4 = this.loadAnimation(spriteSheet2, 0, 3, 0, 1, 200);
            animation4.setLooping(false);
            animator.addAnimation(EnumAnimation.EXPLODE, animation4);
        }
    }

    private void tigerAnimator(Animator animator, EnumSprites index) throws SlickException {
        if (index == EnumSprites.TIGER_BODY) {
            SpriteSheet spriteSheet = new SpriteSheet("assets/img/game/tank/tigerA_1.png", 49, 44);
            animator.addAnimation(EnumAnimation.BASIC, this.loadAnimation(spriteSheet, 0, 1, 0, 1, 200));
            SpriteSheet spriteSheet2 = new SpriteSheet("assets/img/game/effect/explosionTank.png", 147, 145);
            Animation animation = this.loadAnimation(spriteSheet2, 0, 2, 0, 2, 200);
            animation.setLooping(false);
            animator.addAnimation(EnumAnimation.EXPLODE, animation);
        } else if (index == EnumSprites.TIGER_TOP) {
            SpriteSheet spriteSheet = new SpriteSheet("assets/img/game/tank/tigerA_2.png", 44, 24);
            animator.addAnimation(EnumAnimation.BASIC, loadAnimation(spriteSheet, 0, 1, 0, 1, 200));
        } else if (index == EnumSprites.TIGER_HIT) {
            Animation animation = new Animation();
            Image img1 = new Image("assets/img/game/effect/rocket0.png");
            animation.addFrame(img1, 150);
            Image img2 = new Image("assets/img/game/effect/rocket1.png");
            animation.addFrame(img2, 150);
            Image img3 = new Image("assets/img/game/effect/rocket2.png");
            animation.addFrame(img3, 150);
            animator.addAnimation(EnumAnimation.BASIC, animation);
            SpriteSheet spriteSheet = new SpriteSheet("assets/img/game/effect/explosionTiger.png", 54, 54);
            Animation animation2 = this.loadAnimation(spriteSheet, 0, 5, 0, 1, 150);
            animation2.setLooping(false);
            animator.addAnimation(EnumAnimation.EXPLODE, animation2);
        } else if (index == EnumSprites.TIGER_SPELL) {
            SpriteSheet spriteSheet = new SpriteSheet("assets/img/game/effect/tiger_shield.png", 90, 90);
            animator.addAnimation(EnumAnimation.BASIC, loadAnimation(spriteSheet, 0, 1, 0, 1, 200));
        } else if (index == EnumSprites.TIGER_BODY_ENEMY) {
            SpriteSheet spriteSheet = new SpriteSheet("assets/img/game/tank/tigerE_1.png", 49, 44);
            animator.addAnimation(EnumAnimation.BASIC, this.loadAnimation(spriteSheet, 0, 1, 0, 1, 200));
            SpriteSheet spriteSheet2 = new SpriteSheet("assets/img/game/effect/explosionTank.png", 147, 145);
            Animation animation = this.loadAnimation(spriteSheet2, 0, 2, 0, 2, 200);
            animation.setLooping(false);
            animator.addAnimation(EnumAnimation.EXPLODE, animation);
        } else if (index == EnumSprites.TIGER_TOP_ENEMY) {
            SpriteSheet spriteSheet = new SpriteSheet("assets/img/game/tank/tigerE_2.png", 44, 24);
            animator.addAnimation(EnumAnimation.BASIC, loadAnimation(spriteSheet, 0, 1, 0, 1, 200));
        }
    }

    private void sniperAnimator(Animator animator, EnumSprites index) throws SlickException {
        if (index == EnumSprites.SNIPER_BODY) {
            SpriteSheet spriteSheet = new SpriteSheet("assets/img/game/tank/sniperA_1.png", 80, 48);
            animator.addAnimation(EnumAnimation.BASIC, this.loadAnimation(spriteSheet, 0, 1, 0, 1, 200));
            SpriteSheet spriteSheet2 = new SpriteSheet("assets/img/game/effect/explosionTank.png", 147, 145);
            Animation animation = this.loadAnimation(spriteSheet2, 0, 2, 0, 2, 200);
            animation.setLooping(false);
            animator.addAnimation(EnumAnimation.EXPLODE, animation);
        } else if (index == EnumSprites.SNIPER_TOP) {
            SpriteSheet spriteSheet = new SpriteSheet("assets/img/game/tank/sniperA_2.png", 80, 48);
            animator.addAnimation(EnumAnimation.BASIC, loadAnimation(spriteSheet, 0, 1, 0, 1, 200));
        } else if (index == EnumSprites.SNIPER_HIT) {
            SpriteSheet spriteSheet1 = new SpriteSheet("assets/img/game/effect/snipeHit0.png", 22, 22);
            animator.addAnimation(EnumAnimation.BASIC, loadAnimation(spriteSheet1, 0, 1, 0, 1, 200));
            SpriteSheet spriteSheet2 = new SpriteSheet("assets/img/game/effect/explosionSniper.png", 47, 47);
            Animation animation2 = this.loadAnimation(spriteSheet2, 0, 4, 0, 1, 150);
            animation2.setLooping(false);
            animator.addAnimation(EnumAnimation.EXPLODE, animation2);
        } else if (index == EnumSprites.SNIPER_SPELL) {
            SpriteSheet spriteSheet = new SpriteSheet("assets/img/game/effect/snipe_fufu.png", 119, 100);
            Animation animation = loadAnimation(spriteSheet, 0, 4, 0, 1, 200);
            animation.setLooping(false);
            animator.addAnimation(EnumAnimation.BASIC, animation);
        }
        else if (index == EnumSprites.SNIPER_BODY_ENEMY) {
            SpriteSheet spriteSheet = new SpriteSheet("assets/img/game/tank/sniperE_1.png", 80, 48);
            animator.addAnimation(EnumAnimation.BASIC, this.loadAnimation(spriteSheet, 0, 1, 0, 1, 200));
            SpriteSheet spriteSheet2 = new SpriteSheet("assets/img/game/effect/explosionTank.png", 147, 145);
            Animation animation = this.loadAnimation(spriteSheet2, 0, 2, 0, 2, 200);
            animation.setLooping(false);
            animator.addAnimation(EnumAnimation.EXPLODE, animation);
        } else if (index == EnumSprites.SNIPER_TOP_ENEMY) {
            SpriteSheet spriteSheet = new SpriteSheet("assets/img/game/tank/sniperE_2.png", 80, 48);
            animator.addAnimation(EnumAnimation.BASIC, loadAnimation(spriteSheet, 0, 1, 0, 1, 200));
        }
    }

    private void rusherAnimator(Animator animator, EnumSprites index) throws SlickException {
        if (index == EnumSprites.RUSHER_BODY) {
            SpriteSheet spriteSheet = new SpriteSheet("assets/img/game/tank/rusherA_1.png", 50, 50);
            animator.addAnimation(EnumAnimation.BASIC, this.loadAnimation(spriteSheet, 0, 1, 0, 1, 200));
            SpriteSheet spriteSheet2 = new SpriteSheet("assets/img/game/effect/explosionTank.png", 147, 145);
            Animation animation = this.loadAnimation(spriteSheet2, 0, 2, 0, 2, 200);
            animation.setLooping(false);
            animator.addAnimation(EnumAnimation.EXPLODE, animation);
        } else if (index == EnumSprites.RUSHER_TOP) {
            SpriteSheet spriteSheet = new SpriteSheet("assets/img/game/tank/rusherA_2.png", 36, 21);
            animator.addAnimation(EnumAnimation.BASIC, loadAnimation(spriteSheet, 0, 1, 0, 1, 200));
        } else if (index == EnumSprites.RUSHER_HIT) {
            Animation animation = new Animation();
            Image img1 = new Image("assets/img/game/effect/bullet0.png");
            animation.addFrame(img1, 300);
            Image img2 = new Image("assets/img/game/effect/bullet1.png");
            animation.addFrame(img2, 300);
            Image img3 = new Image("assets/img/game/effect/bullet2.png");
            animation.addFrame(img3, 300);
            animator.addAnimation(EnumAnimation.BASIC, animation);
            SpriteSheet spriteSheet = new SpriteSheet("assets/img/game/effect/explosionRusher.png", 25, 29);
            Animation animation2 = this.loadAnimation(spriteSheet, 0, 3, 0, 1, 150);
            animation2.setLooping(false);
            animator.addAnimation(EnumAnimation.EXPLODE, animation2);
        } else if (index == EnumSprites.RUSHER_SPELL) {
            SpriteSheet spriteSheet = new SpriteSheet("assets/img/game/effect/rusher_circle.png", 200, 200);
            animator.addAnimation(EnumAnimation.BASIC, loadAnimation(spriteSheet, 0, 3, 0, 1, 200));
        } else if (index == EnumSprites.RUSHER_BODY_ENEMY) {
            SpriteSheet spriteSheet = new SpriteSheet("assets/img/game/tank/rusherE_1.png", 50, 50);
            animator.addAnimation(EnumAnimation.BASIC, this.loadAnimation(spriteSheet, 0, 1, 0, 1, 200));
            SpriteSheet spriteSheet2 = new SpriteSheet("assets/img/game/effect/explosionTank.png", 147, 145);
            Animation animation = this.loadAnimation(spriteSheet2, 0, 2, 0, 2, 200);
            animation.setLooping(false);
            animator.addAnimation(EnumAnimation.EXPLODE, animation);
        } else if (index == EnumSprites.RUSHER_TOP_ENEMY) {
            SpriteSheet spriteSheet = new SpriteSheet("assets/img/game/tank/rusherE_2.png", 36, 21);
            animator.addAnimation(EnumAnimation.BASIC, loadAnimation(spriteSheet, 0, 1, 0, 1, 200));
        }
    }

    private void mapLoad(Animator animator) throws SlickException {
        SpriteSheet spriteSheet = new SpriteSheet("assets/img/game/game_bg.png", 1280, 768);
        animator.addAnimation(EnumAnimation.BASIC, this.loadAnimation(spriteSheet, 0, 1, 0, 1, 200));
    }
}
