package com.lefrantguillaume.Utils.configs;

/**
 * Created by andres_k on 11/03/2015.
 */
public class WindowConfig {
    private static int sizeX;
    private static int sizeY;

    public WindowConfig(int x, int y) {
        this.sizeX = x;
        this.sizeY = y;
    }

    public static int getSizeX() {
        return sizeX;
    }

    public static int getSizeY() {
        return sizeY;
    }
}
