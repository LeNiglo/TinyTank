package com.lefrantguillaume.components.gameComponent.animations;

import com.lefrantguillaume.components.gameComponent.gameObject.EnumGameObject;
import org.newdawn.slick.SlickException;

import java.util.HashMap;

/**
 * Created by andres_k on 13/03/2015.
 */
public class AnimatorGameData {
    private AnimatorFactory animatorFactory;
    private HashMap<EnumGameObject, Animator> tankBodyAnimator;
    private HashMap<EnumGameObject, Animator> tankTopAnimator;
    private HashMap<EnumGameObject, Animator> spellAnimator;
    private HashMap<EnumGameObject, Animator> shotAnimator;
    private HashMap<EnumGameObject, Animator> obstacleAnimator;
    private HashMap<EnumGameObject, Animator> areaAnimator;
    private Animator mapAnimator;

    public AnimatorGameData() {
        this.animatorFactory = new AnimatorGameFactory();
        this.tankBodyAnimator = new HashMap<>();
        this.tankTopAnimator = new HashMap<>();
        this.spellAnimator = new HashMap<>();
        this.shotAnimator = new HashMap<>();
        this.obstacleAnimator = new HashMap<>();
        this.areaAnimator = new HashMap<>();
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
        this.addTankAnimator(this.animatorFactory.getAnimator(EnumSprites.TIGER_BODY), EnumGameObject.TIGER);
        this.addTankAnimator(this.animatorFactory.getAnimator(EnumSprites.SNIPER_BODY), EnumGameObject.SNIPER);
        this.addTankAnimator(this.animatorFactory.getAnimator(EnumSprites.RUSHER_BODY), EnumGameObject.RUSHER);
        this.addTankAnimator(this.animatorFactory.getAnimator(EnumSprites.TIGER_BODY_ENEMY), EnumGameObject.TIGER_ENEMY);
        this.addTankAnimator(this.animatorFactory.getAnimator(EnumSprites.SNIPER_BODY_ENEMY), EnumGameObject.SNIPER_ENEMY);
        this.addTankAnimator(this.animatorFactory.getAnimator(EnumSprites.RUSHER_BODY_ENEMY), EnumGameObject.RUSHER_ENEMY);
    }

    private void initGuns() throws SlickException {
        this.addGunAnimator(this.animatorFactory.getAnimator(EnumSprites.TIGER_TOP), EnumGameObject.TIGER);
        this.addGunAnimator(this.animatorFactory.getAnimator(EnumSprites.SNIPER_TOP), EnumGameObject.SNIPER);
        this.addGunAnimator(this.animatorFactory.getAnimator(EnumSprites.RUSHER_TOP), EnumGameObject.RUSHER);
        this.addGunAnimator(this.animatorFactory.getAnimator(EnumSprites.TIGER_TOP_ENEMY), EnumGameObject.TIGER_ENEMY);
        this.addGunAnimator(this.animatorFactory.getAnimator(EnumSprites.SNIPER_TOP_ENEMY), EnumGameObject.SNIPER_ENEMY);
        this.addGunAnimator(this.animatorFactory.getAnimator(EnumSprites.RUSHER_TOP_ENEMY), EnumGameObject.RUSHER_ENEMY);
    }

    private void initSpells() throws SlickException {
        this.addSpellAnimator(this.animatorFactory.getAnimator(EnumSprites.TIGER_SPELL), EnumGameObject.SHIELD);
        this.addSpellAnimator(this.animatorFactory.getAnimator(EnumSprites.SNIPER_SPELL), EnumGameObject.INVISIBILITY);
        this.addSpellAnimator(this.animatorFactory.getAnimator(EnumSprites.RUSHER_SPELL), EnumGameObject.TELEPORT);
    }

    private void initShots() throws SlickException {
        this.addShotAnimator(this.animatorFactory.getAnimator(EnumSprites.TIGER_HIT), EnumGameObject.ROCKET);
        this.addShotAnimator(this.animatorFactory.getAnimator(EnumSprites.SNIPER_HIT), EnumGameObject.LASER);
        this.addShotAnimator(this.animatorFactory.getAnimator(EnumSprites.RUSHER_HIT), EnumGameObject.MACHINE_GUN);
    }

