package com.lefrantguillaume.gameComponent.maps;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andres_k on 13/05/2015.
 */
public class MapController {
    private List<Map> maps;
    private int currentMapIndex = 0;

    public MapController(){
        this.maps = new ArrayList<Map>();
    }

    public void clearMaps(){
        this.maps.clear();
    }

    public void addMap(Map map){
        this.maps.add(map);
    }

    // GETTERS
    public List<Map> getMaps(){
        return this.maps;
    }

    public Map getMap(int index){
        return this.maps.get(index);
    }

    public Map getCurrentMap(){
        return this.maps.get(currentMapIndex);
    }
    //SETTERS
    public void setCurrentMapIndex(int index){
        if (index >= 0 && index < this.maps.size()){
            this.maps.get(this.currentMapIndex).resetCurrentObject();
            this.currentMapIndex = index;
        }
    }
}
