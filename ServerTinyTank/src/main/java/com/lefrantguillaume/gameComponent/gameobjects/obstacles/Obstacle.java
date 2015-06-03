package com.lefrantguillaume.gameComponent.gameobjects.obstacles;


import com.lefrantguillaume.gameComponent.EnumGameObject;
import com.lefrantguillaume.networkComponent.gameServerComponent.clientmsgs.MessageObstacleUpdateState;
import com.lefrantguillaume.utils.Block;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andres_k on 16/03/2015.
 */
public class Obstacle {
    private final EnumGameObject type;
    private final  List<Block> collisionObject;
    private final Pair<Float, Float> shiftOrigin;
    private final float maxLife;
    private final float damage;
    private float currentLife;

    private String playerId;
    private String playerPseudo;
    private String id;
    private Pair<Float, Float> positions;
    private float angle;
    private boolean created;

    public Obstacle(EnumGameObject type,  List<Block> collisionObject, Pair<Float, Float> shiftOrigin, float maxLife, float damage) {
        this.type = type;
        this.maxLife = maxLife;
        this.damage = damage;
        this.shiftOrigin = new Pair<>(shiftOrigin.getKey(), shiftOrigin.getValue());
        this.collisionObject = collisionObject;
        this.created = false;
        this.currentLife = this.maxLife;
    }

    public Obstacle(Obstacle obstacle) {
        this.type = obstacle.type;
        this.maxLife = obstacle.maxLife;
        this.damage = obstacle.damage;
        this.shiftOrigin = new Pair<>(obstacle.shiftOrigin.getKey(), obstacle.shiftOrigin.getValue());
        this.created = obstacle.created;
        this.currentLife = obstacle.currentLife;
        this.collisionObject = obstacle.collisionObject;
    }

    // FUNCTIONS
    public void createObstacle(String playerId, String playerPseudo, String id, float angle, float posX, float posY) {
        this.playerId = playerId;
        this.playerPseudo = playerPseudo;
        this.angle = angle;
        this.id = id;
        this.positions = new Pair<>(posX, posY);
        this.created = true;
    }

    public MessageObstacleUpdateState getHit(float damage) {
        MessageObstacleUpdateState msg;

        this.currentLife -= damage;
        if (this.currentLife < 0) {
            this.currentLife = 0;
        }
        msg = new MessageObstacleUpdateState(this.playerPseudo, this.playerId, this.id, this.currentLife);
        return msg;
    }

    // GETTERS
    public String getPlayerId() {
        return this.playerId;
    }

    public String getPlayerPseudo(){
        return this.playerPseudo;
    }

    public float getX() {
        return this.positions.getKey();
    }

    public float getY() {
        return this.positions.getValue();
    }

    public String getId() {
        return this.id;
    }

    public EnumGameObject getType() {
        return this.type;
    }

    public Pair<Float, Float> getPositions() {
        return this.positions;
    }

    public Pair<Float, Float> getShiftOrigin() {
        return this.shiftOrigin;
    }

    public float getAngle() {
        return this.angle;
    }

    public float getMaxLife() {
        return this.maxLife;
    }

    public float getCurrentLife() {
        return this.currentLife;
    }

    public float getDamage() {
        return this.damage;
    }

    public List<EnumGameObject> getIgnoredObjectList() {
        List<EnumGameObject> types = new ArrayList<>();

        if (this.type.equals(EnumGameObject.MINE)) {
            types.add(EnumGameObject.ROCKET);
            types.add(EnumGameObject.MACHINE_GUN);
            types.add(EnumGameObject.LASER);
        } else if (this.type.equals(EnumGameObject.PLASMA_WALL)) {
            types.add(EnumGameObject.LASER);
        }
        return types;
    }

    // SETTERS

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public void setCurrentLife(int currentLife) {
        this.currentLife = currentLife;
    }
}
