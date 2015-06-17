package com.lefrantguillaume.components.gameComponent.animations;

import com.lefrantguillaume.components.graphicsComponent.userInterface.EnumInterfaceComponent;
import org.newdawn.slick.SlickException;

import java.util.HashMap;

/**
 * Created by andres_k on 13/03/2015.
 */
public class AnimatorInterfaceData {
    private AnimatorFactory animatorFactory;
    private HashMap<EnumInterfaceComponent, Animator> tankStatAnimator;
    private HashMap<EnumInterfaceComponent, Animator> tankPreviewAnimator;
    private HashMap<EnumInterfaceComponent, Animator> backgroundAnimator;
    private HashMap<EnumInterfaceComponent, Animator> buttonAnimator;

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
//        this.initButtons();
    }

    public void initTanks() throws SlickException {
        this.addTankStatAnimator(this.animatorFactory.getAnimator(EnumSprites.STAT_TIGER), EnumInterfaceComponent.TIGER);
        this.addTankStatAnimator(this.animatorFactory.getAnimator(EnumSprites.STAT_SNIPER), EnumInterfaceComponent.SNIPER);
        this.addTankStatAnimator(this.animatorFactory.getAnimator(EnumSprites.STAT_RUSHER), EnumInterfaceComponent.RUSHER);
        this.addTankStatAnimator(this.animatorFactory.getAnimator(EnumSprites.STAT_RANK), EnumInterfaceComponent.RANK);
        this.addTankPreviewAnimator(this.animatorFactory.getAnimator(EnumSprites.PREVIEW_TIGER), EnumInterfaceComponent.TIGER);
        this.addTankPreviewAnimator(this.animatorFactory.getAnimator(EnumSprites.PREVIEW_SNIPER), EnumInterfaceComponent.SNIPER);
        this.addTankPreviewAnimator(this.animatorFactory.getAnimator(EnumSprites.PREVIEW_RUSHER), EnumInterfaceComponent.RUSHER);
    }

    public void initBackgrounds() throws SlickException {
        this.addBackgroundAnimator(this.animatorFactory.getAnimator(EnumSprites.BACKGROUND), EnumInterfaceComponent.BACKGROUND_1);
    }

    public void initButtons() throws SlickException {
        this.addButtonAnimator(this.animatorFactory.getAnimator(EnumSprites.NEXT), EnumInterfaceComponent.NEXT);
        this.addButtonAnimator(this.animatorFactory.getAnimator(EnumSprites.PREV), EnumInterfaceComponent.PREV);
    }

    public void addTankStatAnimator(Animator tankStatAnimator, EnumInterfaceComponent type) {
        this.tankStatAnimator.put(type, tankStatAnimator);
    }

    public void addTankPreviewAnimator(Animator tankPreviewAnimator, EnumInterfaceComponent type) {
        this.tankPreviewAnimator.put(type, tankPreviewAnimator);
    }

    public void addBackgroundAnimator(Animator backgroundAnimator, EnumInterfaceComponent type) {
        this.backgroundAnimator.put(type, backgroundAnimator);
    }

    public void addButtonAnimator(Animator buttonAnimator, EnumInterfaceComponent type) {
        this.buttonAnimator.put(type, buttonAnimator);
    }

    public HashMap<EnumInterfaceComponent, Animator> getTankStatAnimators() {
        return this.tankStatAnimator;
    }

    public HashMap<EnumInterfaceComponent, Animator> getBackgroundAnimators() {
        return this.backgroundAnimator;
    }

    public HashMap<EnumInterfaceComponent, Animator> getButtonAnimators() {
        return this.buttonAnimator;
    }

    public HashMap<EnumInterfaceComponent, Animator> getTankPreviewAnimators() {
        return tankPreviewAnimator;
    }

    public Animator getTankStatAnimator(int index) {
        return this.tankStatAnimator.get(index);
    }

    public Animator getTankPreviewAnimator(int index) {
        return this.tankPreviewAnimator.get(index);
    }

    public Animator getBackgroundAnimator(int index) {
        return this.backgroundAnimator.get(index);
    }

    public Animator getButtonAnimator(int index) {
        return this.buttonAnimator.get(index);
    }

}

