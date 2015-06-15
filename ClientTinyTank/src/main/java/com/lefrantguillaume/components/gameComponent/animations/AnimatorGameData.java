package com.lefrantguillaume.components.gameComponent.animations;

import com.lefrantguillaume.components.gameComponent.gameObject.EnumGameObject;
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
    private List<Animator> areaAnimator;
    private Animator mapAnimator;

    public AnimatorGameData() {
        this.animatorFactory = new AnimatorGameFactory();
        this.tankBodyAnimator = new ArrayList<>();
        this.tankTopAnimator = new ArrayList<>();
        this.spellAnimator = new ArrayList<>();
        this.shotAnimator = new ArrayList<>();
        this.obstacleAnimator = new ArrayList<>();
        this.areaAnimator = new ArrayList<>();
    }

    public void initGame() throws SlickException {
        this.initTanks();
        this.initGuns();
        this.initShots();
        this.initSpells();
        this.initObstacles();
        this.initAreas();
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
        this.addObstacleAnimator(this.animatorFactory.getAnimator(EnumSprites.IRON_WALL));
        this.addObstacleAnimator(this.animatorFactory.getAnimator(EnumSprites.IRON_WALL));
        this.addObstacleAnimator(this.animatorFactory.getAnimator(EnumSprites.IRON_WALL));
    }

    private void initAreas() throws SlickException {
        this.addAreaAnimator(this.animatorFactory.getAnimator(EnumSprites.TIGER_SPELL));
        this.addAreaAnimator(this.animatorFactory.getAnimator(EnumSprites.TIGER_SPELL));
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

    public void addAreaAnimator(Animator areaAnimator){
        this.areaAnimator.add(areaAnimator);
    }

    public Animator getTankBodyAnimator(EnumGameObject index) {
        if (index.getIndex() >= 0 && index.getIndex() < this.tankBodyAnimator.size()) {
            return new Animator(this.tankBodyAnimator.get(index.getIndex()));
        } else {
            return null;
        }
    }

    public Animator getTankTopAnimator(EnumGameObject index) {
        if (index.getIndex() >= 0 && index.getIndex() < this.tankTopAnimator.size()) {
            return new Animator(this.tankTopAnimator.get(index.getIndex()));
        } else {
            return null;
        }
    }

    public Animator getSpellAnimator(EnumGameObject index) {
        if (index.getIndex() >= 0 && index.getIndex() < this.spellAnimator.size()) {
            return new Animator(this.spellAnimator.get(index.getIndex()));
        } else {
            return null;
        }
    }

    public Animator getShotAnimator(EnumGameObject index) {
        if (index.getIndex() >= 0 && index.getIndex() < this.shotAnimator.size()) {
            return new Animator(this.shotAnimator.get(index.getIndex()));
        } else {
            return null;
        }
    }

    public Animator getMapAnimator() {
        return this.mapAnimator;
    }

    public Animator getObstacleAnimator(EnumGameObject index) {
        if (index.getIndex() >= 0 && index.getIndex() < this.obstacleAnimator.size()) {
            return new Animator(this.obstacleAnimator.get(index.getIndex()));
        } else {
            return null;
        }
    }

    public Animator getAreaAnimator(EnumGameObject index) {
        if (index.getIndex() >= 0 && index.getIndex() < this.areaAnimator.size()) {
            return new Animator(this.areaAnimator.get(index.getIndex()));
        } else {
            return null;
        }
    }
}
