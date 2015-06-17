package com.lefrantguillaume.components.collisionComponent;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;


/**
 * Created by andres_k on 13/03/2015.
 */
public class CollisionDetection {
    public static boolean checkCollision(CollisionObject obj1, CollisionObject obj2) {
        Shape shape1 = obj1.getShape();
        Shape shape2 = obj2.getShape();

        if (shape1.intersects(shape2)) {
            return true;
        } else if (shape2.contains(shape1)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean checkCollision(CollisionObject obj1, Rectangle obj2, Float radianAngle) {
        Shape s1 = obj1.getShape();
        Shape s2 = obj2.transform(Transform.createRotateTransform(radianAngle, obj2.getX(), obj2.getY()));
        if (s1.intersects(s2)) {
            return true;
        } else if (s1.contains(s2)) {
            return true;
        } else {
            return false;
        }
    }
}
