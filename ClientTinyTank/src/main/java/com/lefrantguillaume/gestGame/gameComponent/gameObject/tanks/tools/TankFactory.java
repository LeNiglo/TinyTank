package com.lefrantguillaume.gestGame.gameComponent.gameObject.tanks.tools;

import com.lefrantguillaume.gestGame.Utils.stockage.Pair;
import com.lefrantguillaume.gestGame.Utils.tools.Rectangle;
import com.lefrantguillaume.gestGame.gameComponent.animations.AnimatorGameData;
import com.lefrantguillaume.gestGame.gameComponent.gameObject.projectiles.EnumShots;
import com.lefrantguillaume.gestGame.gameComponent.gameObject.spells.EnumSpells;
import com.lefrantguillaume.gestGame.gameComponent.gameObject.tanks.equipment.Canon;
import com.lefrantguillaume.gestGame.gameComponent.gameObject.tanks.equipment.TankSpell;
import com.lefrantguillaume.gestGame.gameComponent.gameObject.tanks.equipment.TankState;
import com.lefrantguillaume.gestGame.gameComponent.gameObject.tanks.equipment.TankWeapon;
import com.lefrantguillaume.gestGame.gameComponent.gameObject.tanks.types.Tank;
import com.lefrantguillaume.gestGame.gameComponent.gameObject.tanks.types.EnumTanks;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * Created by andres_k on 13/03/2015.
 */
public class TankFactory {
    public static Tank createTank(JSONObject config, AnimatorGameData animatorGameData) throws JSONException {
        Tank tank = new Tank(config, animatorGameData);
        return tank;
    }

    public static TankWeapon createTankWeapon(JSONObject config, AnimatorGameData animatorGameData) throws JSONException {
        JSONObject hit = config.getJSONObject("hit");
        JSONObject weapon = config.getJSONObject("weapon");

        EnumShots shot = EnumShots.getEnumByValue(hit.getString("shotType"));
        float speed = Float.valueOf(hit.getString("speed"));
        float damage = Float.valueOf(hit.getString("damage"));
        Pair<Float, Float> shiftHitExplode = new Pair<Float, Float>(Float.valueOf(hit.getJSONObject("build").getString("shiftXExplode")), Float.valueOf(hit.getJSONObject("build").getString("shiftYExplode")));
        Pair<Float, Float> shiftHitOrigin = new Pair<Float, Float>(Float.valueOf(hit.getJSONObject("build").getString("centerX")), Float.valueOf(hit.getJSONObject("build").getString("centerY")));
        Pair<Float, Float> shiftHitHead = new Pair<Float, Float>(Float.valueOf(hit.getJSONObject("build").getString("headX")), Float.valueOf(hit.getJSONObject("build").getString("headY")));

        Pair<Float, Float> shiftWeaponOrigin = new Pair<Float, Float>(Float.valueOf(weapon.getString("centerX")), Float.valueOf(weapon.getString("centerY")));
        TankWeapon tankWeapon = new TankWeapon(speed, damage, shiftWeaponOrigin, shiftHitExplode, shiftHitOrigin, shiftHitHead, animatorGameData.getShotAnimator(shot), shot);
        JSONArray canons = weapon.getJSONArray("canons");
        for (int i = 0; i < canons.length(); ++i){
            JSONObject current = canons.getJSONObject(i);
            Pair<Float, Float> shiftOrigin = new Pair<Float, Float>(Float.valueOf(current.getString("centerX")), Float.valueOf(current.getString("centerY")));
            Pair<Float, Float> shiftCanonHead = new Pair<Float, Float>(Float.valueOf(current.getString("shiftXHead")), Float.valueOf(current.getString("shiftYHead")));
            tankWeapon.addCanon(new Canon(shiftOrigin, shiftCanonHead));
        }

        JSONArray collisions = hit.getJSONObject("build").getJSONArray("collisions");
        for (int i = 0; i < collisions.length(); ++i){
            JSONObject current = collisions.getJSONObject(i);
            Pair<Float, Float> shiftOrigin2 = new Pair<Float, Float>(Float.valueOf(current.getString("shiftOriginX")), Float.valueOf(current.getString("shiftOriginY")));
            Pair<Float, Float> sizes= new Pair<Float, Float>(Float.valueOf(current.getString("sizeX")), Float.valueOf(current.getString("sizeY")));
            tankWeapon.addCollisionObject(new Rectangle(shiftOrigin2, sizes));
        }


        return tankWeapon;
    }

    public static TankState createTankState(JSONObject config, AnimatorGameData animatorGameData) throws JSONException {
        EnumTanks tank = EnumTanks.getEnumByValue(config.getString("tankType"));

        JSONObject build = config.getJSONObject("build");
        Pair<Float, Float> shiftOrigin = new Pair<Float, Float>(Float.valueOf(build.getString("centerX")), Float.valueOf(build.getString("centerY")));
        Pair<Float, Float> shiftHitExplode = new Pair<Float, Float>(Float.valueOf(build.getString("shiftXExplode")), Float.valueOf(build.getString("shiftYExplode")));

        TankState tankState = new TankState(Float.valueOf(config.getString("speed")), Float.valueOf(config.getString("life")), Float.valueOf(config.getString("armor")), Float.valueOf(config.getString("accuracy")),
                animatorGameData.getTankBodyAnimator(tank), animatorGameData.getTankTopAnimator(tank), tank, shiftOrigin, shiftHitExplode);
        JSONArray collisions = build.getJSONArray("collisions");
        for (int i = 0; i < collisions.length(); ++i){
            JSONObject current = collisions.getJSONObject(i);
            Pair<Float, Float> shiftOrigin2 = new Pair<Float, Float>(Float.valueOf(current.getString("shiftOriginX")), Float.valueOf(current.getString("shiftOriginY")));
            Pair<Float, Float> sizes= new Pair<Float, Float>(Float.valueOf(current.getString("sizeX")), Float.valueOf(current.getString("sizeY")));
            tankState.addCollisionObject(new Rectangle(shiftOrigin2, sizes));
        }
        return tankState;
    }

    public static TankSpell createTankSpell(JSONObject config, AnimatorGameData animatorGameData) throws JSONException {
        EnumSpells spell = EnumSpells.getEnumByValue(config.getString("spellType"));
        TankSpell tankSpell = new TankSpell();
        return tankSpell;
    }
}
