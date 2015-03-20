package com.lefrantguillaume.gameComponent.animations;

import org.newdawn.slick.SlickException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andres_k on 13/03/2015.
 */
public class AnimatorInterfaceData {
    private AnimatorFactory animatorFactory;
    private List<Animator> tankStatAnimator;
    private List<Animator> tankPreviewAnimator;
    private List<Animator> backgroundAnimator;
    private List<Animator> buttonAnimator;

    public AnimatorInterfaceData() {
        this.animatorFactory = new AnimatorInterfaceFactory();
        this.tankStatAnimator = new ArrayList<Animator>();
        this.tankPreviewAnimator = new ArrayList<Animator>();
        this.backgroundAnimator = new ArrayList<Animator>();
        this.buttonAnimator = new ArrayList<Animator>();
    }

    public void init() throws SlickException {
        this.initTanks();
        this.initBackgrounds();
        this.initButtons();
    }

    public void initTanks() throws SlickException {
        this.addTankStatAnimator(this.animatorFactory.getAnimator(EnumSprites.STAT_TIGER));
        this.addTankStatAnimator(this.animatorFactory.getAnimator(EnumSprites.STAT_RUSHER));
        this.addTankStatAnimator(this.animatorFactory.getAnimator(EnumSprites.STAT_SNIPER));
        this.addTankStatAnimator(this.animatorFactory.getAnimator(EnumSprites.STAT_RANK));
        this.addTankPreviewAnimator(this.animatorFactory.getAnimator(EnumSprites.PREVIEW_TIGER));
        this.addTankPreviewAnimator(this.animatorFactory.getAnimator(EnumSprites.PREVIEW_RUSHER));
        this.addTankPreviewAnimator(this.animatorFactory.getAnimator(EnumSprites.PREVIEW_SNIPER));
    }

    public void initBackgrounds() throws SlickException {
        this.addBackgroundAnimator(this.animatorFactory.getAnimator(EnumSprites.BACKGROUND));
    }

    public void initButtons() throws SlickException {
        this.addButtonAnimator(this.animatorFactory.getAnimator(EnumSprites.BUTTONS));
    }

    public void addTankStatAnimator(Animator tankStatAnimator) {
        this.tankStatAnimator.add(tankStatAnimator);
    }

    public void addTankPreviewAnimator(Animator tankPreviewAnimator) {
        this.tankPreviewAnimator.add(tankPreviewAnimator);
    }

    public void addBackgroundAnimator(Animator backgroundAnimator) {
        this.backgroundAnimator.add(backgroundAnimator);
    }

    public void addButtonAnimator(Animator buttonAnimator) {
        this.buttonAnimator.add(buttonAnimator);
    }

    public List<Animator> getTankStatAnimators() {
        return this.tankStatAnimator;
    }

    public List<Animator> getBackgroundAnimators() {
        return this.backgroundAnimator;
    }

    public List<Animator> getButtonAnimators() {
        return this.buttonAnimator;
    }

    public List<Animator> getTankPreviewAnimators() {
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

