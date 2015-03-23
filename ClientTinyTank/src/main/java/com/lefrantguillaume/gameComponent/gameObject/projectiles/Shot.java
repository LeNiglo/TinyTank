package com.lefrantguillaume.gameComponent.gameObject.projectiles;

import com.lefrantguillaume.Utils.tools.Debug;
import com.lefrantguillaume.Utils.stockage.Pair;
import com.lefrantguillaume.Utils.stockage.Tuple;
import com.lefrantguillaume.gameComponent.animations.Animator;
import com.lefrantguillaume.gameComponent.gameObject.EnumType;

import java.util.Observable;
import java.util.Observer;
import java.util.UUID;

/**
 * Created by andres_k on 13/03/2015.
 */
public class Shot implements Observer {
    private final UUID id;
    private final String userId;
    private final Animator animator;
    private Pair<Float, Float> positions;
    private float damage;
    private float speed;
    private float angle;
    private boolean explode;

    public Shot(String userId, float damage, float speed, Animator animator, Tuple<Float, Float, Float> coord) {
        this.explode = false;
        this.userId = userId;
        this.id = UUID.randomUUID();
        this.positions = new Pair<Float, Float>(coord.getV1(), coord.getV2());
        this.angle = coord.getV3();
        this.damage = damage;
        this.speed = speed;
        this.animator = animator;
    }

    @Override
    public void update(Observable o, Object arg) {
        Tuple<Float, Float, EnumType> order = (Tuple<Float, Float, EnumType>) arg;

        Debug.debug("check Object");
        if (order.getV3() == EnumType.OBSTACLE || order.getV3() == EnumType.SHOT || order.getV3() == EnumType.TANK) {
//            this.positions.setV1(order.getV1());
            //          this.positions.setV2(order.getV2());
            this.animator.setIndex(EnumAnimationShot.EXPLODE.getValue());
            this.explode = true;
        }
    }

    // FUNCTIONS
    public void move(int delta) {
        Pair<Float, Float> coords = movePredict(delta, false);
        this.positions.setV1(coords.getV1());
        this.positions.setV2(coords.getV2());
    }

    /**
     * @param delta
     * @param mode  : true for graphic mode
     * @return
     */
    public Pair<Float, Float> movePredict(int delta, boolean mode) {
        float x;
        float y;
        double addX = Math.cos(this.angle * Math.PI / 180);
        double addY = Math.sin(this.angle * Math.PI / 180);
        if (mode == true) {
            x = this.getGraphicalX() + (((float) addX * this.speed / 100) * delta);
            y = this.getGraphicalY() + (((float) addY * this.speed / 100) * delta);
        } else {
            x = this.getX() + (((float) addX * this.speed / 100) * delta);
            y = this.getY() + (((float) addY * this.speed / 100) * delta);

        }
        return new Pair<Float, Float>(x, y);
    }

    // GETTERS
    public float getGraphicalX() {
        return this.positions.getV1() - (this.animator.currentSizeAnimation().getV1() / 2);
    }

    public float getGraphicalY() {
        return this.positions.getV2() - (this.animator.currentSizeAnimation().getV2() / 2);
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
}
