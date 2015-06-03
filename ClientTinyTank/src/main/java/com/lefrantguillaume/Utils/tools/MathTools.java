package com.lefrantguillaume.Utils.tools;


import com.lefrantguillaume.Utils.stockage.Pair;

import java.awt.geom.AffineTransform;

/**
 * Created by andres_k on 17/03/2015.
 */
public class MathTools {

    public static float getAngle(double x1, double y1, double x2, double y2) {
        double angle;
        angle = Math.atan2(y2 - y1, x2 - x1) * 180 / Math.PI;
        return (float) angle;
    }

    public static float toRadian(float angle){
        return (float)(angle * Math.PI / 180);
    }

    public static Pair<Float, Float> movePredict(float angle, float speed, float delta) {
        double addX = Math.cos(angle * Math.PI / 180);
        double addY = Math.sin(angle * Math.PI / 180);
        float x = ((float) addX * speed / 10);
        float y = ((float) addY * speed / 10);
        return new Pair<>(x * delta, y * delta);
    }

    public static void rotate(Pair<Float, Float> center, Pair<Float, Float> point, float angle){
        float[] newPoint = {point.getV1(), point.getV2()};
        AffineTransform.getRotateInstance(Math.toRadians(angle), center.getV1(), center.getV2()).transform(newPoint, 0, newPoint, 0, 1);
        point.setV1(newPoint[0]);
        point.setV2(newPoint[1]);
    }
}
