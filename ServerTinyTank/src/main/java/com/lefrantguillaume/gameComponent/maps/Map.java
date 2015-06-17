package com.lefrantguillaume.gameComponent.maps;

import com.lefrantguillaume.WindowController;
import com.lefrantguillaume.gameComponent.gameMode.EnumGameMode;
import com.lefrantguillaume.gameComponent.gameobjects.obstacles.Block;
import com.lefrantguillaume.gameComponent.gameobjects.obstacles.Obstacle;
import com.lefrantguillaume.gameComponent.gameobjects.obstacles.ObstacleConfigData;
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
    private final List<Obstacle> mapObstacles;
    private List<Rectangle2D> currentObjects;
    private String mapName;
    private File fileConfig;
    private File image;

    public Map(ObstacleConfigData obstacleConfigData, File fileConfig, File image, JSONObject map) throws JSONException {
        this.fileConfig = fileConfig;
        this.mapGameModes = new ArrayList<>();
        this.currentObjects = new ArrayList<>();
        this.mapObstacles = new ArrayList<>();
        this.image = image;
        this.createWorld(obstacleConfigData);
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

    private void createWorld(ObstacleConfigData obstacleConfigData) {
        this.mapObstacles.add(new Obstacle(obstacleConfigData.getWorldWall("admin-UNBREAKABLE-Wall-1"), true));
        this.mapObstacles.add(new Obstacle(obstacleConfigData.getWorldWall("admin-UNBREAKABLE-Wall-2"), true));
        this.mapObstacles.add(new Obstacle(obstacleConfigData.getWorldWall("admin-UNBREAKABLE-Wall-3"), true));
        this.mapObstacles.add(new Obstacle(obstacleConfigData.getWorldWall("admin-UNBREAKABLE-Wall-4"), true));
    }

    public void resetCurrentObject() {
        this.currentObjects.clear();
    }

    private boolean detectCollision(List<Obstacle> world, List<Rectangle2D> obstacles, Rectangle2D object) {
        for (int i = 0; i < world.size(); ++i) {
            Rectangle2D current = new Rectangle2D(world.get(i).getXByIndex(0), world.get(i).getYByIndex(0), world.get(i).getSizeXByIndex(0), world.get(i).getSizeYByIndex(0));
            if (object.intersects(current)) {
                WindowController.addConsoleMsg("collision avec la map");
                return true;
            }
        }
        for (int i = 0; i < obstacles.size(); ++i) {
            if (object.intersects(obstacles.get(i))) {
                WindowController.addConsoleMsg("collision avec un objet: " + i);
                return true;
            }
        }
        return false;
    }

    public Pair<Float, Float> calcRespawnPoint(EnumGameMode gameMode, int team, List<Block> collisionObject, boolean newRound) {
        Pair<Float, Float> positions = null;
        MapGameMode current = this.getGameMode(gameMode);
        if (current == null) {
            return null;
        }
        List<Rectangle2D> respawnPoints = current.getRespawnPointsForATeam(team);
        if (respawnPoints == null) {
            return null;
        }
        for (int i = 0; i < respawnPoints.size(); ++i) {
            for (int i2 = 0; i2 < 10; ++i2) {
                int x = (int) (RandomTools.getInt((int) respawnPoints.get(i).getWidth()) + respawnPoints.get(i).getMinX());
                int y = (int) (RandomTools.getInt((int) respawnPoints.get(i).getHeight()) + respawnPoints.get(i).getMinY());

                WindowController.addConsoleMsg("respawn : [" + x +", "+ y +"] into [" + respawnPoints.get(i).getMinX() + "," + respawnPoints.get(i).getMinY() + "][" + respawnPoints.get(i).getWidth() + "," + respawnPoints.get(i).getHeight() + "]");
                for (int i3 = 0; i3 < collisionObject.size(); ++i3) {
                    Rectangle2D object = new Rectangle2D(x - collisionObject.get(i).getShiftOrigin().getKey(), y - collisionObject.get(i).getShiftOrigin().getValue(),
                            collisionObject.get(i).getSizes().getKey(), collisionObject.get(i).getSizes().getValue());
                    if (!this.detectCollision(this.mapObstacles, this.currentObjects, object)) {
                        positions = new Pair<>((float) x, (float) y);
                        if (newRound == true)
                            this.currentObjects.add(object);
                        return positions;
                    }
                }
            }
        }
        return positions;
    }

    // GETTERS

    public MapGameMode getGameMode(EnumGameMode enumMode) {
        for (int i = 0; i < this.mapGameModes.size(); ++i) {
            if (enumMode.equals(this.mapGameModes.get(i).getEnumMode())) {
                return this.mapGameModes.get(i);
            }
        }
        return null;
    }

    public List<Obstacle> getMapObstacles(){
        return this.mapObstacles;
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
