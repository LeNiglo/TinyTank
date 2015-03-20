package com.lefrantguillaume.collisionComponent;

import com.lefrantguillaume.Utils.stockage.Pair;
import com.lefrantguillaume.Utils.stockage.Tuple;
import com.lefrantguillaume.Utils.tools.Debug;
import com.lefrantguillaume.gameComponent.gameObject.EnumType;

import java.util.Observable;
import java.util.UUID;

/**
 * Created by andres_k on 13/03/2015.
 */
public class CollisionObject extends Observable {
    private final boolean solid;
    private final String idUser;
    private final UUID id;
    private final EnumType type;
    private Pair<Float, Float> positions;
    private Pair<Float, Float> sizes;
    private float angle;
    private float saveX;
    private float saveY;

    public CollisionObject(boolean solid, float x, float y, Pair<Float, Float> sizes, String idUser, UUID id, EnumType type, float angle) {
        this.solid = solid;
        this.type = type;
        this.angle = angle;
        this.positions = new Pair<Float, Float>(x, y);
        this.sizes = sizes;
        this.saveX = x;
        this.saveY = y;
        this.idUser = idUser;
        this.id = id;
    }

    // FUNCTIONS
    public void notifyCollision(EnumType type) {
        this.setChanged();
        Debug.debug("obj id:" + id + " idUser:" + idUser + " nbrObservers:" + this.countObservers());
        this.notifyObservers(new Tuple<Float, Float, EnumType>(this.positions.getV1(), this.positions.getV2(), type));
    }

    public void modifCoord(Pair<Float, Float> coords) {
        this.positions.setV1(coords.getV1());
        this.positions.setV2(coords.getV2());
    }

    public void backToSave() {
        this.positions.setV1(this.saveX);
        this.positions.setV2(this.saveY);
    }

    // GETTERS

    public float getCenterX() {
        return this.positions.getV1() + (this.sizes.getV1() / 2);
    }

    public float getCenterY() {
        return this.positions.getV2() + (this.sizes.getV2() / 2);
    }

    public EnumType getType() {
        return this.type;
    }

    public String getIdUser() {
        return this.idUser;
    }

    public float getX() {
        return this.positions.getV1();
    }

    public float getY() {
        return this.positions.getV2();
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
}
