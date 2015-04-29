package com.lefrantguillaume.gestGame.Utils.tools;


import com.lefrantguillaume.gestGame.Utils.stockage.Pair;

/**
 * Created by andres_k on 17/03/2015.
 */
public class MathTools {

    public static float getAngle(double x1, double y1, double x2, double y2) {
        double angle;


        angle = Math.atan2(y2 - y1, x2 - x1) * 180 / Math.PI;
        return (float) angle;
    }

    public static Pair<Float, Float> movePredict(float angle, float speed, float delta) {
        double addX = Math.cos(angle * Math.PI / 180);
        double addY = Math.sin(angle * Math.PI / 180);
        float x = ((float) addX * speed / 10);
        float y = ((float) addY * speed / 10);
        return new Pair<Float, Float>(x * delta, y * delta);
    }

    public static Pair<Float, Float> getOrigin(Pair<Float, Float> positions, Pair<Float, Float> shift) {
        Pair<Float, Float> obj = new Pair<Float, Float>(positions.getV1() + shift.getV1(), positions.getV2() + shift.getV2());

        return obj;
    }
}
