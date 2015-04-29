package com.lefrantguillaume.gestGame.Utils.configs;

import java.awt.*;

/**
 * Created by andres_k on 11/03/2015.
 */
public class WindowConfig {
    private static int sizeX;
    private static int sizeY;

    public WindowConfig() {

        //TODO: Résolution à régler
        Dimension dimension = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        int height = (int)dimension.getHeight();
        int width  = (int)dimension.getWidth();


        this.sizeX = 1280;
        this.sizeY = 768;
    }

    public static int getSizeX() {
        return sizeX;
    }

    public static int getSizeY() {
        return sizeY;
    }
}
