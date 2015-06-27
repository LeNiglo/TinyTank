package com.lefrantguillaume.components.collisionComponent;

import com.lefrantguillaume.Utils.stockage.Pair;
import com.lefrantguillaume.Utils.stockage.Tuple;
import com.lefrantguillaume.Utils.tools.Block;
import com.lefrantguillaume.components.gameComponent.gameObject.EnumGameObject;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by andres_k on 13/03/2015.
 */
public class CollisionObject extends Observable implements Observer {
    private List<EnumGameObject> ignoredObject;
    private final String idUser;
    private final String id;
    private final EnumGameObject type;
    private Pair<Float, Float> positions;
    private Pair<Float, Float> sizes;
    private Pair<Float, Float> shiftOrigin;
    private Pair<Float, Float> savePositions;
    private float angle;
    private boolean alive;
    private boolean destroyed;
    private CollisionObject saveCollisionObject;

    public CollisionObject(List<EnumGameObject> ignoredObject, Pair<Float, Float> positions, Pair<Float, Float> sizes, Pair<Float, Float> shiftOrigin,
                           String idUser, String id, EnumGameObject type, float angle) {
        this.ignoredObject = new ArrayList<>();
        this.ignoredObject.addAll(ignoredObject);
        this.shiftOrigin = new Pair<>(shiftOrigin);
        this.positions = new Pair<>(positions);
        this.sizes = new Pair<>(sizes);
        this.savePositions = new Pair<>(positions);
        this.type = type;
        this.angle = angle;
        this.idUser = idUser;
        this.id = id;
        this.alive = true;
        this.destroyed = false;
        this.saveCollisionObject = null;
    }

    // FUNCTIONS
    @Override
    public void update(Observable o, Object arg) {

        if (arg instanceof Block) { // modifier la position/size de la collision
            Block block = (Block) arg;

            this.savePositions.setV1(block.getShiftOrigin().getV1());
            this.savePositions.setV2(block.getShiftOrigin().getV2());
            this.positions.setV1(block.getShiftOrigin().getV1());
            this.positions.setV2(block.getShiftOrigin().getV2());
            this.sizes.setV1(block.getSizes().getV1());
            this.sizes.setV2(block.getSizes().getV2());
        } else if (arg instanceof Pair) { // changer la position
            Pair<Float, Float> coords = (Pair<Float, Float>) arg;

            this.savePositions.setV1(coords.getV1());
            this.savePositions.setV2(coords.getV2());
            this.positions.setV1(coords.getV1());
            this.positions.setV2(coords.getV2());
        } else if (arg instanceof Tuple) { //revive
            Tuple<Boolean, Float, Float> values = (Tuple<Boolean, Float, Float>) arg;

            if (values.getV1() == false) {
                this.alive = false;
                //Debug.debug(this.type + ": collision DIE");
            } else {
              //  Debug.debug(this.type + ": collision REVIVE");
                this.alive = true;
                this.savePositions.setV1(values.getV2());
                this.savePositions.setV2(values.getV3());
                this.positions.setV1(values.getV2());
                this.positions.setV2(values.getV3());
            }
        } else if (arg instanceof ArrayList) {
            this.ignoredObject.clear();
            this.ignoredObject.addAll((ArrayList<EnumGameObject>) arg);
        } else {
            this.alive = false;
            this.destroyed = true;
            //Debug.debug(this.type + ": collision DESTROYED");
        }
    }

    public boolean checkLastCollision(CollisionObject collisionObject) {
        if (this.saveCollisionObject == null) {
            return false;
        }
        //Debug.debug("CHECK LAST : " + collisionObject.getId() + " =? " + this.saveCollisionObject.getId());
        if (collisionObject.getId().equals(this.saveCollisionObject.getId())) {
            return true;
        }
        return false;
    }

    public boolean isObjectIgnored(CollisionObject object) {
        if (this.isTypeIgnored(this, object.getType())) {
            return true;
        } else if (this.isTypeIgnored(object, this.type)) {
            return true;
        }
        return false;
    }

    public boolean isTypeIgnored(CollisionObject object, EnumGameObject type) {
        List<EnumGameObject> values = object.getIgnoredList();

        if (values == null)
            return false;
//        Debug.debug("ignored size: " + values.size() + " on " + object);
        for (int i = 0; i < values.size(); ++i) {
  //          Debug.debug(type.getValue() + " =? " + values.get(i).getValue());
            if (values.get(i) == type) {
                return true;
            }
        }
        return false;
    }

    public void notifyCollision(EnumGameObject type) {
        this.setChanged();
        this.notifyObservers(new Tuple<>(this.positions.getV1(), this.positions.getV2(), type));
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

    public boolean canDoCollisionWithObject(CollisionObject object) {
        if (this.checkLastCollision(object) == true) {
            return false;
        } else {
            return true;
        }
    }

    // GETTERS

    public float getX() {
        return this.positions.getV1();
    }

    public float getY() {
        return this.positions.getV2();
    }

    public EnumGameObject getType() {
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

    public String getId() {
        return this.id;
    }

    public float getAngle() {
        return this.angle;
    }

    public float getRadian() {
        return (float) (this.angle * Math.PI / 180);
    }

    public List<EnumGameObject> getIgnoredList() {
        return this.ignoredObject;
    }

    public CollisionObject getSaveCollisionObject() {
        return this.saveCollisionObject;
    }

    public boolean isDestroyed() {
        return this.destroyed;
    }

    public boolean isAlive() {
        return this.alive;
    }

    public Shape getShape() {
        Shape shape;
        if (this.getSizeY() == -1) {
            shape = new Circle(this.getX(), this.getY(), this.getSizeX());
        } else {
            shape = new Rectangle(this.getOriginX(), this.getOriginY(), this.getSizeX(), this.getSizeY());
        }
        shape = shape.transform(Transform.createRotateTransform(this.getRadian(), this.getX(), this.getY()));
        return shape;
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

    public void setSaveCollisionObject(CollisionObject saveCollisionObject) {
        this.saveCollisionObject = saveCollisionObject;
    }

    @Override
    public String toString() {
        return "Id: " + this.getId() + " with type: " + this.getType() + " and idUser: " + this.getIdUser() + " and isAline: " + this.isAlive();
    }
}
