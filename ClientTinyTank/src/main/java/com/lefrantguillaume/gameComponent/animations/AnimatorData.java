package com.lefrantguillaume.gameComponent.animations;

import com.lefrantguillaume.gameComponent.tanks.EnumShots;
import com.lefrantguillaume.gameComponent.tanks.EnumTanks;
import org.newdawn.slick.SlickException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andres_k on 13/03/2015.
 */
public class AnimatorData {
    private AnimatorFactory animatorFactory;
    private List<Animator> tankAnimator;
    private List<Animator> shotAnimator;

    public AnimatorData() {
        this.animatorFactory = new AnimatorFactory();
        this.tankAnimator = new ArrayList<Animator>();
        this.shotAnimator = new ArrayList<Animator>();
    }

    public void initGame() throws SlickException {
        this.initTanks();
        this.initShots();
    }

    public void initHome() {
    }

    private void initTanks() throws SlickException {
        this.addTankAnimator(this.animatorFactory.getAnimator(EnumSprites.PANZER));
    }

    public Animator getTankAnimator(EnumTanks index) {
        return new Animator(tankAnimator.get(index.getValue()));
    }

    public void addTankAnimator(Animator tankAnimator) {
        this.tankAnimator.add(tankAnimator);
    }

    private void initShots() throws SlickException {
        this.addShotAnimator(this.animatorFactory.getAnimator(EnumSprites.ROCKET));
    }

    public Animator getShotAnimator(EnumShots index) {
        return new Animator(this.shotAnimator.get(index.getValue()));
    }

    public void addShotAnimator(Animator shotAnimator) {
        this.shotAnimator.add(shotAnimator);
    }
}
