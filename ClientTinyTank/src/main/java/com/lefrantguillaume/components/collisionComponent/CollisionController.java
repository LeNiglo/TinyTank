package com.lefrantguillaume.components.collisionComponent;

import com.lefrantguillaume.Utils.stockage.Pair;
import com.lefrantguillaume.Utils.stockage.Tuple;
import com.lefrantguillaume.Utils.tools.Debug;
import com.lefrantguillaume.Utils.tools.MathTools;
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

    public Tuple<EnumCollision, Boolean, Pair<CollisionObject, CollisionObject>> checkCollision(Pair<Float, Float> coords, String id) {
        HashMap<Integer, Tuple<EnumCollision, Boolean, Pair<CollisionObject, CollisionObject>>> listSaveActions = new HashMap<>();

        List<CollisionObject> objects = this.getCollisionObject(id);
        if (!objects.isEmpty()) {
            for (int i = 0; i < this.items.size(); ++i) {
                CollisionObject current = this.items.get(i);
                for (CollisionObject object : objects) {
//                    Debug.debug("----------------" + object.getType() + " -> " + current.getType() + "----------------");
                    object.modifCoord(coords);
                    if (current.isAlive() && object.isAlive() && CollisionDetection.checkCollision(object, current) == true && object.getIdUser().equals(current.getIdUser()) != true) { // 1 collision
                        boolean isIgnored;
                        int upPriority = 0;

                        if ((isIgnored = current.isObjectIgnored(object)) == false) { // collision ignoré ? set isIgnored pour savoir si on avance ou non
                            object.backToSave();
                            object.notifyCollision(current.getType());
                            current.notifyCollision(object.getType());
                            upPriority = 2;
                        }
                        if (object.canDoCollisionWithObject(current)) { // collision possible
  //                          Debug.debug("collision ok");
                            listSaveActions.put(4 + upPriority, new Tuple<>(EnumCollision.IN, isIgnored, new Pair<>(object, current)));
                        } else { // collision impossible (déjà en collision)
    //                        Debug.debug("collision impossible");
                            listSaveActions.put(3 + upPriority, new Tuple<>(EnumCollision.NOTHING, isIgnored, new Pair<>(object, current)));
                        }
                    } else { //pas de collision
      //                  Debug.debug("pas de collision");
                        if (object.getSaveCollisionObject() != null) {
                            listSaveActions.put(1, new Tuple<>(EnumCollision.OUT, true, new Pair<>(object, object.getSaveCollisionObject())));
                        } else {
                            listSaveActions.put(1, new Tuple<>(EnumCollision.NOTHING, true, null));
                        }
                    }
                }
            }
        }
        listSaveActions.put(0, new Tuple<>(EnumCollision.NOTHING, false, null));
        int maxPriority = 6;
        while (maxPriority >= 0) {
            if (listSaveActions.containsKey(maxPriority) == true) {
                if (maxPriority == 1) {
                    for (CollisionObject object : objects) {
                        object.setSaveCollisionObject(null);
                    }
                }
                Tuple<EnumCollision, Boolean, Pair<CollisionObject, CollisionObject>> values = listSaveActions.get(maxPriority);
                if (values.getV1() == EnumCollision.IN){
                    values.getV3().getV1().setSaveCollisionObject(values.getV3().getV2());
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
