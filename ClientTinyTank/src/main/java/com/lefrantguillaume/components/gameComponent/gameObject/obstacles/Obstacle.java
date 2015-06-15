package com.lefrantguillaume.components.gameComponent.gameObject.obstacles;

import com.lefrantguillaume.Utils.stockage.Pair;
import com.lefrantguillaume.Utils.tools.Block;
import com.lefrantguillaume.Utils.tools.Debug;
import com.lefrantguillaume.components.gameComponent.animations.Animator;
import com.lefrantguillaume.components.gameComponent.animations.EnumAnimation;
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

    private String playerId;
    private String playerPseudo;
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

    public Obstacle(Obstacle obstacle, boolean isCreated) {
        this.animator = new Animator(obstacle.animator);
        this.type = obstacle.type;
        this.maxLife = obstacle.maxLife;
        this.damage = obstacle.damage;
        this.collisionObject = obstacle.collisionObject;
        this.created = obstacle.created;
        this.currentLife = obstacle.currentLife;
        this.shiftOrigin = obstacle.shiftOrigin;
        if (isCreated == true) {
            this.playerId = obstacle.playerId;
            this.playerPseudo = obstacle.playerPseudo;
            this.angle = obstacle.angle;
            this.id = obstacle.id;
            this.positions = new Pair<>(obstacle.positions);
        }
    }

    public void createObstacle(String playerId, String playerPseudo, String id, float angle, float posX, float posY) {
        this.playerId = playerId;
        this.playerPseudo = playerPseudo;
        this.angle = angle;
        this.id = id;
        this.positions = new Pair<>(posX, posY);
        this.created = true;
    }

    public void getHit(){
        if (this.animator != null) {
            if (this.currentLife == 0) {
                this.animator.setCurrent(EnumAnimation.EXPLODE);
            } else {
                this.animator.nextCurrentIndex();
            }
        }
    }
    // GETTERS
    public String getPlayerId() {
        return this.playerId;
    }

    public String getPlayerPseudo(){
        return this.playerPseudo;
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
        return this.id;
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

        Debug.debug("type: " + this.type);
        if (this.type.equals(EnumGameObject.MINE)) {
            types.add(EnumGameObject.ROCKET);
            types.add(EnumGameObject.MACHINE_GUN);
            types.add(EnumGameObject.LASER);
            types.add(EnumGameObject.TIGER);
            types.add(EnumGameObject.RUSHER);
            types.add(EnumGameObject.SNIPER);
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

    public void setCurrentLife(float currentLife) {
        this.currentLife = currentLife;
        this.getHit();
    }

    public void setId(String id){
        this.id = id;
    }
}

