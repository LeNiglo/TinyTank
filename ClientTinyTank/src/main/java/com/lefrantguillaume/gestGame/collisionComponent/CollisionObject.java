package com.lefrantguillaume.gestGame.collisionComponent;

import com.lefrantguillaume.gestGame.Utils.stockage.Pair;
import com.lefrantguillaume.gestGame.Utils.stockage.Tuple;
import com.lefrantguillaume.gestGame.Utils.tools.Rectangle;
import com.lefrantguillaume.gestGame.gameComponent.gameObject.EnumType;

import java.util.Observable;
import java.util.Observer;
import java.util.UUID;

/**
 * Created by andres_k on 13/03/2015.
 */
public class CollisionObject extends Observable implements Observer {
    private final boolean solid;
    private final String idUser;
    private final UUID id;
    private final EnumType type;
    private Pair<Float, Float> positions;
    private Pair<Float, Float> sizes;
    private Pair<Float, Float> shiftOrigin;
    private Pair<Float, Float> savePositions;
    private float angle;
    private boolean destroyed = false;
    private boolean alive = true;

    public CollisionObject(boolean solid, Pair<Float, Float> positions, Pair<Float, Float> sizes, Pair<Float, Float> shiftOrigin, String idUser, UUID id, EnumType type, float angle) {
        this.solid = solid;
        this.shiftOrigin = new Pair<Float, Float>(shiftOrigin);
        this.positions = new Pair<Float, Float>(positions);
        this.savePositions = new Pair<Float, Float>(positions);
        this.sizes = new Pair<Float, Float>(sizes);
        this.type = type;
        this.angle = angle;
        this.idUser = idUser;
        this.id = id;
    }

    // FUNCTIONS
    @Override
    public void update(Observable o, Object arg) {

        if (arg instanceof Rectangle) { // modifier la position/size de la collision
            Rectangle coord = (Rectangle) arg;

            if (coord != null) {
                this.savePositions.setV1(coord.getShiftOrigin().getV1());
                this.savePositions.setV2(coord.getShiftOrigin().getV2());
                this.positions.setV1(coord.getShiftOrigin().getV1());
                this.positions.setV2(coord.getShiftOrigin().getV2());
                this.sizes.setV1(coord.getSizes().getV1());
                this.sizes.setV2(coord.getSizes().getV2());
            }
        } else if (arg instanceof Tuple) { //changer la position/revive
            Tuple<Boolean, Float, Float> values = (Tuple<Boolean, Float, Float>) arg;

            if (values != null) {
                if (values.getV1() == false) {
                    this.alive = false;
                } else if (values.getV1() == true) {
                    this.alive = true;
                    this.savePositions.setV1(values.getV2());
                    this.savePositions.setV2(values.getV3());
                    this.positions.setV1(values.getV2());
                    this.positions.setV2(values.getV3());
                }
            }
        } else {
            this.alive = false;
            this.destroyed = true;
        }
    }

    public void notifyCollision(EnumType type) {
        this.setChanged();
        this.notifyObservers(new Tuple<Float, Float, EnumType>(this.positions.getV1(), this.positions.getV2(), type));
    }

    public void modifCoord(Pair<Float, Float> coords) {
        this.savePositions.setV1(this.positions.getV1());
        this.savePositions.setV2(this.positions.getV2());
        this.positions.setV1(coords.getV1());
        this.positions.setV2(coords.getV2());
    }

    public void backToSave() {
        this.positions.setV1(this.savePositions.getV1());
        this.positions.setV2(this.savePositions.getV2());
    }

    // GETTERS

    public float getX() {
        return this.positions.getV1();
    }

    public float getY() {
        return this.positions.getV2();
    }

    public EnumType getType() {
        return this.type;
    }

    public String getIdUser() {
        return this.idUser;
    }

    public float getOriginX() {
        return this.positions.getV1() + this.shiftOrigin.getV1();
    }

    public float getOriginY() {
        return this.positions.getV2() + this.shiftOrigin.getV2();
    }

    public float getSizeX() {
        return this.sizes.getV1();
    }

    public float getSizeY() {
        return this.sizes.getV2();
    }

    public UUID getId() {
        return this.id;
    }

    public boolean isSolid() {
        return solid;
    }

    public float getAngle() {
        return this.angle;
    }

    public float getRadian() {
        return (float) (this.angle * Math.PI / 180);
    }

    // SETTERS
    public void setAngle(float angle) {
        this.angle = angle;
    }

    public void setX(float x) {
        this.positions.setV1(x);
    }

    public void setY(float y) {
        this.positions.setV2(y);
    }

    public void setSaveX(float x) {
        this.savePositions.setV1(x);
    }

    public void setSaveY(float y) {
        this.savePositions.setV1(y);
    }

    public boolean isAlive() {
        return this.alive;
    }

    public boolean isDestroyed() {
        return this.destroyed;
    }
}
