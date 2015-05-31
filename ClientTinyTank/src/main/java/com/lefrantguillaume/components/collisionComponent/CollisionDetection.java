package com.lefrantguillaume.components.collisionComponent;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;


/**
 * Created by andres_k on 13/03/2015.
 */
public class CollisionDetection {
    public static boolean checkCollision(CollisionObject obj1, CollisionObject obj2) {
        Rectangle rectangle1 = new Rectangle(obj1.getOriginX(), obj1.getOriginY(), obj1.getSizeX(), obj1.getSizeY());
        Rectangle rectangle2 = new Rectangle(obj2.getOriginX(), obj2.getOriginY(), obj2.getSizeX(), obj2.getSizeY());

        Shape s1 = rectangle1.transform(Transform.createRotateTransform(obj1.getRadian(), obj1.getX(), obj1.getY()));
        Shape s2 = rectangle2.transform(Transform.createRotateTransform(obj2.getRadian(), obj2.getX(), obj2.getY()));
        if (s1.intersects(s2)) {
            return true;
        } else {
            return false;
        }
    }
}
