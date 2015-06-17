package com.lefrantguillaume.components.gameComponent.gameObject.tanks.tools;

import com.lefrantguillaume.components.gameComponent.animations.AnimatorGameData;
import com.lefrantguillaume.components.gameComponent.gameObject.EnumGameObject;
import com.lefrantguillaume.components.gameComponent.gameObject.obstacles.ObstacleConfigData;
import com.lefrantguillaume.components.gameComponent.gameObject.tanks.Tank;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.util.HashMap;

/**
 * Created by andres_k on 24/03/2015.
 */
public class TankConfigData {
    private HashMap<EnumGameObject, Tank> tanks;
    private boolean valid;

    public TankConfigData() {
        this.tanks = new HashMap<>();
        this.valid = false;
    }

    // FUNCTIONS
    public void initTanks(JSONObject config, AnimatorGameData animatorGameData, ObstacleConfigData obstacleConfigData) throws JSONException {

        JSONArray tankArray = config.getJSONArray("tanks");

        for (int i = 0; i < tankArray.length(); ++i) {
            Tank tank = TankFactory.createTank(tankArray.getJSONObject(i), animatorGameData, obstacleConfigData);
            this.tanks.put(tank.getTankState().getType(), tank);
        }
        this.valid = true;
    }

    public void clear() {
        this.tanks.clear();
    }

    // GETTERS
    public HashMap<EnumGameObject, Tank> getTanks() {
        return this.tanks;
    }

    public Tank getTank(EnumGameObject type) {
        if (this.tanks.containsKey(type)){
            return new Tank(this.tanks.get(type));
        } else {
            return null;
        }
    }

    public boolean isValid() {
        return valid;
    }
}
