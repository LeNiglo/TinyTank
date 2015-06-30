package com.lefrantguillaume.components.gameComponent.animations;

import com.lefrantguillaume.components.graphicsComponent.userInterface.elements.EnumOverlayElement;
import org.newdawn.slick.SlickException;

import java.util.HashMap;

/**
 * Created by andres_k on 13/03/2015.
 */
public class AnimatorOverlayData {
    private AnimatorFactory animatorFactory;
    private HashMap<EnumOverlayElement, Animator> roundAnimator;

    public AnimatorOverlayData() {
        this.animatorFactory = new AnimatorOverlayFactory();
        this.roundAnimator = new HashMap<>();
    }

    public void init() throws SlickException {
        this.initRound();
    }

    public void initRound() throws SlickException {
        this.addRoundAnimator(this.animatorFactory.getAnimator(EnumSprites.NEW_ROUND), EnumOverlayElement.NEW_ROUND);
        this.addRoundAnimator(this.animatorFactory.getAnimator(EnumSprites.STATE), EnumOverlayElement.STATE);
    }

    public void addRoundAnimator(Animator roundAnimator, EnumOverlayElement type) {
        this.roundAnimator.put(type, roundAnimator);
    }

    public Animator getRoundAnimator(EnumOverlayElement index) {
        return this.roundAnimator.get(index);
    }

}