    private void initObstacles() throws SlickException {
        this.addObstacleAnimator(this.animatorFactory.getAnimator(EnumSprites.IRON_WALL), EnumGameObject.IRON_WALL);
        this.addObstacleAnimator(this.animatorFactory.getAnimator(EnumSprites.PLASMA_WALL), EnumGameObject.PLASMA_WALL);
        this.addObstacleAnimator(this.animatorFactory.getAnimator(EnumSprites.MINE), EnumGameObject.MINE);
    }

    private void initAreas() throws SlickException {
        this.addAreaAnimator(this.animatorFactory.getAnimator(EnumSprites.OBJECTIVE), EnumGameObject.OBJECTIVE_AREA);

        this.addAreaAnimator(this.animatorFactory.getAnimator(EnumSprites.BOMB), EnumGameObject.SPAWN_AREA);
        //TODO -> AREA devient BOMB ( server + client )
    }

    public void addTankAnimator(Animator tankAnimator, EnumGameObject type) {
        this.tankBodyAnimator.put(type, tankAnimator);
    }

    public void addGunAnimator(Animator gunAnimator, EnumGameObject type) {
        this.tankTopAnimator.put(type, gunAnimator);
    }

    public void addSpellAnimator(Animator spellAnimator, EnumGameObject type) {
        this.spellAnimator.put(type, spellAnimator);
    }

    public void addShotAnimator(Animator shotAnimator, EnumGameObject type) {
        this.shotAnimator.put(type, shotAnimator);
    }

    public void addObstacleAnimator(Animator obstacleAnimator, EnumGameObject type) {
        this.obstacleAnimator.put(type, obstacleAnimator);
    }

    public void addAreaAnimator(Animator areaAnimator, EnumGameObject type){
        this.areaAnimator.put(type, areaAnimator);
    }

    public HashMap<EnumGameObject, Animator> getTankBodyAnimator(EnumGameObject type) {
        if (this.tankBodyAnimator.containsKey(type)) {
            HashMap<EnumGameObject, Animator> result = new HashMap<>();
            result.put(type, new Animator(this.tankBodyAnimator.get(type)));
            EnumGameObject newType = EnumGameObject.getEnemyEnum(type);
            if (newType != type && this.tankBodyAnimator.containsKey(newType)){
                result.put(newType, new Animator(this.tankBodyAnimator.get(newType)));
            }
            return result;
        } else {
            return null;
        }
    }

    public HashMap<EnumGameObject, Animator> getTankTopAnimator(EnumGameObject type) {
        if (this.tankTopAnimator.containsKey(type)) {
            HashMap<EnumGameObject, Animator> result = new HashMap<>();
            result.put(type, new Animator (this.tankTopAnimator.get(type)));
            EnumGameObject newType = EnumGameObject.getEnemyEnum(type);
            if (newType != type && this.tankTopAnimator.containsKey(newType)){
                result.put(newType, new Animator (this.tankTopAnimator.get(newType)));
            }
            return result;
        } else {
            return null;
        }
    }

    public Animator getSpellAnimator(EnumGameObject type) {
        if (this.spellAnimator.containsKey(type)) {
            return new Animator(this.spellAnimator.get(type));
        } else {
            return null;
        }
    }

    public Animator getShotAnimator(EnumGameObject type) {
        if (this.shotAnimator.containsKey(type)) {
            return new Animator(this.shotAnimator.get(type));
        } else {
            return null;
        }
    }

    public Animator getMapAnimator() {
        return this.mapAnimator;
    }

    public Animator getObstacleAnimator(EnumGameObject type) {
        if (this.obstacleAnimator.containsKey(type)) {
            return new Animator(this.obstacleAnimator.get(type));
        } else {
            return null;
        }
    }

    public Animator getAreaAnimator(EnumGameObject type) {
        if (this.areaAnimator.containsKey(type)) {
            return new Animator(this.areaAnimator.get(type));
        } else {
            return null;
        }
    }
}
