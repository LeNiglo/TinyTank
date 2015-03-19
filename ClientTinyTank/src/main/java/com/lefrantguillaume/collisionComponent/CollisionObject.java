package com.lefrantguillaume.collisionComponent;

import com.lefrantguillaume.Utils.stockage.Pair;
import com.lefrantguillaume.Utils.stockage.Tuple;
import com.lefrantguillaume.Utils.tools.Debug;
import com.lefrantguillaume.gameComponent.actions.EnumActions;
import com.lefrantguillaume.gameComponent.gameObject.EnumType;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Created by andres_k on 13/03/2015.
 */
public class CollisionObject extends Observable {
    private final boolean solid;
    private final int idUser;
    private final int id;
    private final EnumType type;
    private float x;
    private float y;
    private float saveX;
    private float saveY;
    private float sizeX;
    private float sizeY;

    public CollisionObject(boolean solid, float x, float y, float sizeX, float sizeY, int idUser, int id, EnumType type) {
        this.solid = solid;
        this.type = type;
        this.x = x;
        this.y = y;
        this.saveX = x;
        this.saveY = y;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.idUser = idUser;
        this.id = id;
    }

    // FUNCTIONS
    public void notifyCollision(EnumType type) {
        this.setChanged();
        Debug.debug("obj id:" + id + " idUser:" + idUser + " nbrObservers:"+ this.countObservers());
        this.notifyObservers(new Tuple<Float, Float, EnumType>(this.x, this.y, type));
    }

    public void modifCoord(Pair<Float, Float> coords) {
        this.x = coords.getV1();
        this.y = coords.getV2();
    }

    public void backToSave() {
        this.x = this.saveX;
        this.y = this.saveY;
    }

    // GETTERS

    public EnumType getType() {
        return this.type;
    }

    public int getIdUser() {
        return this.idUser;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public float getSizeX() {
        return this.sizeX;
    }

    public float getSizeY() {
        return this.sizeY;
    }

    public int getId() {
        return this.id;
    }

    public boolean isSolid() {
        return solid;
    }
}
