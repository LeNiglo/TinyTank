package com.lefrantguillaume.Utils;

/**
 * Created by andres_k on 11/03/2015.
 */
public class WindowConfig {
    private int sizeX;
    private int sizeY;

    public WindowConfig(int x, int y){
        this.sizeX = x;
        this.sizeY = y;
    }

    public int getSizeX(){
        return this.sizeX;
    }
    public int getSizeY(){
        return this.sizeY;
    }
    public void setSizeX(int value){
        this.sizeX = value;
    }
    public void setSizeY(int value){
        this.sizeY = value;
    }
}
