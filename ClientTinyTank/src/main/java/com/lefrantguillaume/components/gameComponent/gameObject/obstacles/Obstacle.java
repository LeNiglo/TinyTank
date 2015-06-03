package com.lefrantguillaume.components.gameComponent.gameObject.obstacles;

import com.lefrantguillaume.Utils.stockage.Pair;
import com.lefrantguillaume.Utils.tools.Block;
import com.lefrantguillaume.components.gameComponent.animations.Animator;
import com.lefrantguillaume.components.gameComponent.gameObject.EnumGameObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andres_k on 16/03/2015.
 */
public class Obstacle {
    private final Animator animator;
    private final EnumGameObject type;
    private final List<Block> collisionObject;
    private final Pair<Float, Float> shiftOrigin;
    private final float maxLife;
    private final float damage;
    private float currentLife;

    private String userId;
    private String id;
    private Pair<Float, Float> positions;
    private float angle;
    private boolean created;

    public Obstacle(Animator animator, EnumGameObject type, List<Block> collisionObject, Pair<Float, Float> shiftOrigin, float maxLife, float damage) {
        this.animator = animator;
        this.type = type;
        this.collisionObject = collisionObject;
        this.shiftOrigin = shiftOrigin;
        this.maxLife = maxLife;
        this.damage = damage;
        this.created = false;
        this.currentLife = this.maxLife;
    }

    public Obstacle(Obstacle obstacle) {
        this.animator = new Animator(obstacle.animator);
        this.type = obstacle.type;
        this.maxLife = obstacle.maxLife;
        this.damage = obstacle.damage;
        this.created = obstacle.created;
        this.currentLife = obstacle.currentLife;
        this.collisionObject = obstacle.collisionObject;
        this.shiftOrigin = obstacle.shiftOrigin;
    }

    public void createObstacle(String userId, String id, float angle, float posX, float posY) {
        this.userId = userId;
        this.angle = angle;
        this.id = id;
        this.positions = new Pair<>(posX, posY);
        this.created = true;
    }

    // GETTERS
    public String getUserId() {
        return this.userId;
    }

    public float getGraphicalX() {
        return this.positions.getV1() + this.shiftOrigin.getV1();
    }

    public float getGraphicalY() {
        return this.positions.getV2() + this.shiftOrigin.getV2();
    }

    public float getX() {
        return this.positions.getV1();
    }

    public float getY() {
        return this.positions.getV2();
    }

    public String getId() {
        return id;
    }

    public Animator getAnimator() {
        return animator;
    }

    public EnumGameObject getType() {
        return type;
    }

    public Pair<Float, Float> getPositions() {
        return this.positions;
    }

    public Pair<Float, Float> getShiftOrigin() {
        return shiftOrigin;
    }

    public float getAngle() {
        return this.angle;
    }

    public float getMaxLife() {
        return this.maxLife;
    }

    public float getCurrentLife() {
        return this.currentLife;
    }

    public float getDamage() {
        return this.damage;
    }

    public List<EnumGameObject> getIgnoredObjectList() {
        List<EnumGameObject> types = new ArrayList<>();

        if (this.type.equals(EnumGameObject.MINE)) {
            types.add(EnumGameObject.ROCKET);
            types.add(EnumGameObject.MACHINE_GUN);
            types.add(EnumGameObject.LASER);
        } else if (this.type.equals(EnumGameObject.PLASMA_WALL)) {
            types.add(EnumGameObject.LASER);
        }
        return types;
    }

    public List<Block> getCollisionObject(){
        return this.collisionObject;
    }

    // SETTERS
    public void setX(float x) {
        this.positions.setV1(x);
    }

    public void setY(float y) {
        this.positions.setV2(y);
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public void setCurrentLife(int currentLife) {
        this.currentLife = currentLife;
    }
}
