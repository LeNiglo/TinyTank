package com.lefrantguillaume.utils;

import java.awt.*;

/**
 * Created by andres_k on 11/03/2015.
 */
public class WindowConfig {
    private static float sizeX;
    private static float sizeY;

    public WindowConfig() {

        //TODO: Résolution à régler
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        float height = (float)dimension.getHeight();
        float width  = (float)dimension.getWidth();


        this.sizeX = 1280;
        this.sizeY = 768;
    }

    public static float getSizeX() {
        return sizeX;
    }

    public static float getSizeY() {
        return sizeY;
    }
}
