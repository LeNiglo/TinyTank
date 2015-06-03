package com.lefrantguillaume.components.gameComponent.gameObject.tanks.equipment;

import com.lefrantguillaume.Utils.stockage.Pair;
import com.lefrantguillaume.Utils.tools.Block;
import com.lefrantguillaume.components.gameComponent.animations.Animator;
import com.lefrantguillaume.components.gameComponent.gameObject.EnumGameObject;
import com.lefrantguillaume.components.gameComponent.gameObject.obstacles.Obstacle;

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
    private Animator animator;

    public TankBox(EnumGameObject type, float maxLife, float damage, Animator animator, Pair<Float, Float> shiftOrigin) {
        this.collisionObject = new ArrayList<>();
        this.shiftOrigin = shiftOrigin;
        this.animator = animator;
        this.maxLife = maxLife;
        this.damage = damage;
        this.type = type;
    }

    public TankBox(TankBox tankBox) {
        this.maxLife = tankBox.maxLife;
        this.damage = tankBox.damage;
        this.type = tankBox.type;
        this.shiftOrigin = new Pair<>(tankBox.shiftOrigin);
        this.animator = new Animator(tankBox.animator);
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
        Obstacle obstacle = new Obstacle(new Animator(this.animator), this.type, this.collisionObject, this.shiftOrigin, this.maxLife, this.damage);
        return obstacle;
    }

    // GETTERS

    public List<Block> getCollisionObject(){
        return this.collisionObject;
    }
}
