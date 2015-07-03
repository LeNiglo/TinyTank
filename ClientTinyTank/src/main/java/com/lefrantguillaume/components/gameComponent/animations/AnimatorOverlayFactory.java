package com.lefrantguillaume.components.gameComponent.animations;

import com.lefrantguillaume.Utils.tools.Debug;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * Created by andres_k on 13/03/2015.
 */
public class AnimatorOverlayFactory extends AnimatorFactory {
    public Animator getAnimator(EnumSprites index) throws SlickException {
        if (index.getIndex() == EnumSprites.ROUND.getIndex()) {
            return this.getRoundAnimator(index);
        } else if (index.getIndex() == EnumSprites.WALL.getIndex()) {
            return this.getWallAnimator(index);
        } else if (index.getIndex() == EnumSprites.TIGER.getIndex()) {
            return this.getTigerAnimator(index);
        } else if (index.getIndex() == EnumSprites.SNIPER.getIndex()) {
            return this.getSniperAnimator(index);
        } else if (index.getIndex() == EnumSprites.RUSHER.getIndex()) {
            return this.getRusherAnimator(index);
        } else if (index.getIndex() == EnumSprites.MENU.getIndex()) {
            return this.getMenuAnimator(index);
        }
        return null;
    }

    public Animator getRoundAnimator(EnumSprites index) throws SlickException {
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

    public Animator getWallAnimator(EnumSprites index) throws SlickException {
        Animator animator = new Animator();
        if (index == EnumSprites.IRON_WALL) {
            Animation animation = new Animation();
            Image img = new Image("assets/old/img/game/icon/tigerBox.png");
            animation.addFrame(img, 150);
            animation.setLooping(false);
            animator.addAnimation(EnumAnimation.BASIC, animation);
        } else if (index == EnumSprites.PLASMA_WALL) {
            Animation animation = new Animation();
            Image img = new Image("assets/old/img/game/icon/sniperBox.png");
            animation.addFrame(img, 150);
            animation.setLooping(false);
            animator.addAnimation(EnumAnimation.BASIC, animation);
        } else if (index == EnumSprites.MINE) {
            Animation animation = new Animation();
            Image img = new Image("assets/old/img/game/icon/rusherBox.png");
            animation.addFrame(img, 150);
            animation.setLooping(false);
            animator.addAnimation(EnumAnimation.BASIC, animation);
        }

        return animator;
    }

    public Animator getTigerAnimator(EnumSprites index) throws SlickException {
        Animator animator = new Animator();
        if (index == EnumSprites.TIGER_HIT) {
            Animation animation = new Animation();
            Image img = new Image("assets/old/img/game/icon/tigerHit.png");
            animation.addFrame(img, 150);
            animation.setLooping(false);
            animator.addAnimation(EnumAnimation.BASIC, animation);
        } else if (index == EnumSprites.TIGER_SPELL) {
            Animation animation = new Animation();
            Image img = new Image("assets/old/img/game/icon/tigerSpell.png");
            animation.addFrame(img, 150);
            animation.setLooping(false);
            animator.addAnimation(EnumAnimation.BASIC, animation);
        }
        return animator;
    }

    public Animator getSniperAnimator(EnumSprites index) throws SlickException {
        Animator animator = new Animator();
        Debug.debug("add SNIPER ICON ANIMATOR: " + index);
        if (index == EnumSprites.SNIPER_HIT) {
            Animation animation = new Animation();
            Image img = new Image("assets/old/img/game/icon/sniperHit.png");
            animation.addFrame(img, 150);
            animation.setLooping(false);
            animator.addAnimation(EnumAnimation.BASIC, animation);
        } else if (index == EnumSprites.SNIPER_SPELL) {
            Animation animation = new Animation();
            Image img = new Image("assets/old/img/game/icon/sniperSpell.png");
            animation.addFrame(img, 150);
            animation.setLooping(false);
            animator.addAnimation(EnumAnimation.BASIC, animation);
        }
        return animator;
    }

    public Animator getRusherAnimator(EnumSprites index) throws SlickException {
        Animator animator = new Animator();
        if (index == EnumSprites.RUSHER_HIT) {
            Animation animation = new Animation();
            Image img = new Image("assets/old/img/game/icon/rusherHit.png");
            animation.addFrame(img, 150);
            animation.setLooping(false);
            animator.addAnimation(EnumAnimation.BASIC, animation);
        } else if (index == EnumSprites.RUSHER_SPELL) {
            Animation animation = new Animation();
            Image img = new Image("assets/old/img/game/icon/rusherSpell.png");
            animation.addFrame(img, 150);
            animation.setLooping(false);
            animator.addAnimation(EnumAnimation.BASIC, animation);
        }
        return animator;
    }

    public Animator getMenuAnimator(EnumSprites index) throws SlickException {
        Animator animator = new Animator();

        if (index == EnumSprites.EXIT) {
            Animation animation = new Animation();
            Image img = new Image("assets/old/img/overlay/exit.png");
            animation.addFrame(img, 150);
            animation.setLooping(false);
            animator.addAnimation(EnumAnimation.BASIC, animation);
        } else if (index == EnumSprites.SETTINGS) {
            Animation animation = new Animation();
            Image img = new Image("assets/old/img/overlay/settings.png");
            animation.addFrame(img, 150);
            animation.setLooping(false);
            animator.addAnimation(EnumAnimation.BASIC, animation);
        } else if (index == EnumSprites.CONTROLS) {
            Animation animation = new Animation();
            Image img = new Image("assets/old/img/overlay/controls.png");
            animation.addFrame(img, 150);
            animation.setLooping(false);
            animator.addAnimation(EnumAnimation.BASIC, animation);
        } else if (index == EnumSprites.SCREEN) {
            Animation animation = new Animation();
            Image img = new Image("assets/old/img/overlay/screen.png");
            animation.addFrame(img, 150);
            animation.setLooping(false);
            animator.addAnimation(EnumAnimation.BASIC, animation);
        }

        return animator;
    }
}
