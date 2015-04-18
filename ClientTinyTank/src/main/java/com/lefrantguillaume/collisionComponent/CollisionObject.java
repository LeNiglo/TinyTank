package com.lefrantguillaume.collisionComponent;

import com.lefrantguillaume.Utils.stockage.Pair;
import com.lefrantguillaume.Utils.stockage.Tuple;
import com.lefrantguillaume.Utils.tools.Debug;
import com.lefrantguillaume.Utils.tools.Rectangle;
import com.lefrantguillaume.gameComponent.gameObject.EnumType;

import java.util.Observable;
import java.util.Observer;
import java.util.UUID;

/**
 * Created by andres_k on 13/03/2015.
 */
public class CollisionObject extends Observable implements Observer{
    private final boolean solid;
    private final String idUser;
    private final UUID id;
    private final EnumType type;
    private Pair<Float, Float> positions;
    private Pair<Float, Float> sizes;
    private Pair<Float, Float> shiftOrigin;
    private float angle;
    private float saveX;
    private float saveY;
    private boolean destroyed = false;

    public CollisionObject(boolean solid,Pair<Float, Float> positions, Pair<Float, Float> sizes, Pair<Float, Float> shiftOrigin, String idUser, UUID id, EnumType type, float angle) {
        this.solid = solid;
        this.shiftOrigin = new Pair<Float, Float>(shiftOrigin);
        this.positions = new Pair<Float, Float>(positions);
        this.sizes = new Pair<Float, Float>(sizes);
        this.type = type;
        this.angle = angle;
        this.saveX = positions.getV1();
        this.saveY = positions.getV2();
        this.idUser = idUser;
        this.id = id;
    }

    // FUNCTIONS
    @Override
    public void update(Observable o, Object arg) {
        Rectangle coord = (Rectangle) arg;

        if (coord != null) {
            this.positions.setV1(coord.getShiftOrigin().getV1());
            this.positions.setV2(coord.getShiftOrigin().getV2());
            this.sizes.setV1(coord.getSizes().getV1());
            this.sizes.setV2(coord.getSizes().getV2());
        }
        else {
            this.destroyed = true;
        }
    }

    public void notifyCollision(EnumType type) {
        this.setChanged();
        this.notifyObservers(new Tuple<Float, Float, EnumType>(this.positions.getV1(), this.positions.getV2(), type));
    }

    public void modifCoord(Pair<Float, Float> coords) {
        this.saveX = this.positions.getV1();
        this.saveY = this.positions.getV2();
        this.positions.setV1(this.positions.getV1() + coords.getV1());
        this.positions.setV2(this.positions.getV2() + coords.getV2());
    }

    public void backToSave() {
        this.positions.setV1(this.saveX);
        this.positions.setV2(this.saveY);
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

    public void setX(float x){
        this.positions.setV1(x);
    }

    public void setY(float y){
        this.positions.setV2(y);
    }

    public boolean isDestroyed() {
        return destroyed;
    }
}
