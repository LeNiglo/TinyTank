package com.lefrantguillaume.components.gameComponent.gameObject.tanks.tools;

import com.lefrantguillaume.utils.stockage.Pair;
import com.lefrantguillaume.utils.tools.Block;
import com.lefrantguillaume.components.gameComponent.animations.AnimatorGameData;
import com.lefrantguillaume.components.gameComponent.gameObject.EnumGameObject;
import com.lefrantguillaume.components.gameComponent.gameObject.obstacles.ObstacleConfigData;
import com.lefrantguillaume.components.gameComponent.gameObject.spells.SpellFactory;
import com.lefrantguillaume.components.gameComponent.gameObject.tanks.Tank;
import com.lefrantguillaume.components.gameComponent.gameObject.tanks.equipment.*;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * Created by andres_k on 13/03/2015.
 */
public class TankFactory {
    public static Tank createTank(JSONObject config, AnimatorGameData animatorGameData, ObstacleConfigData obstacleConfigData) throws JSONException {
        Tank tank = new Tank(config, animatorGameData, obstacleConfigData);
        return tank;
    }

    public static TankWeapon createTankWeapon(JSONObject config, AnimatorGameData animatorGameData) throws JSONException {
        JSONObject hit = config.getJSONObject("hit");
        JSONObject weapon = config.getJSONObject("weapon");

        EnumGameObject shotType = EnumGameObject.getEnumByValue(hit.getString("shotType"));

        float speed = Float.valueOf(hit.getString("speed"));
        float damage = Float.valueOf(hit.getString("damage"));
        float maxRange = Float.valueOf(hit.getString("maxRange"));
        long cooldown = Long.valueOf(hit.getString("cooldown"));

        Pair<Float, Float> shiftHitExplode = new Pair<>(Float.valueOf(hit.getJSONObject("build").getString("shiftXExplode")), Float.valueOf(hit.getJSONObject("build").getString("shiftYExplode")));
        Pair<Float, Float> shiftHitOrigin = new Pair<>(Float.valueOf(hit.getJSONObject("build").getString("centerX")), Float.valueOf(hit.getJSONObject("build").getString("centerY")));
        Pair<Float, Float> shiftHitHead = new Pair<>(Float.valueOf(hit.getJSONObject("build").getString("headX")), Float.valueOf(hit.getJSONObject("build").getString("headY")));

        Pair<Float, Float> shiftWeaponOrigin = new Pair<>(Float.valueOf(weapon.getString("centerX")), Float.valueOf(weapon.getString("centerY")));
        TankWeapon tankWeapon = new TankWeapon(speed, damage, maxRange, shiftWeaponOrigin, shiftHitExplode, shiftHitOrigin, shiftHitHead, animatorGameData.getShotAnimator(shotType), shotType, cooldown);
        JSONArray canons = weapon.getJSONArray("canons");
        for (int i = 0; i < canons.length(); ++i){
            JSONObject current = canons.getJSONObject(i);
            Pair<Float, Float> shiftOrigin = new Pair<>(Float.valueOf(current.getString("centerX")), Float.valueOf(current.getString("centerY")));
            Pair<Float, Float> shiftCanonHead = new Pair<>(Float.valueOf(current.getString("shiftXHead")), Float.valueOf(current.getString("shiftYHead")));
            tankWeapon.addCanon(new Canon(shiftOrigin, shiftCanonHead));
        }

        JSONArray collisions = hit.getJSONObject("build").getJSONArray("collisions");
        for (int i = 0; i < collisions.length(); ++i){
            JSONObject current = collisions.getJSONObject(i);
            Pair<Float, Float> shiftOrigin2 = new Pair<>(Float.valueOf(current.getString("shiftOriginX")), Float.valueOf(current.getString("shiftOriginY")));
            Pair<Float, Float> sizes= new Pair<>(Float.valueOf(current.getString("sizeX")), Float.valueOf(current.getString("sizeY")));
            tankWeapon.addCollisionObject(new Block(shiftOrigin2, sizes));
        }


        return tankWeapon;
    }

    public static TankState createTankState(JSONObject config, AnimatorGameData animatorGameData) throws JSONException {
        EnumGameObject tankType = EnumGameObject.getEnumByValue(config.getString("tankType"));

        JSONObject build = config.getJSONObject("build");
        Pair<Float, Float> shiftOrigin = new Pair<>(Float.valueOf(build.getString("centerX")), Float.valueOf(build.getString("centerY")));
        Pair<Float, Float> shiftHitExplode = new Pair<>(Float.valueOf(build.getString("shiftXExplode")), Float.valueOf(build.getString("shiftYExplode")));

        TankState tankState = new TankState(Float.valueOf(config.getString("speed")), Float.valueOf(config.getString("life")), Float.valueOf(config.getString("armor")), Float.valueOf(config.getString("accuracy")),
                animatorGameData.getTankBodyAnimator(tankType), animatorGameData.getTankTopAnimator(tankType), tankType, shiftOrigin, shiftHitExplode);
        JSONArray collisions = build.getJSONArray("collisions");
        for (int i = 0; i < collisions.length(); ++i){
            JSONObject current = collisions.getJSONObject(i);
            Pair<Float, Float> shiftOrigin2 = new Pair<>(Float.valueOf(current.getString("shiftOriginX")), Float.valueOf(current.getString("shiftOriginY")));
            Pair<Float, Float> sizes= new Pair<>(Float.valueOf(current.getString("sizeX")), Float.valueOf(current.getString("sizeY")));
            tankState.addCollisionObject(new Block(shiftOrigin2, sizes));
        }
        return tankState;
    }

    public static TankSpell createTankSpell(JSONObject config, AnimatorGameData animatorGameData, ObstacleConfigData obstacleConfigData) throws JSONException {
        EnumGameObject spellType = EnumGameObject.getEnumByValue(config.getString("spellType"));
        long cooldown = Long.valueOf(config.getString("cooldown"));

        TankSpell tankSpell = new TankSpell(SpellFactory.createSpell(spellType, obstacleConfigData, animatorGameData.getSpellAnimator(spellType)), cooldown);
        return tankSpell;
    }

    public static TankBox createTankBox(JSONObject config, AnimatorGameData animatorGameData) throws JSONException {
        EnumGameObject boxType = EnumGameObject.getEnumByValue(config.getString("boxType"));
        int boxes = Integer.valueOf(config.getString("boxes"));
        long cooldownPut = Long.valueOf(config.getString("cooldownPut"));
        long cooldownGenerate = Long.valueOf(config.getString("cooldownGenerate"));

        return new TankBox(boxType, boxes, cooldownPut, cooldownGenerate);
    }
}
