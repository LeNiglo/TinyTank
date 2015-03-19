package com.lefrantguillaume.collisionComponent;

import com.lefrantguillaume.Utils.tools.Debug;
import com.lefrantguillaume.Utils.stockage.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andres_k on 10/03/2015.
 */
public class CollisionController {
    private List<CollisionObject> items;
//    private List<Pair<CollisionObject, List<CollisionObject>>> areas;

    public CollisionController() {
        this.items = new ArrayList<CollisionObject>();
/*
        this.areas = new ArrayList<Pair<CollisionObject, List<CollisionObject>>>();
        this.areas.add(new Pair<CollisionObject, List<CollisionObject>>(new CollisionObject(0, 0, WindowConfig.getSizeX() / 2, WindowConfig.getSizeY() / 2, -1, -1), new ArrayList<CollisionObject>()));
        this.areas.add(new Pair<CollisionObject, List<CollisionObject>>(new CollisionObject(WindowConfig.getSizeX() / 2, 0, WindowConfig.getSizeX() / 2, WindowConfig.getSizeY() / 2, -2, -2),
                new ArrayList<CollisionObject>()));
        this.areas.add(new Pair<CollisionObject, List<CollisionObject>>(new CollisionObject(0, WindowConfig.getSizeY() / 2, WindowConfig.getSizeX() / 2, WindowConfig.getSizeY() / 2, -3, -3),
                new ArrayList<CollisionObject>()));
        this.areas.add(new Pair<CollisionObject, List<CollisionObject>>(new CollisionObject(WindowConfig.getSizeX() / 2, WindowConfig.getSizeY() / 2, WindowConfig.getSizeX() / 2, WindowConfig.getSizeY() / 2, -4, -4),
                new ArrayList<CollisionObject>()));
*/
    }

    public void addCollisionObject(CollisionObject object) {
        Debug.debug("Add object with user:" + String.valueOf(object.getIdUser() + " and id:" + String.valueOf(object.getId())));
        this.items.add(object);
/*
        if (CollisionDetection.checkCollision(areas.get(0).getV1(), object)) {
            object.addAreaId(0);
            this.areas.get(0).getId().add(object);
        }
        if (CollisionDetection.checkCollision(areas.get(1).getV1(), object)) {
            object.addAreaId(1);
            this.areas.get(1).getId().add(object);
        }
        if (CollisionDetection.checkCollision(areas.get(2).getV1(), object)) {
            object.addAreaId(2);
            this.areas.get(2).getId().add(object);
        }
        if (CollisionDetection.checkCollision(areas.get(3).getV1(), object)) {
            object.addAreaId(3);
            this.areas.get(3).getId().add(object);
        }
*/
    }

    public void deleteCollisionObject(int id) {
        for (int i = 0; i < this.items.size(); ++i) {
            if (this.items.get(i).getId() == id)
                this.items.remove(i);
        }
    }

    public void actualiseArea(CollisionObject object) {
/*
        if (CollisionDetection.checkCollision(areas.get(0).getV1(), object)) {
            if (!object.getAreaId().contains(0)) {
                object.addAreaId(0);
                this.areas.get(0).getId().add(object);
            }
        } else {
            if (object.getAreaId().contains(0)) {
                int index = this.getCollisionObjectIndex(object.getId());
                Debug.debug("delete index:" + String.valueOf(index));
                this.areas.get(0).getId().remove(index);
            }
        }
        if (CollisionDetection.checkCollision(areas.get(1).getV1(), object)) {
            if (!object.getAreaId().contains(1)) {
                object.addAreaId(1);
                this.areas.get(1).getId().add(object);
            }
        } else {
            if (object.getAreaId().contains(1)) {
                this.areas.get(1).getId().remove(this.getCollisionObjectIndex(object.getId()));
            }
        }
        if (CollisionDetection.checkCollision(areas.get(2).getV1(), object)) {
            if (!object.getAreaId().contains(2)) {
                Debug.debug("add item");
                object.addAreaId(2);
                this.areas.get(2).getId().add(object);
            }
        } else {
            if (object.getAreaId().contains(2)) {
                this.areas.get(2).getId().remove(this.getCollisionObjectIndex(object.getId()));
            }
        }
        if (CollisionDetection.checkCollision(areas.get(3).getV1(), object)) {
            if (!object.getAreaId().contains(3)) {
                object.addAreaId(3);
                this.areas.get(3).getId().add(object);
            }
        } else {
            if (object.getAreaId().contains(3)) {
                this.areas.get(3).getId().remove(this.getCollisionObjectIndex(object.getId()));
            }
        }
  */
    }

    /*
        public Pair<CollisionObject, List<CollisionObject>> getArea(int index) {
            return this.areas.get(index);
        }
    */
    public CollisionObject getCollisionObject(int id) {
        for (int i = 0; i < this.items.size(); ++i) {
            if (this.items.get(i).getId() == id)
                return this.items.get(i);

/*
            for (int i2 = 0; i2 < this.areas.get(i).getId().size(); ++i2) {
                if (this.areas.get(i).getId().get(i2).getId() == id)
                    return this.areas.get(i).getId().get(i2);
            }
        }
 */
        }
        return null;
    }

    /*
    public int getCollisionObjectIndex(int id) {
        for (int i = 0; i < this.areas.size(); ++i) {
            for (int i2 = 0; i2 < this.areas.get(i).getId().size(); ++i2) {
                if (this.areas.get(i).getId().get(i2).getId() == id) {
                    return i2;
                }
            }
        }
        Debug.debug("fail");
        return -1;
    }
*/
    public boolean checkCollision(Pair<Float, Float> coords, int id) {
        CollisionObject object = this.getCollisionObject(id);
        if (object != null) {
            object.modifCoord(coords);
            this.actualiseArea(object);
            if (this.items.size() != 1) {
                for (int i = 0; i < this.items.size(); ++i) {
                    CollisionObject current = this.items.get(i);
                    if (current.getIdUser() != object.getIdUser()) {
                        if (CollisionDetection.checkCollision(object, current)) {
                            Debug.debug("delete");
                            object.notifyCollision(current.getType());
                            if (current.isSolid()) {
                                object.backToSave();
                            }
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
  /*      CollisionObject object = this.getCollisionObject(id);
        if (object != null) {
            object.modifCoord(coords);
            this.actualiseArea(object);
            for (int i = 0; i < object.getAreaId().size(); ++i) {
                if (this.areas.get(i).getId().size() != 1) {
                    Debug.debug(String.valueOf(this.areas.get(i).getId().size()));
                    for (int i2 = 0; i2 < this.areas.get(i).getId().size(); ++i2) {
                        CollisionObject current = this.areas.get(i).getId().get(i2);
                        if (current.getIdUser() != object.getIdUser()) {
                            if (CollisionDetection.checkCollision(object, current)) {
                                object.backToSave();
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
    */
}
