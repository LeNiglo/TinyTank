package com.lefrantguillaume.gameComponent.gameobjects.tanks.tools;


import com.lefrantguillaume.gameComponent.EnumGameObject;
import com.lefrantguillaume.gameComponent.gameobjects.tanks.Tank;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andres_k on 24/03/2015.
 */
public class TankConfigData {
    private List<Tank> tanks;
    private boolean valid;

    public TankConfigData() {
        this.tanks = new ArrayList<>();
        this.valid = false;
    }

    // FUNCTIONS
    public void initTanks(JSONObject config) throws JSONException {

        JSONArray tankArray = config.getJSONArray("tanks");
        for (int i = 0; i < tankArray.length(); ++i) {
            tanks.add(TankFactory.createTank(tankArray.getJSONObject(i)));
        }
        this.valid = true;
    }

    public void clear(){
        this.tanks.clear();
    }

    // GETTERS
    public List<Tank> getTanks() {
        return this.tanks;
    }

    public Tank getTank(EnumGameObject index) {
        if (this.valid && index.getIndex() < this.tanks.size()) {
            return new Tank(this.tanks.get(index.getIndex()));
        } else {
            return null;
        }
    }

    public boolean isValid() {
        return valid;
    }
}
