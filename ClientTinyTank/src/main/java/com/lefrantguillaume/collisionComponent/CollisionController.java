package com.lefrantguillaume.collisionComponent;

import com.lefrantguillaume.Utils.stockage.Pair;
import com.lefrantguillaume.Utils.tools.Debug;
import com.lefrantguillaume.gameComponent.controllers.MapController;
import com.lefrantguillaume.gameComponent.gameObject.EnumType;

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
    public void createWorld(MapController map) {
        Pair<Float, Float> pos1 = new Pair<Float, Float>(map.getSizeX() / 2, 0f);
        Pair<Float, Float> pos2 = new Pair<Float, Float>(map.getSizeX() / 2, map.getSizeY());
        Pair<Float, Float> pos3 = new Pair<Float, Float>(0f, map.getSizeY() / 2);
        Pair<Float, Float> pos4 = new Pair<Float, Float>(map.getSizeX(), map.getSizeY() / 2);
        Pair<Float, Float> size1 = new Pair<Float, Float>(map.getSizeX(), 10f);
        Pair<Float, Float> size2 = new Pair<Float, Float>(10f, map.getSizeY());
        Pair<Float, Float> origin1 = new Pair<Float, Float>(-map.getSizeX() / 2, -10f);
        Pair<Float, Float> origin2 = new Pair<Float, Float>(-map.getSizeX() / 2, 0f);
        Pair<Float, Float> origin3 = new Pair<Float, Float>(-10f, -map.getSizeY() / 2);
        Pair<Float, Float> origin4 = new Pair<Float, Float>(0f, -map.getSizeY() / 2);

        this.addCollisionObject(new CollisionObject(true, pos1, size1, origin1, "admin", UUID.randomUUID().toString(), EnumType.UNBREAKABLE, 0));
        this.addCollisionObject(new CollisionObject(true, pos2, size1, origin2, "admin", UUID.randomUUID().toString(), EnumType.UNBREAKABLE, 0));
        this.addCollisionObject(new CollisionObject(true, pos3, size2, origin3, "admin", UUID.randomUUID().toString(), EnumType.UNBREAKABLE, 0));
        this.addCollisionObject(new CollisionObject(true, pos4, size2, origin4, "admin", UUID.randomUUID().toString(), EnumType.UNBREAKABLE, 0));

    }

    public void addCollisionObject(CollisionObject object) {
        Debug.debug("Add object with user:" + String.valueOf(object.getIdUser() + " and id:" + String.valueOf(object.getId())));
        this.items.add(object);
    }

    public void deleteCollisionObject(String id) {
        for (int i = 0; i < this.items.size(); ++i) {
            if (this.items.get(i).getId().equals(id)) {
                this.items.remove(i);
                --i;
            }
        }
    }

    public void clearCollisionObjects() {
        this.items.clear();
    }

    public Pair<Boolean, Pair<String, String>> checkCollision(Pair<Float, Float> coords, String id) {
        List<CollisionObject> objects = this.getCollisionObject(id);
        try {
            if (!objects.isEmpty()) {
                for (int i = 0; i < objects.size(); ++i) {
                    objects.get(i).modifCoord(coords);
                    for (int i2 = 0; i2 < this.items.size(); ++i2) {
                        CollisionObject current = this.items.get(i2);
                        if (current.isAlive()) {
                            if (current.getIdUser().equals(objects.get(i).getIdUser()) == false) {
                                if (CollisionDetection.checkCollision(objects.get(i), current) == true) {
                                    objects.get(i).notifyCollision(current.getType());
                                    current.notifyCollision(current.getType());
                                    if (current.isSolid()) {
                                        objects.get(i).backToSave();
                                    }
                                    if (current.getType() == EnumType.UNBREAKABLE) {
                                        return new Pair<Boolean, Pair<String, String>>(false, new Pair<String, String>(objects.get(i).getId().toString(), current.getId().toString()));
                                    }
                                    return new Pair<Boolean, Pair<String, String>>(true, new Pair<String, String>(objects.get(i).getId().toString(), current.getId().toString()));
                                }
                            }
                        }
                    }
                }
                return new Pair<Boolean, Pair<String, String>>(false, new Pair<String, String>("null", "null"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void cleanCollision() {
        for (int i = 0; i < this.items.size(); ++i) {
            CollisionObject current = this.items.get(i);
            if (current != null && current.isDestroyed()) {
                this.items.remove(i);
            }
        }
    }

    // GETTERS

    public List<CollisionObject> getCollisionObject(String id) {
        List<CollisionObject> result = new ArrayList<CollisionObject>();
        for (int i = 0; i < this.items.size(); ++i) {
            if (this.items.get(i) != null) {
                if (this.items.get(i).getId().equals(id)) {
                    result.add(this.items.get(i));
                }
            }
        }
        return result;
    }

    public List<CollisionObject> getCollisionObjects() {
        return this.items;
    }
}
