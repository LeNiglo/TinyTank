package com.lefrantguillaume.components.collisionComponent;

import com.lefrantguillaume.Utils.stockage.Pair;
import com.lefrantguillaume.Utils.stockage.Tuple;
import com.lefrantguillaume.Utils.tools.Debug;
import com.lefrantguillaume.Utils.tools.MathTools;
import com.lefrantguillaume.components.gameComponent.controllers.MapController;
import org.newdawn.slick.geom.Rectangle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by andres_k on 10/03/2015.
 */
public class CollisionController {
    private List<CollisionObject> items;

    public CollisionController() {
        this.items = new ArrayList<>();
    }

    // FUNCTIONS
    public void createWorld(MapController map) {
        /*
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

        this.addCollisionObject(new CollisionObject(null, pos1, size1, origin1, "admin", UUID.randomUUID().toString(), EnumGameObject.UNBREAKABLE, 0));
        this.addCollisionObject(new CollisionObject(null, pos2, size1, origin2, "admin", UUID.randomUUID().toString(), EnumGameObject.UNBREAKABLE, 0));
        this.addCollisionObject(new CollisionObject(null, pos3, size2, origin3, "admin", UUID.randomUUID().toString(), EnumGameObject.UNBREAKABLE, 0));
        this.addCollisionObject(new CollisionObject(null, pos4, size2, origin4, "admin", UUID.randomUUID().toString(), EnumGameObject.UNBREAKABLE, 0));
*/
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
    
    public Tuple<Boolean, Boolean, Pair<String, String>> checkCollision(Pair<Float, Float> coords, String id) {
        HashMap<Integer, Tuple<Boolean, Boolean, Pair<String, String>>> listSaveActions = new HashMap<>();

        List<CollisionObject> objects = this.getCollisionObject(id);
        if (!objects.isEmpty()) {
            for (int i = 0; i < this.items.size(); ++i) {
                CollisionObject current = this.items.get(i);
                Debug.debug("--------------------" + i + "--------------------");

                for (CollisionObject object : objects) {
                    object.modifCoord(coords);
                    if (CollisionDetection.checkCollision(object, current) == true && object.getIdUser().equals(current.getIdUser()) != true) { // 1 collision
                        if (current.isIgnored(object.getType()) == false) { // collision pas ignorée
                            object.backToSave();
                            object.notifyCollision(current.getType());
                            current.notifyCollision(object.getType());
                            if (object.canDoCollisionWithObject(current)) { // collision possible
                                Debug.debug("collision ok");
                                listSaveActions.put(3, new Tuple<>(true, false, new Pair<>(object.getId(), current.getId())));
                            } else { // collision impossible (déjà en collision)
                                Debug.debug("collision impossible");
                                listSaveActions.put(4, new Tuple<>(false, false, new Pair<>(object.getId(), current.getId())));
                            }
                        } else { // collision ignorée (area)
                            Debug.debug("collision ignoré");
                            listSaveActions.put(2, new Tuple<>(true, true, new Pair<>(object.getId(), current.getId())));
                        }
                        object.setSaveCollisionObject(current);
                    } else { //pas de collision
                        Debug.debug("pas de collision");
                        listSaveActions.put(1, new Tuple<>(false, true, new Pair<>("null", "null")));
                    }
                }
            }
        }
        listSaveActions.put(0, new Tuple<>(false, false, new Pair<>("null", "null")));
        int maxPriority = 4;
        while (maxPriority >= 0) {
            if (listSaveActions.containsKey(maxPriority) == true) {
                if (maxPriority == 1){
                    for (CollisionObject object : objects){
                        object.setSaveCollisionObject(null);
                    }
                }
                return listSaveActions.get(maxPriority);
            }
            --maxPriority;
        }
        return listSaveActions.get(0);
    }

    public boolean checkCollision(Pair<Float, Float> point, Pair<Float, Float> sizes, float angle) {
        Rectangle rectangle = new Rectangle(point.getV1(), point.getV2(), sizes.getV1(), sizes.getV2());
        Float radianAngle = MathTools.toRadian(angle);
        for (int i = 0; i < this.items.size(); ++i) {
            if (CollisionDetection.checkCollision(this.items.get(i), rectangle, radianAngle)) {
                return true;
            }
        }
        return false;
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
