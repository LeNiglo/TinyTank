package com.lefrantguillaume.components.gameComponent.animations;

import com.lefrantguillaume.components.graphicsComponent.userInterface.overlay.EnumOverlayElement;
import org.newdawn.slick.SlickException;

import java.util.HashMap;

/**
 * Created by andres_k on 13/03/2015.
 */
public class AnimatorOverlayData {
    private AnimatorFactory animatorFactory;
    private HashMap<EnumOverlayElement, Animator> roundAnimator;
    private HashMap<EnumOverlayElement, Animator> iconAnimator;
    private HashMap<EnumOverlayElement, Animator> menuAnimator;


    public AnimatorOverlayData() {
        this.animatorFactory = new AnimatorOverlayFactory();
        this.roundAnimator = new HashMap<>();
        this.iconAnimator = new HashMap<>();
        this.menuAnimator = new HashMap<>();
    }

    public void init() throws SlickException {
        this.initRound();
        this.initIcon();
        this.initMenu();
    }

    public void initRound() throws SlickException {
        this.addRoundAnimator(this.animatorFactory.getAnimator(EnumSprites.NEW_ROUND), EnumOverlayElement.NEW_ROUND);
        this.addRoundAnimator(this.animatorFactory.getAnimator(EnumSprites.END_ROUND), EnumOverlayElement.END_ROUND);
        this.addRoundAnimator(this.animatorFactory.getAnimator(EnumSprites.TIMER), EnumOverlayElement.TIMER);
    }

    public void initIcon() throws SlickException {
        this.addIconAnimator(this.animatorFactory.getAnimator(EnumSprites.TIGER_HIT), EnumOverlayElement.TIGER_HIT);
        this.addIconAnimator(this.animatorFactory.getAnimator(EnumSprites.TIGER_SPELL), EnumOverlayElement.TIGER_SPELL);
        this.addIconAnimator(this.animatorFactory.getAnimator(EnumSprites.IRON_WALL), EnumOverlayElement.TIGER_BOX);
        this.addIconAnimator(this.animatorFactory.getAnimator(EnumSprites.SNIPER_HIT), EnumOverlayElement.SNIPER_HIT);
        this.addIconAnimator(this.animatorFactory.getAnimator(EnumSprites.SNIPER_SPELL), EnumOverlayElement.SNIPER_SPELL);
        this.addIconAnimator(this.animatorFactory.getAnimator(EnumSprites.PLASMA_WALL), EnumOverlayElement.SNIPER_BOX);
        this.addIconAnimator(this.animatorFactory.getAnimator(EnumSprites.RUSHER_HIT), EnumOverlayElement.RUSHER_HIT);
        this.addIconAnimator(this.animatorFactory.getAnimator(EnumSprites.RUSHER_SPELL), EnumOverlayElement.RUSHER_SPELL);
        this.addIconAnimator(this.animatorFactory.getAnimator(EnumSprites.MINE), EnumOverlayElement.RUSHER_BOX);
    }

    public void initMenu() throws SlickException {
        this.addMenuAnimator(this.animatorFactory.getAnimator(EnumSprites.EXIT), EnumOverlayElement.EXIT);
        this.addMenuAnimator(this.animatorFactory.getAnimator(EnumSprites.SETTINGS), EnumOverlayElement.SETTINGS);
        this.addMenuAnimator(this.animatorFactory.getAnimator(EnumSprites.CONTROLS), EnumOverlayElement.CONTROLS);
        this.addMenuAnimator(this.animatorFactory.getAnimator(EnumSprites.SCREEN), EnumOverlayElement.SCREEN);
    }

    public void addRoundAnimator(Animator roundAnimator, EnumOverlayElement type) {
        this.roundAnimator.put(type, roundAnimator);
    }

    public void addIconAnimator(Animator iconAnimator, EnumOverlayElement type) {
        this.iconAnimator.put(type, iconAnimator);
    }

    public void addMenuAnimator(Animator menuAnimator, EnumOverlayElement type) {
        this.menuAnimator.put(type, menuAnimator);
    }

    // GETTERS
    public Animator getRoundAnimator(EnumOverlayElement index) {
        return this.roundAnimator.get(index);
    }

    public Animator getIconAnimator(EnumOverlayElement index) {
        return this.iconAnimator.get(index);
    }

    public Animator getMenuAnimator(EnumOverlayElement index) {
        return this.menuAnimator.get(index);
    }
}

