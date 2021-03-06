package com.lefrantguillaume.gameComponent.gameobjects.tanks.tools;

import com.lefrantguillaume.gameComponent.EnumGameObject;
import com.lefrantguillaume.gameComponent.gameobjects.obstacles.Block;
import com.lefrantguillaume.gameComponent.gameobjects.tanks.Tank;
import com.lefrantguillaume.gameComponent.gameobjects.tanks.equipment.TankBox;
import com.lefrantguillaume.gameComponent.gameobjects.tanks.equipment.TankSpell;
import com.lefrantguillaume.gameComponent.gameobjects.tanks.equipment.TankState;
import com.lefrantguillaume.gameComponent.gameobjects.tanks.equipment.TankWeapon;
import javafx.util.Pair;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * Created by andres_k on 13/03/2015.
 */
public class TankFactory {
    public static Tank createTank(JSONObject config) throws JSONException {
        Tank tank = new Tank(config);
        return tank;
    }

    public static TankWeapon createTankWeapon(JSONObject config) throws JSONException {
        JSONObject hit = config.getJSONObject("hit");

        EnumGameObject shotType = EnumGameObject.getEnumByValue(hit.getString("shotType"));
        float speed = Float.valueOf(hit.getString("speed"));
        float damage = Float.valueOf(hit.getString("damage"));
        long cooldown = Long.valueOf(hit.getString("cooldown"));

        TankWeapon tankWeapon = new TankWeapon(speed, damage, shotType, cooldown);
        return tankWeapon;
    }

    public static TankState createTankState(JSONObject config) throws JSONException {
        EnumGameObject tankType = EnumGameObject.getEnumByValue(config.getString("tankType"));

        JSONObject build = config.getJSONObject("build");

        TankState tankState = new TankState(Float.valueOf(config.getString("speed")), Float.valueOf(config.getString("life")), Float.valueOf(config.getString("armor")), tankType);
        JSONArray collisions = build.getJSONArray("collisions");
        for (int i = 0; i < collisions.length(); ++i){
            JSONObject current = collisions.getJSONObject(i);
            Pair<Float, Float> shiftOrigin = new Pair<Float, Float>(Float.valueOf(current.getString("shiftOriginX")), Float.valueOf(current.getString("shiftOriginY")));
            Pair<Float, Float> sizes= new Pair<Float, Float>(Float.valueOf(current.getString("sizeX")), Float.valueOf(current.getString("sizeY")));
            tankState.addCollisionObject(new Block(shiftOrigin, sizes));
        }
        return tankState;
    }

    public static TankSpell createTankSpell(JSONObject config) throws JSONException {
        EnumGameObject spellType = EnumGameObject.getEnumByValue(config.getString("spellType"));
        long cooldown = Long.valueOf(config.getString("cooldown"));

        TankSpell tankSpell = new TankSpell(spellType, cooldown);
        return tankSpell;
   }

    public static TankBox createTankBox(JSONObject config) throws JSONException {
        EnumGameObject boxType = EnumGameObject.getEnumByValue(config.getString("boxType"));
        int boxes = Integer.valueOf(config.getString("boxes"));
        long cooldownPut = Long.valueOf(config.getString("cooldownPut"));
        long cooldownGenerate = Long.valueOf(config.getString("cooldownGenerate"));

        return new TankBox(boxType, boxes, cooldownPut, cooldownGenerate);
    }
}
