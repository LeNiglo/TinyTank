package com.lefrantguillaume.gameComponent.animations;

import com.lefrantguillaume.gameComponent.gameObject.obstacles.EnumObstacles;
import com.lefrantguillaume.gameComponent.gameObject.projectiles.EnumShots;
import com.lefrantguillaume.gameComponent.gameObject.spells.EnumSpells;
import com.lefrantguillaume.gameComponent.gameObject.tanks.EnumTanks;
import org.newdawn.slick.SlickException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andres_k on 13/03/2015.
 */
public class AnimatorData {
    private AnimatorFactory animatorFactory;
    private List<Animator> tankAnimator;
    private List<Animator> gunAnimator;
    private List<Animator> spellAnimator;
    private List<Animator> shotAnimator;
    private List<Animator> obstacleAnimator;
    private Animator mapAnimator;

    public AnimatorData() {
        this.animatorFactory = new AnimatorFactory();
        this.tankAnimator = new ArrayList<Animator>();
        this.gunAnimator = new ArrayList<Animator>();
        this.spellAnimator = new ArrayList<Animator>();
        this.shotAnimator = new ArrayList<Animator>();
        this.obstacleAnimator = new ArrayList<Animator>();
    }

    public void initGame() throws SlickException {
        this.initTanks();
        this.initGuns();
        this.initShots();
        this.initSpells();
    }

    public void initMap(String configMapFile) throws SlickException {
        this.mapAnimator = this.animatorFactory.getAnimator(EnumSprites.MAP);
        //do some things with the configFile
    }

    private void initTanks() throws SlickException {
        this.addTankAnimator(this.animatorFactory.getAnimator(EnumSprites.PANZER_BODY));
    }

    private void initGuns() throws SlickException {
        this.addGunAnimator(this.animatorFactory.getAnimator(EnumSprites.PANZER_GUN));
    }

    private void initSpells() throws SlickException {
        this.addSpellAnimator(this.animatorFactory.getAnimator(EnumSprites.PANZER_SPELL));
    }

    private void initShots() throws SlickException {
        this.addShotAnimator(this.animatorFactory.getAnimator(EnumSprites.PANZER_ROCKET));
    }

    public void addTankAnimator(Animator tankAnimator) {
        this.tankAnimator.add(tankAnimator);
    }

    public void addGunAnimator(Animator gunAnimator) {
        this.gunAnimator.add(gunAnimator);
    }

    public void addSpellAnimator(Animator spellAnimator) {
        this.spellAnimator.add(spellAnimator);
    }

    public void addShotAnimator(Animator shotAnimator) {
        this.shotAnimator.add(shotAnimator);
    }

    public void addObstacleAnimator(Animator obstacleAnimator) {
        this.obstacleAnimator.add(obstacleAnimator);
    }

    public Animator getTankAnimator(EnumTanks index) {
        return new Animator(this.tankAnimator.get(index.getValue()));
    }

    public Animator getGunAnimator(EnumTanks index) {
        return new Animator(this.gunAnimator.get(index.getValue()));
    }

    public Animator getSpellAnimator(EnumSpells index) {
        return new Animator(this.spellAnimator.get(index.getValue()));
    }

    public Animator getShotAnimator(EnumShots index) {
        return new Animator(this.shotAnimator.get(index.getValue()));
    }

    public Animator getMapAnimator() {
        return this.mapAnimator;
    }

    public Animator getObstacleAnimator(EnumObstacles index) {
        return new Animator(this.obstacleAnimator.get(index.getValue()));
    }
}
