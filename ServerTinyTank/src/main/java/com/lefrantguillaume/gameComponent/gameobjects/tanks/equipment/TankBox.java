package com.lefrantguillaume.gameComponent.gameobjects.tanks.equipment;


import com.lefrantguillaume.gameComponent.EnumGameObject;
import com.lefrantguillaume.gameComponent.gameobjects.obstacles.Obstacle;
import com.lefrantguillaume.utils.Block;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andres_k on 03/06/2015.
 */
public class TankBox {
    private final float maxLife;
    private final float damage;
    private final EnumGameObject type;
    private Pair<Float, Float> shiftOrigin;
    private List<Block> collisionObject;

    public TankBox(EnumGameObject type, float maxLife, float damage, Pair<Float, Float> shiftOrigin) {
        this.collisionObject = new ArrayList<>();
        this.shiftOrigin = shiftOrigin;
        this.maxLife = maxLife;
        this.damage = damage;
        this.type = type;
    }

    public TankBox(TankBox tankBox) {
        this.maxLife = tankBox.maxLife;
        this.damage = tankBox.damage;
        this.type = tankBox.type;
        this.shiftOrigin = new Pair<>(tankBox.shiftOrigin.getKey(), tankBox.shiftOrigin.getValue());
        this.collisionObject = new ArrayList<>();
        for (int i = 0; i < tankBox.collisionObject.size(); ++i){
            this.collisionObject.add(tankBox.collisionObject.get(i));
        }
    }

    // FUNCTIONS

    public void addCollisionObject(Block block) {
        this.collisionObject.add(block);
    }

    public Obstacle generateBox(){
        Obstacle obstacle = new Obstacle(this.type, this.collisionObject, this.shiftOrigin, this.maxLife, this.damage);
        return obstacle;
    }

    // GETTERS

    public List<Block> getCollisionObject(){
        return this.collisionObject;
    }
}
