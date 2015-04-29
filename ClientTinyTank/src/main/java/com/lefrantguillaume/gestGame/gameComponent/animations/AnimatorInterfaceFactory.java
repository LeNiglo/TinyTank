package com.lefrantguillaume.gestGame.gameComponent.animations;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

/**
 * Created by andres_k on 13/03/2015.
 */
public class AnimatorInterfaceFactory extends AnimatorFactory {
    public Animator getAnimator(EnumSprites index) throws SlickException {
        Animator animator = new Animator();

        if (index == EnumSprites.BACKGROUND) {
            SpriteSheet spriteSheet = new SpriteSheet("assets/img/interface/log_back.png", 1280, 768);
            animator.addAnimation(this.loadAnimation(spriteSheet, 0, 1, 0, 1, 200));
        } else if (index == EnumSprites.BUTTONS) {
            SpriteSheet spriteSheet = new SpriteSheet("assets/img/interface/prev.png", 150, 150);
            animator.addAnimation(this.loadAnimation(spriteSheet, 0, 1, 0, 1, 200));
            spriteSheet = new SpriteSheet("assets/img/interface/next.png", 150, 150);
            animator.addAnimation(this.loadAnimation(spriteSheet, 0, 1, 0, 1, 200));
        } else if (index == EnumSprites.STAT_RANK) {
            SpriteSheet spriteSheet = new SpriteSheet("assets/img/interface/rank.png", 320, 200);
            animator.addAnimation(this.loadAnimation(spriteSheet, 0, 1, 0, 1, 200));
        } else if (index == EnumSprites.STAT_TIGER) {
            SpriteSheet spriteSheet = new SpriteSheet("assets/img/interface/tiger_stat.png", 320, 200);
            animator.addAnimation(this.loadAnimation(spriteSheet, 0, 1, 0, 1, 200));
        } else if (index == EnumSprites.STAT_SNIPER) {
            SpriteSheet spriteSheet = new SpriteSheet("assets/img/interface/snipe_stat.png", 320, 200);
            animator.addAnimation(this.loadAnimation(spriteSheet, 0, 1, 0, 1, 200));
        } else if (index == EnumSprites.STAT_RUSHER) {
            SpriteSheet spriteSheet = new SpriteSheet("assets/img/interface/rusher_stat.png", 320, 200);
            animator.addAnimation(this.loadAnimation(spriteSheet, 0, 1, 0, 1, 200));
        } else if (index == EnumSprites.PREVIEW_TIGER) {
            SpriteSheet spriteSheet = new SpriteSheet("assets/img/game/tank/tigerA.png", 77, 44);
            animator.addAnimation(this.loadAnimation(spriteSheet, 0, 1, 0, 1, 200));
        } else if (index == EnumSprites.PREVIEW_SNIPER) {
            SpriteSheet spriteSheet = new SpriteSheet("assets/img/game/tank/sniperA.png", 80, 48);
            animator.addAnimation(this.loadAnimation(spriteSheet, 0, 1, 0, 1, 200));
        } else if (index == EnumSprites.PREVIEW_RUSHER) {
            SpriteSheet spriteSheet = new SpriteSheet("assets/img/game/tank/rusherA.png", 74, 50);
            animator.addAnimation(this.loadAnimation(spriteSheet, 0, 1, 0, 1, 200));
        }

        return animator;
    }
}
