package com.lefrantguillaume.gameComponent.maps;

import com.lefrantguillaume.WindowController;
import com.lefrantguillaume.gameComponent.gameMode.EnumGameMode;
import javafx.geometry.Rectangle2D;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by andres_k on 27/05/2015.
 */
public class MapGameMode {
    private HashMap<Integer, List<Rectangle2D>> respawnPoints;
    private List<Rectangle2D> walls;
    private EnumGameMode enumMode;

    public MapGameMode(EnumGameMode enumMode, JSONObject config) throws JSONException {
        this.enumMode = enumMode;
        this.respawnPoints = new HashMap<>();
        this.walls = new ArrayList<>();
        JSONArray spawnPoint = config.getJSONArray("spawnPoint");
        for (int i = 0; i < spawnPoint.length(); ++i) {
            JSONObject tmp = spawnPoint.getJSONObject(i);
            Integer id = tmp.getInt("team");
            if (!this.respawnPoints.containsKey(id)){
                this.respawnPoints.put(id, new ArrayList<>());
            }
            this.respawnPoints.get(id).add(new Rectangle2D(tmp.getInt("x"), tmp.getInt("y"), tmp.getInt("sizeX"), tmp.getInt("sizeY")));
        }
        JSONArray obstacle = config.getJSONArray("obstacles");
        for (int i = 0; i < obstacle.length(); ++i) {
            JSONObject tmp2 = obstacle.getJSONObject(i);
            this.walls.add(new Rectangle2D(tmp2.getInt("x"), tmp2.getInt("y"), tmp2.getInt("sizeX"), tmp2.getInt("sizeY")));
        }

    }

    //GETTERS
    public EnumGameMode getEnumMode(){
        return this.enumMode;
    }

    public List<Rectangle2D> getWalls(){
        return this.walls;
    }

    public List<Rectangle2D> getRespawnPointsForATeam(int idTeam){
        WindowController.addConsoleMsg("respawn size : " + this.respawnPoints.size() + "  requested: " + idTeam);
        return this.respawnPoints.get(idTeam);
    }
}
