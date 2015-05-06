package com.lefrantguillaume.gameComponent.gameObject.projectiles;

import com.lefrantguillaume.Utils.stockage.Pair;
import com.lefrantguillaume.Utils.tools.MathTools;
import com.lefrantguillaume.Utils.tools.Rectangle;
import com.lefrantguillaume.gameComponent.gameObject.EnumType;
import com.lefrantguillaume.Utils.stockage.Tuple;
import com.lefrantguillaume.gameComponent.animations.Animator;

import java.util.*;

/**
 * Created by andres_k on 13/03/2015.
 */
public class Shot extends Observable implements Observer {
    private final UUID id;
    private final String userId;
    private final Animator animator;
    private final Pair<Float, Float> shiftHead;
    private final Pair<Float, Float> shiftToExplode;
    private Pair<Float, Float> shiftOrigin;
    private Pair<Float, Float> positions;
    private float damage;
    private float speed;
    private float angle;
    private boolean explode;
    private List<Rectangle> collisionObject;

    public Shot(String userId, UUID id, float damage, float speed, Animator animator, Tuple<Float, Float, Float> positioning, Pair<Float, Float> shiftOrigin, Pair<Float, Float> shiftToExplode, Pair<Float, Float> shiftHead) {
        this.shiftOrigin = new Pair<Float, Float>(shiftOrigin);
        this.shiftToExplode = new Pair<Float, Float>(shiftToExplode);
        this.shiftHead = new Pair<Float, Float>(shiftHead);
        this.explode = false;
        this.userId = userId;
        this.id = id;
        this.positions = new Pair<Float, Float>(positioning.getV1(), positioning.getV2());
        this.angle = positioning.getV3();
        this.damage = damage;
        this.speed = speed;
        this.animator = animator;
        this.collisionObject = new ArrayList<Rectangle>();
    }

    @Override
    public void update(Observable o, Object arg) {
        Tuple<Float, Float, EnumType> order = (Tuple<Float, Float, EnumType>) arg;

        if (order.getV3() == EnumType.UNBREAKABLE || order.getV3() == EnumType.OBSTACLE || order.getV3() == EnumType.SHOT || order.getV3() == EnumType.TANK) {
            this.positions.setV1(order.getV1());
            this.positions.setV2(order.getV2());
            this.shiftOrigin.setV1(this.shiftToExplode.getV1());
            this.shiftOrigin.setV2(this.shiftToExplode.getV2());
            this.animator.setIndex(EnumAnimationShot.EXPLODE.getValue());
            this.explode = true;
            Rectangle tmp = null;
            this.setChanged();
            this.notifyObservers(tmp);
        }
    }

    // FUNCTIONS
    public void move(float delta) {
        if (this.explode == false) {
            Pair<Float, Float> coords = this.movePredict(delta);
            this.positions.setV1(this.getX() + coords.getV1());
            this.positions.setV2(this.getY() + coords.getV2());
        }
    }

    public Pair<Float, Float> movePredict(float delta) {
        return MathTools.movePredict(this.angle, this.speed, delta);
    }

    public Pair<Float, Float> coordPredict(float delta) {
        Pair<Float, Float> coords = this.movePredict(delta);
        coords.setV1(coords.getV1() + this.getX());
        coords.setV2(coords.getV2() + this.getY());
        return coords;
    }

    public void addCollisionObject(Rectangle rectangle) {
        this.collisionObject.add(rectangle);
    }

    // GETTERS
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

    public String getUserId() {
        return this.userId;
    }

    public UUID getId() {
        return this.id;
    }

    public float getDamage() {
        return this.damage;
    }

    public float getSpeed() {
        return this.speed;
    }

    public float getAngle() {

        return this.angle;
    }

    public boolean getExplode() {
        return this.explode;
    }

    public Animator getAnimator() {
        return animator;
    }

    public Pair<Float, Float> getPositions() {
        return this.positions;
    }

    public Pair<Float, Float> getShiftOrigin() {
        return this.shiftOrigin;
    }

    public Pair<Float, Float> getShiftToExplode() {
        return this.shiftToExplode;
    }

    public List<Rectangle> getCollisionObject() {
        return this.collisionObject;
    }
}
