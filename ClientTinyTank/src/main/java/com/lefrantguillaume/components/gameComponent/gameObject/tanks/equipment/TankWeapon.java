package com.lefrantguillaume.components.gameComponent.gameObject.tanks.equipment;

import com.lefrantguillaume.Utils.stockage.Pair;
import com.lefrantguillaume.Utils.stockage.Tuple;
import com.lefrantguillaume.Utils.tools.Block;
import com.lefrantguillaume.components.gameComponent.animations.Animator;
import com.lefrantguillaume.components.gameComponent.gameObject.EnumGameObject;
import com.lefrantguillaume.components.gameComponent.gameObject.projectiles.Shot;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.items.ActivatedTimer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andres_k on 18/03/2015.
 */
public class TankWeapon {
    private Animator shotAnimator;
    private ActivatedTimer activatedTimer;
    private EnumGameObject shotType;
    private final float damageShot;
    private final float speedShot;
    private List<Canon> canons;
    private Pair<Float, Float> shiftHitExplode;
    private Pair<Float, Float> shiftHitOrigin;
    private Pair<Float, Float> shiftHitHead;
    private Pair<Float, Float> shiftWeaponOrigin;
    private List<Block> collisionObject;
    private int current;

    public TankWeapon(float speedShot, float damageShot, Pair<Float, Float> shiftWeaponOrigin, Pair<Float, Float> shiftHitExplode, Pair<Float, Float> shiftHitOrigin,
                      Pair<Float, Float> shiftHitHead, Animator shotAnimator, EnumGameObject shotType) {
        this.shiftWeaponOrigin = new Pair<>(shiftWeaponOrigin);
        this.shiftHitOrigin = new Pair<>(shiftHitOrigin);
        this.shiftHitExplode = new Pair<>(shiftHitExplode);
        this.shiftHitHead = new Pair<>(shiftHitHead);
        this.shiftHitExplode = new Pair<>(shiftHitExplode);
        this.shotAnimator = new Animator(shotAnimator);
        this.shotType = shotType;
        this.damageShot = damageShot;
        this.speedShot = speedShot;
        this.current = 0;
        this.canons = new ArrayList<>();
        this.collisionObject = new ArrayList<>();
        this.activatedTimer = new ActivatedTimer(true, false, 1000);
    }

    public TankWeapon(TankWeapon tankWeapon) {
        this.shiftWeaponOrigin = new Pair<>(tankWeapon.shiftWeaponOrigin);
        this.shiftHitOrigin = new Pair<>(tankWeapon.shiftHitOrigin);
        this.shiftHitExplode = new Pair<>(tankWeapon.shiftHitExplode);
        this.shiftHitHead = new Pair<>(tankWeapon.shiftHitHead);
        this.shiftHitExplode = new Pair<>(tankWeapon.shiftHitExplode);
        this.shotAnimator = new Animator(tankWeapon.shotAnimator);
        this.shotType = tankWeapon.shotType;
        this.damageShot = tankWeapon.damageShot;
        this.speedShot = tankWeapon.speedShot;
        this.current = 0;
        this.canons = new ArrayList<>();
        this.collisionObject = new ArrayList<>();
        for (int i = 0; i < tankWeapon.canons.size(); ++i) {
            this.canons.add(tankWeapon.canons.get(i));
        }
        for (int i = 0; i < tankWeapon.collisionObject.size(); ++i) {
            this.collisionObject.add(tankWeapon.collisionObject.get(i));
        }
        this.activatedTimer = new ActivatedTimer(tankWeapon.activatedTimer);
    }

    // FUNCTIONS

    public Shot generateShot(String userId, String id, float angle, Pair<Float, Float> coord) {
        if (this.activatedTimer.isActivated()) {
            float x1 = this.canons.get(this.current).getShiftCanonHead().getV1();
            float y1 = this.canons.get(this.current).getShiftCanonHead().getV2();
            double radAngle = angle * Math.PI / 180;

            float x = (float) (x1 * Math.cos(radAngle) - y1 * Math.sin(radAngle) + coord.getV1());
            float y = (float) (x1 * Math.sin(radAngle) + y1 * Math.cos(radAngle) + coord.getV2());

            Tuple<Float, Float, Float> newCoord = new Tuple<>(x, y, angle);
            Shot shot = new Shot(userId, id, this.shotType, this.getDamageShot(), this.getSpeedShot(), new Animator(this.getShotAnimator()), newCoord, new Pair<>(this.shiftHitOrigin),
                    new Pair<>(this.getShiftHitExplode()), new Pair<>(this.shiftHitHead));

            for (int i = 0; i < this.collisionObject.size(); ++i) {
                shot.addCollisionObject(this.collisionObject.get(i));
            }
            this.nextCanon();
            this.activatedTimer.setActivated(false);
            this.activatedTimer.startTimer();
            return shot;
        }
        return null;
    }

    public void addCollisionObject(Block block) {
        this.collisionObject.add(block);
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

    public EnumGameObject getShotType() {
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

    public long getCooldown(){
        return this.activatedTimer.getDelay();
    }

    public boolean isActivated(){
        return this.activatedTimer.isActivated();
    }
}
