package com.lefrantguillaume.components.gameComponent.gameObject.obstacles;

import com.lefrantguillaume.Utils.stockage.Pair;
import com.lefrantguillaume.Utils.tools.Block;
import com.lefrantguillaume.components.gameComponent.animations.AnimatorGameData;
import com.lefrantguillaume.components.gameComponent.gameObject.EnumGameObject;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andres_k on 09/06/2015.
 */
public class ObstacleFactory {

    public static Obstacle createBox(AnimatorGameData animatorGameData, EnumGameObject type, JSONObject values) throws JSONException {
        List<Block> blocks = new ArrayList<>();
        JSONObject build = values.getJSONObject("build");
        Pair<Float, Float> shiftOrigin = new Pair<>(Float.valueOf(build.getString("centerX")), Float.valueOf(build.getString("centerY")));
        JSONArray collisions = build.getJSONArray("collisions");
        for (int i = 0; i < collisions.length(); ++i){
            JSONObject current = collisions.getJSONObject(i);
            Pair<Float, Float> shiftOrigin2 = new Pair<>(Float.valueOf(current.getString("shiftOriginX")), Float.valueOf(current.getString("shiftOriginY")));
            Pair<Float, Float> sizes= new Pair<>(Float.valueOf(current.getString("sizeX")), Float.valueOf(current.getString("sizeY")));
            blocks.add(new Block(shiftOrigin2, sizes));
        }
        Obstacle obstacle;
        if (type.getValue().contains("area")) {
            obstacle = new Obstacle(animatorGameData.getAreaAnimator(type), type, blocks, shiftOrigin, Float.valueOf(values.getString("life")), Float.valueOf(values.getString("damage")));
        } else {
            obstacle = new Obstacle(animatorGameData.getObstacleAnimator(type), type, blocks, shiftOrigin, Float.valueOf(values.getString("life")), Float.valueOf(values.getString("damage")));
        }
        return obstacle;
    }
}
