package com.lefrantguillaume.components.gameComponent.gameObject.tanks.equipment;

import com.lefrantguillaume.Utils.stockage.Pair;
import com.lefrantguillaume.Utils.tools.Block;
import com.lefrantguillaume.components.gameComponent.animations.Animator;
import com.lefrantguillaume.components.gameComponent.animations.EnumAnimation;
import com.lefrantguillaume.components.gameComponent.gameObject.EnumGameObject;
import com.lefrantguillaume.components.gameComponent.playerData.action.EnumDirection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by andres_k on 18/03/2015.
 */
public class TankState {
    private Pair<Float, Float> shiftOrigin;
    private Pair<Float, Float> positions;
    private Pair<Float, Float> shiftOriginSave;
    private Pair<Float, Float> shiftToExplode;
    private List<Block> collisionObject;
    private HashMap<EnumGameObject, Animator> bodyAnimator;
    private HashMap<EnumGameObject, Animator> topAnimator;
    private EnumGameObject type;
    private EnumGameObject currentTeam;
    private final float speed;
    private final float maxLife;
    private final float maxArmor;
    private final float accuracy;
    private float currentLife;
    private float currentArmor;
    private float shieldEffect;
    private float slowEffect;
    private float boostEffect;
    private boolean move;
    private float gunAngle;
    private EnumDirection direction;

    public TankState(float speed, float maxLife, float maxArmor, float accuracy, HashMap<EnumGameObject, Animator> bodyAnimator, HashMap<EnumGameObject, Animator> topAnimator, EnumGameObject type,
                     Pair<Float, Float> shiftOrigin, Pair<Float, Float> shiftToExplode) {
        this.bodyAnimator = bodyAnimator;
        this.topAnimator = topAnimator;
        this.type = type;
        this.currentTeam = type;
        this.speed = speed;
        this.maxLife = maxLife;
        this.maxArmor = maxArmor;
        this.accuracy = accuracy;
        this.currentLife = maxLife;
        this.currentArmor = maxArmor;
        this.shiftOrigin = new Pair<>(shiftOrigin);
        this.shiftOriginSave = new Pair<>(shiftOrigin);
        this.shiftToExplode = new Pair<>(shiftToExplode);
        this.move = false;
        this.direction = EnumDirection.DOWN;
        this.gunAngle = this.direction.getAngle();
        this.shieldEffect = 0;
        this.slowEffect = 0;
        this.boostEffect = 0;
        this.positions = new Pair<>(0f, 0f);
        this.collisionObject = new ArrayList<>();
    }

    public TankState(TankState tankState) {
        this.bodyAnimator = new HashMap<>();
        this.topAnimator = new HashMap<>();
        for (Map.Entry entry : tankState.bodyAnimator.entrySet()){
            this.bodyAnimator.put((EnumGameObject)entry.getKey(), new Animator((Animator)entry.getValue()));
        }
        for (Map.Entry entry : tankState.topAnimator.entrySet()){
            this.topAnimator.put((EnumGameObject)entry.getKey(), new Animator((Animator)entry.getValue()));
        }
        this.type = tankState.type;
        this.currentTeam = tankState.currentTeam;
        this.speed = tankState.speed;
        this.maxLife = tankState.maxLife;
        this.maxArmor = tankState.maxArmor;
        this.accuracy = tankState.accuracy;
        this.currentLife = tankState.maxLife;
        this.currentArmor = tankState.maxArmor;
        this.shiftOrigin = new Pair<>(tankState.shiftOrigin);
        this.shiftOriginSave = new Pair<>(tankState.shiftOrigin);
        this.shiftToExplode = new Pair<>(tankState.shiftToExplode);
        this.move = false;
        this.direction = EnumDirection.DOWN;
        this.gunAngle = this.direction.getAngle();
        this.shieldEffect = 0;
        this.slowEffect = 0;
        this.boostEffect = 0;
        this.positions = new Pair<>(0f, 0f);
        this.collisionObject = new ArrayList<>();
        for (int i = 0; i < tankState.collisionObject.size(); ++i) {
            this.collisionObject.add(tankState.collisionObject.get(i));
        }
    }

    // FUNCTIONS

    public void addCollisionObject(Block block) {
        this.collisionObject.add(block);
    }

