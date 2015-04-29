package com.lefrantguillaume.gameComponent.gameObject.tanks.equipment;

import com.lefrantguillaume.Utils.stockage.Pair;
import com.lefrantguillaume.Utils.stockage.Tuple;
import com.lefrantguillaume.Utils.tools.RandomTools;
import com.lefrantguillaume.Utils.tools.Rectangle;
import com.lefrantguillaume.gameComponent.animations.Animator;
import com.lefrantguillaume.gameComponent.gameObject.projectiles.EnumShots;
import com.lefrantguillaume.gameComponent.gameObject.projectiles.Shot;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by andres_k on 18/03/2015.
 */
public class TankWeapon {
    private Animator shotAnimator;
    private EnumShots shotType;
    private final float damageShot;
    private final float speedShot;
    private List<Canon> canons;
    private Pair<Float, Float> shiftHitExplode;
    private Pair<Float, Float> shiftHitOrigin;
    private Pair<Float, Float> shiftHitHead;
    private Pair<Float, Float> shiftWeaponOrigin;
    private List<Rectangle> collisionObject;
    private int current;

    public TankWeapon(float speedShot, float damageShot, Pair<Float, Float> shiftWeaponOrigin, Pair<Float, Float> shiftHitExplode, Pair<Float, Float> shiftHitOrigin,
                      Pair<Float, Float> shiftHitHead, Animator shotAnimator, EnumShots shotType) {
        this.shiftWeaponOrigin = new Pair<Float, Float>(shiftWeaponOrigin);
        this.shiftHitOrigin = new Pair<Float, Float>(shiftHitOrigin);
        this.shiftHitExplode = new Pair<Float, Float>(shiftHitExplode);
        this.shiftHitHead = new Pair<Float, Float>(shiftHitHead);
        this.shiftHitExplode = new Pair<Float, Float>(shiftHitExplode);
        this.shotAnimator = new Animator(shotAnimator);
        this.shotType = shotType;
        this.damageShot = damageShot;
        this.speedShot = speedShot;
        this.current = 0;
        this.canons = new ArrayList<Canon>();
        this.collisionObject = new ArrayList<Rectangle>();
    }

    public TankWeapon(TankWeapon tankWeapon) {
        this.shiftWeaponOrigin = new Pair<Float, Float>(tankWeapon.shiftWeaponOrigin);
        this.shiftHitOrigin = new Pair<Float, Float>(tankWeapon.shiftHitOrigin);
        this.shiftHitExplode = new Pair<Float, Float>(tankWeapon.shiftHitExplode);
        this.shiftHitHead = new Pair<Float, Float>(tankWeapon.shiftHitHead);
        this.shiftHitExplode = new Pair<Float, Float>(tankWeapon.shiftHitExplode);
        this.shotAnimator = new Animator(tankWeapon.shotAnimator);
        this.shotType = tankWeapon.shotType;
        this.damageShot = tankWeapon.damageShot;
        this.speedShot = tankWeapon.speedShot;
        this.current = 0;
        this.canons = new ArrayList<Canon>();
        this.collisionObject = new ArrayList<Rectangle>();
        for (int i = 0; i < tankWeapon.canons.size(); ++i) {
            this.canons.add(tankWeapon.canons.get(i));
        }
        for (int i = 0; i < tankWeapon.collisionObject.size(); ++i) {
            this.collisionObject.add(tankWeapon.collisionObject.get(i));
        }
    }

    // FUNCTIONS

    public Shot generateShot(String userId, UUID id, float angle, Pair<Float, Float> coord) {
        float x1 = this.canons.get(this.current).getShiftCanonHead().getV1();
        float y1 = this.canons.get(this.current).getShiftCanonHead().getV2();
        double radAngle = angle * Math.PI / 180;

        float x = (float) (x1 * Math.cos(radAngle) - y1 * Math.sin(radAngle) + coord.getV1());
        float y = (float) (x1 * Math.sin(radAngle) + y1 * Math.cos(radAngle) + coord.getV2());

        Tuple<Float, Float, Float> newCoord = new Tuple<Float, Float, Float>(x, y, angle);
        Shot shot = new Shot(userId, id, this.getDamageShot(), this.getSpeedShot(), new Animator(this.getShotAnimator()), newCoord, new Pair<Float, Float>(this.shiftHitOrigin),
                new Pair<Float, Float>(this.getShiftHitExplode()), new Pair<Float, Float>(this.shiftHitHead));

        for (int i = 0; i < this.collisionObject.size(); ++i) {
            shot.addCollisionObject(this.collisionObject.get(i));
        }
        this.nextCanon();
        return shot;
    }

    public void addCollisionObject(Rectangle rectangle) {
        this.collisionObject.add(rectangle);
    }

    public void nextCanon() {
        this.current += 1;
        if (this.current == this.canons.size())
            this.current = 0;
    }

    public void prevCanon() {
        this.current -= 1;
        if (this.current < 0)
            this.current = this.canons.size() - 1;
    }

    public void addCanon(Canon canon) {
        this.canons.add(canon);
    }

    // GETETRS
    public float getDamageShot() {
        return this.damageShot;
    }

    public float getSpeedShot() {
        return this.speedShot;
    }

    public Animator getShotAnimator() {
        return this.shotAnimator;
    }

    public EnumShots getShotType() {
        return shotType;
    }

    public Canon getCurrentCanon() {
        return this.canons.get(this.current);
    }

    public Pair<Float, Float> getShiftHitExplode() {
        return this.shiftHitExplode;
    }

    public Pair<Float, Float> getShiftHitOrigin() {
        return this.shiftHitOrigin;
    }

    public Pair<Float, Float> getShiftWeaponOrigin() {
        return this.shiftWeaponOrigin;
    }

    public float getGraphicalX(float value) {
        return value + this.shiftWeaponOrigin.getV1();
    }

    public float getGraphicalY(float value) {
        return value + this.shiftWeaponOrigin.getV2();
    }
}
