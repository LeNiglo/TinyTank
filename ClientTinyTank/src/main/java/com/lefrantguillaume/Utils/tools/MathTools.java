package com.lefrantguillaume.Utils.tools;


/**
 * Created by andres_k on 17/03/2015.
 */
public class MathTools {

    public static float getAngle(double x1, double y1, double x2, double y2){
        double angle;

        angle = Math.atan2(y2 - y1, x2 - x1) * 100 / Math.PI;
        return (float)angle;
    }
}
