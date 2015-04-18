package com.lefrantguillaume.gameComponent.gameObject.tanks.equipment;

import com.lefrantguillaume.Utils.stockage.Pair;
import com.lefrantguillaume.Utils.tools.Rectangle;
import com.lefrantguillaume.gameComponent.animations.Animator;
import com.lefrantguillaume.gameComponent.gameObject.tanks.types.EnumTanks;
import com.lefrantguillaume.gameComponent.playerData.action.EnumDirection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andres_k on 18/03/2015.
 */
public class TankState {
    private Pair<Float, Float> shiftOrigin;
    private Pair<Float, Float> positions;
    private List<Rectangle> collisionObject;
    private Animator bodyAnimator;
    private Animator topAnimator;
    private EnumTanks tankType;
    private final float speed;
    private final float maxLife;
    private float armor;
    private float currentLife;
    private float shieldEffect;
    private float slowEffect;
    private float boostEffect;
    private boolean move;
    private float gunAngle;
    private EnumDirection direction;

    public TankState(float speed, float maxLife, float armor, Animator bodyAnimator, Animator topAnimator, EnumTanks tankType, Pair<Float, Float> shiftOrigin) {
        this.bodyAnimator = bodyAnimator;
        this.topAnimator = topAnimator;
        this.tankType = tankType;
        this.speed = speed;
        this.maxLife = maxLife;
        this.armor = armor;
        this.currentLife = maxLife;
        this.shiftOrigin = new Pair<Float, Float>(shiftOrigin);
        this.move = false;
        this.direction = EnumDirection.DOWN;
        this.gunAngle = this.direction.getAngle();
        this.shieldEffect = 0;
        this.slowEffect = 0;
        this.boostEffect = 0;
        this.positions = new Pair<Float, Float>(0f, 0f);
        this.collisionObject = new ArrayList<Rectangle>();
    }

    public TankState(TankState tankState) {
        this.bodyAnimator = new Animator(tankState.bodyAnimator);
        this.topAnimator = new Animator(tankState.topAnimator);
        this.tankType = tankState.tankType;
        this.speed = tankState.speed;
        this.maxLife = tankState.maxLife;
        this.armor = tankState.armor;
        this.currentLife = tankState.maxLife;
        this.shiftOrigin = new Pair<Float, Float>(tankState.shiftOrigin);
        this.move = false;
        this.direction = EnumDirection.DOWN;
        this.gunAngle = this.direction.getAngle();
        this.shieldEffect = 0;
        this.slowEffect = 0;
        this.boostEffect = 0;
        this.positions = new Pair<Float, Float>(0f, 0f);
        this.collisionObject = new ArrayList<Rectangle>();
        for (int i = 0; i < tankState.collisionObject.size(); ++i) {
            this.collisionObject.add(tankState.collisionObject.get(i));
        }
    }

    // FUNCTIONS

    public void addCollisionObject(Rectangle rectangle) {
        this.collisionObject.add(rectangle);
    }

    // GETTERS
    public float getSpeed() {
        return this.speed;
    }

    public float getCurrentSpeed() {
        return this.speed - (this.slowEffect * this.speed / 100) + (this.boostEffect * this.speed / 100);
    }

    public List<Rectangle> getCollisionObject() {
        return this.collisionObject;
    }

    public float getCurrentLife() {
        return this.currentLife;
    }

    public float getMaxLife() {
        return this.maxLife;
    }

    public float getArmor() {
        return this.armor;
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
        return this.bodyAnimator;
    }

    public Animator getTopAnimator() {
        return this.topAnimator;
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

    // SETTERS
    public void setCurrentLife(float currentLife) {
        this.currentLife = currentLife;
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

    public void setArmor(float armor) {
        this.armor = armor;
    }

    public EnumTanks getTankType() {
        return tankType;
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
}
