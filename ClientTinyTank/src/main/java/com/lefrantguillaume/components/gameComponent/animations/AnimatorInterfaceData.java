package com.lefrantguillaume.components.gameComponent.animations;

import com.lefrantguillaume.components.graphicsComponent.graphics.windowInterface.EnumInterfaceElement;
import com.lefrantguillaume.components.graphicsComponent.userInterface.overlay.EnumOverlayElement;
import org.newdawn.slick.SlickException;

import java.util.HashMap;

/**
 * Created by andres_k on 13/03/2015.
 */
public class AnimatorInterfaceData {
    private AnimatorFactory animatorFactory;
    private HashMap<EnumInterfaceElement, Animator> tankStatAnimator;
    private HashMap<EnumInterfaceElement, Animator> tankPreviewAnimator;
    private HashMap<EnumInterfaceElement, Animator> backgroundAnimator;
    private HashMap<EnumInterfaceElement, Animator> buttonAnimator;

    public AnimatorInterfaceData() {
        this.animatorFactory = new AnimatorInterfaceFactory();
        this.tankStatAnimator = new HashMap<>();
        this.tankPreviewAnimator = new HashMap<>();
        this.backgroundAnimator = new HashMap<>();
        this.buttonAnimator = new HashMap<>();
    }

    public void init() throws SlickException {
        this.initTanks();
        this.initBackgrounds();
        this.initButtons();
    }

    public void initTanks() throws SlickException {
        this.addTankStatAnimator(this.animatorFactory.getAnimator(EnumSprites.STAT_TIGER), EnumInterfaceElement.TIGER);
        this.addTankStatAnimator(this.animatorFactory.getAnimator(EnumSprites.STAT_SNIPER), EnumInterfaceElement.SNIPER);
        this.addTankStatAnimator(this.animatorFactory.getAnimator(EnumSprites.STAT_RUSHER), EnumInterfaceElement.RUSHER);
        this.addTankStatAnimator(this.animatorFactory.getAnimator(EnumSprites.STAT_RANK), EnumInterfaceElement.RANK);
        this.addTankPreviewAnimator(this.animatorFactory.getAnimator(EnumSprites.PREVIEW_TIGER), EnumInterfaceElement.TIGER);
        this.addTankPreviewAnimator(this.animatorFactory.getAnimator(EnumSprites.PREVIEW_SNIPER), EnumInterfaceElement.SNIPER);
        this.addTankPreviewAnimator(this.animatorFactory.getAnimator(EnumSprites.PREVIEW_RUSHER), EnumInterfaceElement.RUSHER);
    }

    public void initBackgrounds() throws SlickException {
        this.addBackgroundAnimator(this.animatorFactory.getAnimator(EnumSprites.BACKGROUND), EnumInterfaceElement.BACKGROUND_1);
    }

    public void initButtons() throws SlickException {
        this.addButtonAnimator(this.animatorFactory.getAnimator(EnumSprites.NEXT), EnumInterfaceElement.NEXT);
        this.addButtonAnimator(this.animatorFactory.getAnimator(EnumSprites.PREV), EnumInterfaceElement.PREV);
    }

    public void addTankStatAnimator(Animator tankStatAnimator, EnumInterfaceElement type) {
        this.tankStatAnimator.put(type, tankStatAnimator);
    }

    public void addTankPreviewAnimator(Animator tankPreviewAnimator, EnumInterfaceElement type) {
        this.tankPreviewAnimator.put(type, tankPreviewAnimator);
    }

    public void addBackgroundAnimator(Animator backgroundAnimator, EnumInterfaceElement type) {
        this.backgroundAnimator.put(type, backgroundAnimator);
    }

    public void addButtonAnimator(Animator buttonAnimator, EnumInterfaceElement type) {
        this.buttonAnimator.put(type, buttonAnimator);
    }

    public HashMap<EnumInterfaceElement, Animator> getTankStatAnimators() {
        return this.tankStatAnimator;
    }

    public HashMap<EnumInterfaceElement, Animator> getBackgroundAnimators() {
        return this.backgroundAnimator;
    }

    public HashMap<EnumInterfaceElement, Animator> getButtonAnimators() {
        return this.buttonAnimator;
    }

    public HashMap<EnumInterfaceElement, Animator> getTankPreviewAnimators() {
        return tankPreviewAnimator;
    }

    public Animator getTankStatAnimator(EnumOverlayElement index) {
        return this.tankStatAnimator.get(index);
    }

    public Animator getTankPreviewAnimator(EnumOverlayElement index) {
        return this.tankPreviewAnimator.get(index);
    }

    public Animator getBackgroundAnimator(EnumOverlayElement index) {
        return this.backgroundAnimator.get(index);
    }

    public Animator getButtonAnimator(EnumOverlayElement index) {
        return this.buttonAnimator.get(index);
    }

}

