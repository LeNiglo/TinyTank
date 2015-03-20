package com.lefrantguillaume.collisionComponent;

import com.lefrantguillaume.Utils.tools.Debug;
import com.lefrantguillaume.Utils.stockage.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by andres_k on 10/03/2015.
 */
public class CollisionController {
    private List<CollisionObject> items;

    public CollisionController() {
        this.items = new ArrayList<CollisionObject>();
    }

    // FUNCTIONS
    public void addCollisionObject(CollisionObject object) {
        Debug.debug("Add object with user:" + String.valueOf(object.getIdUser() + " and id:" + String.valueOf(object.getId())));
        this.items.add(object);
    }

    public void deleteCollisionObject(UUID id) {
        for (int i = 0; i < this.items.size(); ++i) {
            if (this.items.get(i).getId() == id)
                this.items.remove(i);
        }
    }

    public void clearCollisionObjects(){
        this.items.clear();
    }

    public boolean checkCollision(Pair<Float, Float> coords, UUID id) {
        CollisionObject object = this.getCollisionObject(id);
        if (object != null) {
            object.modifCoord(coords);
            if (this.items.size() != 1) {
                for (int i = 0; i < this.items.size(); ++i) {
                    CollisionObject current = this.items.get(i);
                    if (!current.getIdUser().equals(object.getIdUser())) {
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

    // GETTERS
    public CollisionObject getCollisionObject(UUID id) {
        for (int i = 0; i < this.items.size(); ++i) {
            if (this.items.get(i).getId() == id)
                return this.items.get(i);
        }
        return null;
    }
}
