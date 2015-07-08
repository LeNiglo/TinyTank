package com.lefrantguillaume.utils.configs;

import java.awt.*;

/**
 * Created by andres_k on 11/03/2015.
 */
public class WindowConfig {
    private static float sizeX;
    private static float sizeY;
    private static boolean needInit;

    public static void init(){
        if (needInit == true) {
            Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
            float height = (float) dimension.getHeight();
            float width = (float) dimension.getWidth();

            sizeX = 1280;
            sizeY = 768;
            needInit = false;
        }
    }

    public static float getSizeX() {
        return sizeX;
    }

    public static float getSizeY() {
        return sizeY;
    }

    public static int getIntSizeX() {
        return (int)sizeX;
    }

    public static int getIntSizeY() {
        return (int)sizeY;
    }
}