    public void explode() {
        this.topAnimator.get(this.currentTeam).setPrintable(false);
        this.bodyAnimator.get(this.currentTeam).setCurrent(EnumAnimation.EXPLODE);
        this.shiftOrigin.setV1(this.shiftToExplode.getV1());
        this.shiftOrigin.setV2(this.shiftToExplode.getV2());
    }

    public void init(Pair<Float, Float> positions) {
        this.move = false;
        this.shieldEffect = 0;
        this.slowEffect = 0;
        this.boostEffect = 0;
        this.positions = new Pair<>(positions);
        this.currentLife = this.maxLife;
        for (Map.Entry entry : this.bodyAnimator.entrySet()){
            Animator tmp = (Animator) entry.getValue();
            tmp.currentAnimation().restart();
            tmp.setCurrent(EnumAnimation.BASIC);
            tmp.setPrintable(true);
        }
        for (Map.Entry entry : this.topAnimator.entrySet()) {
            Animator tmp = (Animator) entry.getValue();
            tmp.setPrintable(true);
        }
        this.shiftOrigin = new Pair<>(this.shiftOriginSave);
    }

    // GETTERS
    public float getSpeed() {
        return this.speed;
    }

    public float getCurrentSpeed() {
        return this.speed - (this.slowEffect * this.speed / 100) + (this.boostEffect * this.speed / 100);
    }

    public List<Block> getCollisionObject() {
        return this.collisionObject;
    }

    public float getCurrentLife() {
        return this.currentLife;
    }

    public float getCurrentArmor() {
        return this.currentArmor;
    }

    public float getMaxLife() {
        return this.maxLife;
    }

    public float getMaxArmor() {
        return this.maxArmor;
    }

    public float getAccuracy() {
        return this.accuracy;
    }

    public float getBoostEffect() {
        return this.boostEffect;
    }

    public float getSlowEffect() {
        return this.slowEffect;
    }

    public float getShieldEffect() {
        return this.shieldEffect;
    }

    public Animator getBodyAnimator() {
        return this.bodyAnimator.get(this.currentTeam);
    }

    public Animator getTopAnimator() {
        return this.topAnimator.get(this.currentTeam);
    }

    public Pair<Float, Float> getPositions() {
        return positions;
    }

    public float getX() {
        return this.positions.getV1();
    }

    public float getY() {
        return this.positions.getV2();
    }

    public float getGraphicalX() {
        return this.positions.getV1() + this.shiftOrigin.getV1();
    }

    public float getGraphicalY() {
        return this.positions.getV2() + this.shiftOrigin.getV2();
    }

    public Pair<Float, Float> getShiftOrigin() {
        return this.shiftOrigin;
    }

    public boolean isMove() {
        return this.move;
    }

    public float getGunAngle() {
        return this.gunAngle;
    }

    public EnumDirection getDirection() {
        return this.direction;
    }

    public Pair<Float, Float> getShiftToExplode() {
        return this.shiftToExplode;
    }

    public EnumGameObject getType() {
        return type;
    }

    // SETTERS
    public void setCurrentLife(float currentLife) {
        this.currentLife = currentLife;
    }

    public void setCurrentArmor(float currentArmor) {
        this.currentArmor = currentArmor;
    }

    public void setShieldEffect(float shieldEffect) {
        this.shieldEffect = shieldEffect;
    }

    public void setSlowEffect(float slowEffect) {
        this.slowEffect = slowEffect;
    }

    public void setBoostEffect(float boostEffect) {
        this.boostEffect = boostEffect;
    }

    public void setPositions(Pair<Float, Float> positions) {
        this.positions = positions;
    }

    public void setX(float x) {
        this.positions.setV1(x);
    }

    public void setY(float y) {
        this.positions.setV2(y);
    }

    public void addX(float x) {
        this.positions.setV1(this.positions.getV1() + x);
    }

    public void addY(float y) {
        this.positions.setV2(this.positions.getV2() + y);
    }

    public void setMove(boolean move) {
        this.move = move;
    }

    public void setGunAngle(float gunAngle) {
        this.gunAngle = gunAngle;
    }

    public void setDirection(EnumDirection direction) {
        this.direction = direction;
    }

    public void setCurrentTeam(EnumGameObject team){
        if (this.bodyAnimator.containsKey(team) && this.topAnimator.containsKey(team)){
            this.currentTeam = team;
        }
    }
}
