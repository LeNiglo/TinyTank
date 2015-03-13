package com.lefrantguillaume.collisionComponent;

import org.newdawn.slick.geom.Rectangle;

/**
 * Created by andres_k on 13/03/2015.
 */
public class CollisionDetection {
    public static boolean checkCollision(CollisionObject obj1, CollisionObject obj2) {
        Rectangle rectangle1 = new Rectangle(obj1.getX(), obj1.getY(), obj1.getSizeX(), obj1.getSizeY());
        Rectangle rectangle2 = new Rectangle(obj2.getX(), obj2.getY(), obj2.getSizeX(), obj2.getSizeY());

        if (rectangle1.intersects(rectangle2)) {
            return true;
        } else if (rectangle1.contains(obj2.getX(), obj2.getY()) &&
                rectangle1.contains(obj2.getX() + obj2.getSizeX(), obj2.getY()) &&
                rectangle1.contains(obj2.getX(), obj2.getY() + obj2.getSizeY()) &&
                rectangle1.contains(obj2.getX() + obj2.getSizeX(), obj2.getY() + obj2.getSizeY())) {
            return true;
        } else {
            return false;
        }
    }
}
