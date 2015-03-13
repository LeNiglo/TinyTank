package com.lefrantguillaume.gameComponent.actions;

import com.lefrantguillaume.Utils.Debug;
import com.lefrantguillaume.Utils.stockage.Pair;
import com.lefrantguillaume.Utils.stockage.Tuple;
import com.lefrantguillaume.gameComponent.animations.Animator;
import com.lefrantguillaume.gameComponent.tanks.EnumAnimationShot;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by andres_k on 13/03/2015.
 */
public class Shot implements Observer {

    private float damage;
    private float speed;
    private float x;
    private float y;
    private float angle;
    private Animator animator;
    private int userId;
    private int shotId;
    private boolean explode;

    public Shot(float damage, float speed, Animator animator, Tuple<Float, Float, Float> coord, int userId, int shotId) {
        this.explode = false;
        this.userId = userId;
        this.shotId = shotId;
        this.x = coord.getV1();
        this.y = coord.getV2();
        this.angle = coord.getV3();
        this.damage = damage;
        this.speed = speed;
        this.animator = animator;
        Debug.debug("Shot: " + String.valueOf(userId) + " " + String.valueOf(shotId));
    }

    public void move(int delta) {
        Pair<Float, Float> coords = movePredict(delta);
        this.x = coords.getKey();
        this.y = coords.getValue();
    }

    public Pair<Float, Float> movePredict(int delta) {
        float x = this.x;
        float y = this.y;
        if (this.angle == 270f)
            y = y - ((this.speed / 10) * delta);
        else if (this.angle == 0f)
            x = x + ((this.speed / 10) * delta);
        else if (this.angle == 90f)
            y = y + ((this.speed / 10) * delta);
        else if (this.angle == 180f)
            x = x - ((this.speed / 10) * delta);
        return new Pair<Float, Float>(x, y);
    }

    public int getUserId() {
        return this.userId;
    }

    public int getShotId() {
        return this.shotId;
    }

    public float getDamage() {
        return this.damage;
    }

    public float getSpeed() {
        return this.speed;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
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

    @Override
    public void update(Observable o, Object arg) {
        Tuple<Float, Float, EnumActions> order = (Tuple<Float, Float, EnumActions>) arg;

        this.x = order.getV1();
        this.y = order.getV2();
        if (order.getV3() == EnumActions.EXPLODE) {
            this.animator.setIndex(EnumAnimationShot.EXPLODE.getValue());
            this.explode = true;
        }
    }
}
