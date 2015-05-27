package com.lefrantguillaume.gameComponent.maps;

import com.lefrantguillaume.WindowController;
import com.lefrantguillaume.gameComponent.gameMode.EnumGameMode;
import com.lefrantguillaume.utils.Block;
import com.lefrantguillaume.utils.RandomTools;
import javafx.geometry.Rectangle2D;
import javafx.util.Pair;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Styve on 19/03/2015.
 */
public class Map {
    private final List<MapGameMode> mapGameModes;
    private final List<Rectangle2D> world;
    private List<Rectangle2D> currentObjects;
    private String mapName;
    private File fileConfig;
    private File image;

    public Map(File fileConfig, File image, JSONObject map) throws JSONException {
        this.fileConfig = fileConfig;
        this.mapGameModes = new ArrayList<>();
        this.currentObjects = new ArrayList<>();
        this.image = image;
        this.world = new ArrayList<>();
        this.createWorld(map.getInt("sizeX"), map.getInt("sizeY"));
        this.mapName = map.get("name") != null ? map.getString("name") : "unknown";
        JSONObject modes = map.getJSONObject("gameMode");
        Iterator iterator = modes.keys();
        while (iterator.hasNext()) {
            String modeName = (String) iterator.next();
            JSONObject value = modes.getJSONObject(modeName);
            this.mapGameModes.add(new MapGameMode(EnumGameMode.getEnumByValue(modeName), value));
        }
    }

    // FUNCTIONS

    private void createWorld(int sizeX, int sizeY){
        this.world.add(new Rectangle2D(0, 0, sizeX, 5));
        this.world.add(new Rectangle2D(0, sizeY - 5, sizeX, 5));
        this.world.add(new Rectangle2D(0, 0, 5, sizeY));
        this.world.add(new Rectangle2D(sizeX - 5, 0, 5, sizeY));
    }

    public void resetCurrentObject(){
        this.currentObjects.clear();
    }

    private boolean detectCollision(List<Rectangle2D> world, List<Rectangle2D> obstacles, Rectangle2D object){
        for (int i = 0; i < world.size(); ++i) {
            if (object.intersects(world.get(i))) {
                WindowController.addConsoleMsg("collision avec la map");
                return true;
            }
        }
        for (int i = 0; i < obstacles.size(); ++i) {
            if (object.intersects(obstacles.get(i))) {
                WindowController.addConsoleMsg("collision avec un objet");
                return true;
            }
        }
        return false;
    }

    public Pair<Float, Float> calcRespawnPoint(EnumGameMode gameMode, int team, List<Block> collisionObject) {
        Pair<Float, Float> positions = null;
        WindowController.addConsoleMsg("selected mode : " + gameMode);
        MapGameMode current = this.getGameMode(gameMode);
        if (current == null){
            return null;
        }
        WindowController.addConsoleMsg("a");
        List<Rectangle2D> respawnPoints = current.getRespawnPointsForATeam(team);
        if (respawnPoints == null){
            return null;
        }
        WindowController.addConsoleMsg("b");
        for (int i = 0; i < respawnPoints.size(); ++i){
            for (int i2 = 0; i2 < 10; ++i2) {
                int x = (int) (RandomTools.getInt() % respawnPoints.get(i).getMaxX() + respawnPoints.get(i).getWidth());
                int y = (int) (RandomTools.getInt() % respawnPoints.get(i).getMaxY() + respawnPoints.get(i).getHeight());

                for (int i3 = 0; i3 < collisionObject.size(); ++i3){
                    Rectangle2D object = new Rectangle2D(x - collisionObject.get(i).getShiftOrigin().getKey(), y - collisionObject.get(i).getShiftOrigin().getValue(),
                            collisionObject.get(i).getSizes().getKey(), collisionObject.get(i).getSizes().getValue());
                    if (!this.detectCollision(this.world, this.currentObjects, object)) {
                        positions = new Pair<>((float) x, (float) y);
                        this.currentObjects.add(object);
                    }
                }
            }
        }
        return positions;
    }

    // GETTERS

    public MapGameMode getGameMode(EnumGameMode enumMode){
        for (int i = 0; i < this.mapGameModes.size(); ++i){
            WindowController.addConsoleMsg(" " + enumMode + " =? " + this.mapGameModes.get(i).getEnumMode());
            if (enumMode.equals(this.mapGameModes.get(i).getEnumMode())){
                return this.mapGameModes.get(i);
            }
        }
        return null;
    }

    public String getName() {
        return this.mapName;
    }

    public String getFileNameNoExt() {
        return this.fileConfig.getName().substring(0, fileConfig.getName().lastIndexOf("."));
    }

    public String getFileName() {
        return this.fileConfig.getName();
    }

    public String getFilePath() {
        return this.fileConfig.getAbsolutePath();
    }

    public long getFileLength() {
        return this.fileConfig.length();
    }

    public String getImgName() {
        return this.image.getName();
    }

    public String getImgPath() {
        return this.image.getAbsolutePath();
    }

    public long getImgLength() {
        return this.image.length();
    }
}
