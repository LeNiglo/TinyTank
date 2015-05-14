package com.lefrantguillaume.gameComponent.gameobjects.tanks.tools;

import com.lefrantguillaume.gameComponent.gameobjects.shots.EnumShots;
import com.lefrantguillaume.gameComponent.gameobjects.spell.EnumSpells;
import com.lefrantguillaume.gameComponent.gameobjects.tanks.equipment.TankSpell;
import com.lefrantguillaume.gameComponent.gameobjects.tanks.equipment.TankState;
import com.lefrantguillaume.gameComponent.gameobjects.tanks.equipment.TankWeapon;
import com.lefrantguillaume.gameComponent.gameobjects.tanks.types.EnumTanks;
import com.lefrantguillaume.gameComponent.gameobjects.tanks.types.Tank;
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
        JSONObject weapon = config.getJSONObject("weapon");

        EnumShots shot = EnumShots.getEnumByValue(hit.getString("shotType"));
        float speed = Float.valueOf(hit.getString("speed"));
        float damage = Float.valueOf(hit.getString("damage"));

        TankWeapon tankWeapon = new TankWeapon(speed, damage, 10, shot);
        return tankWeapon;
    }

    public static TankState createTankState(JSONObject config) throws JSONException {
        EnumTanks tank = EnumTanks.getEnumByValue(config.getString("tankType"));

        JSONObject build = config.getJSONObject("build");

        TankState tankState = new TankState(Float.valueOf(config.getString("speed")), Float.valueOf(config.getString("life")), Float.valueOf(config.getString("armor")), tank);
        return tankState;
    }

    public static TankSpell createTankSpell(JSONObject config) throws JSONException {
        EnumSpells spell = EnumSpells.getEnumByValue(config.getString("spellType"));
        TankSpell tankSpell = new TankSpell();
        return tankSpell;
    }
}
