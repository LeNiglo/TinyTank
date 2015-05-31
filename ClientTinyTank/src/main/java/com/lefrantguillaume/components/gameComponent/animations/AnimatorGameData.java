package com.lefrantguillaume.components.gameComponent.animations;

import com.lefrantguillaume.components.gameComponent.gameObject.obstacles.EnumObstacles;
import com.lefrantguillaume.components.gameComponent.gameObject.spells.EnumSpells;
import com.lefrantguillaume.components.gameComponent.gameObject.tanks.types.EnumTanks;
import com.lefrantguillaume.components.gameComponent.gameObject.projectiles.EnumShots;
import org.newdawn.slick.SlickException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andres_k on 13/03/2015.
 */
public class AnimatorGameData {
    private AnimatorFactory animatorFactory;
    private List<Animator> tankBodyAnimator;
    private List<Animator> tankTopAnimator;
    private List<Animator> spellAnimator;
    private List<Animator> shotAnimator;
    private List<Animator> obstacleAnimator;
    private Animator mapAnimator;

    public AnimatorGameData() {
        this.animatorFactory = new AnimatorGameFactory();
        this.tankBodyAnimator = new ArrayList<Animator>();
        this.tankTopAnimator = new ArrayList<Animator>();
        this.spellAnimator = new ArrayList<Animator>();
        this.shotAnimator = new ArrayList<Animator>();
        this.obstacleAnimator = new ArrayList<Animator>();
    }

    public void initGame() throws SlickException {
        this.initTanks();
        this.initGuns();
        this.initShots();
        this.initSpells();
        this.initObstacles();
    }

    public void initMap(String configMapFile) throws SlickException {
        this.mapAnimator = this.animatorFactory.getAnimator(EnumSprites.MAP);
        //do some things with the configFile
    }

    private void initTanks() throws SlickException {
        this.addTankAnimator(this.animatorFactory.getAnimator(EnumSprites.TIGER_BODY));
        this.addTankAnimator(this.animatorFactory.getAnimator(EnumSprites.SNIPER_BODY));
        this.addTankAnimator(this.animatorFactory.getAnimator(EnumSprites.RUSHER_BODY));
    }

    private void initGuns() throws SlickException {
        this.addGunAnimator(this.animatorFactory.getAnimator(EnumSprites.TIGER_TOP));
        this.addGunAnimator(this.animatorFactory.getAnimator(EnumSprites.SNIPER_TOP));
        this.addGunAnimator(this.animatorFactory.getAnimator(EnumSprites.RUSHER_TOP));
    }

    private void initSpells() throws SlickException {
        this.addSpellAnimator(this.animatorFactory.getAnimator(EnumSprites.TIGER_SPELL));
        this.addSpellAnimator(this.animatorFactory.getAnimator(EnumSprites.SNIPER_SPELL));
        this.addSpellAnimator(this.animatorFactory.getAnimator(EnumSprites.RUSHER_SPELL));
    }

    private void initShots() throws SlickException {
        this.addShotAnimator(this.animatorFactory.getAnimator(EnumSprites.TIGER_HIT));
        this.addShotAnimator(this.animatorFactory.getAnimator(EnumSprites.SNIPER_HIT));
        this.addShotAnimator(this.animatorFactory.getAnimator(EnumSprites.RUSHER_HIT));
    }

    private void initObstacles() throws SlickException {
        this.addObstacleAnimator(this.animatorFactory.getAnimator(EnumSprites.WALL_WOOD));

    }

    public void addTankAnimator(Animator tankAnimator) {
        this.tankBodyAnimator.add(tankAnimator);
    }

    public void addGunAnimator(Animator gunAnimator) {
        this.tankTopAnimator.add(gunAnimator);
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

    public Animator getTankBodyAnimator(EnumTanks index) {
        return new Animator(this.tankBodyAnimator.get(index.getIndex()));
    }

    public Animator getTankTopAnimator(EnumTanks index) {
        return new Animator(this.tankTopAnimator.get(index.getIndex()));
    }

    public Animator getSpellAnimator(EnumSpells index) {
        return new Animator(this.spellAnimator.get(index.getIndex()));
    }

    public Animator getShotAnimator(EnumShots index) {
        return new Animator(this.shotAnimator.get(index.getIndex()));
    }

    public Animator getMapAnimator() {
        return this.mapAnimator;
    }

    public Animator getObstacleAnimator(EnumObstacles index) {
        return new Animator(this.obstacleAnimator.get(index.getIndex()));
    }
}
