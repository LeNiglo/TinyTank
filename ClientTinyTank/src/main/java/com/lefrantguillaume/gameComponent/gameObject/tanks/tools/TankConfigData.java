package com.lefrantguillaume.gameComponent.gameObject.tanks.tools;

import com.lefrantguillaume.Utils.tools.Debug;
import com.lefrantguillaume.gameComponent.animations.AnimatorGameData;
import com.lefrantguillaume.gameComponent.gameObject.tanks.types.EnumTanks;
import com.lefrantguillaume.gameComponent.gameObject.tanks.types.Tank;
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
        this.tanks = new ArrayList<Tank>();
        this.valid = false;
    }

    // FUNCTIONS
    public void initTanks(JSONObject config, AnimatorGameData animatorGameData) throws JSONException {

        JSONArray tankArray = config.getJSONArray("tanks");

        for (int i = 0; i < tankArray.length(); ++i) {
            tanks.add(TankFactory.createTank(tankArray.getJSONObject(i), animatorGameData));
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

    public Tank getTank(EnumTanks index) {
        if (this.valid == true && index.getIndex() < this.tanks.size()) {
            return new Tank(this.tanks.get(index.getIndex()));
        }
        else {
            return null;
        }
    }

    public boolean isValid() {
        return valid;
    }
}
