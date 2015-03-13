package com.lefrantguillaume.collisionComponent;

import com.lefrantguillaume.Utils.stockage.Pair;
import com.lefrantguillaume.Utils.stockage.Tuple;
import com.lefrantguillaume.gameComponent.actions.EnumActions;
import com.lefrantguillaume.gameComponent.tanks.EnumType;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Created by andres_k on 13/03/2015.
 */
public class CollisionObject extends Observable {
    private float x;
    private float y;
    private float saveX;
    private float saveY;
    private float sizeX;
    private float sizeY;
    private int idUser;
    private int id;
    private List<Integer> areaId;
    private EnumType type;

    public CollisionObject(float x, float y, float sizeX, float sizeY, int idUser, int id, EnumType type) {
        this.type = type;
        this.areaId = new ArrayList<Integer>();
        this.x = x;
        this.y = y;
        this.saveX = x;
        this.saveY = y;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.idUser = idUser;
        this.id = id;
    }

    public void notifyCollision() {
        this.setChanged();
        this.notifyObservers(true);
    }

    public EnumType getType(){
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

    public void modifCoord(Pair<Float, Float> coords) {
        this.x = coords.getKey();
        this.y = coords.getValue();
    }

    public void backToSave() {
        this.x = this.saveX;
        this.y = this.saveY;
    }

    public void alertObservers(){
        this.setChanged();
        this.notifyObservers(new Tuple<Float, Float, EnumActions>(this.x, this.y, EnumActions.EXPLODE));
    }

    public void addAreaId(int id) {
        this.areaId.add(id);
    }

    public List<Integer> getAreaId() {
        return this.areaId;
    }

    public int getId() {
        return this.id;
    }
}
