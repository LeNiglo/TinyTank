package com.lefrantguillaume.gameComponent.gameobjects.obstacles;


import com.lefrantguillaume.gameComponent.EnumGameObject;
import com.lefrantguillaume.networkComponent.gameServerComponent.clientmsgs.MessageObstacleUpdateState;
import javafx.util.Pair;

import java.util.List;

/**
 * Created by andres_k on 16/03/2015.
 */
public class Obstacle {
    private final EnumGameObject type;
    private final List<Block> collisionObject;
    private final List<EnumGameObject> ignored;
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

    public Obstacle(EnumGameObject type, List<EnumGameObject> ignored, List<Block> collisionObject, Pair<Float, Float> shiftOrigin, float maxLife, float damage) {
        this.type = type;
        this.ignored = ignored;
        this.shiftOrigin = shiftOrigin;
        this.maxLife = maxLife;
        this.damage = damage;
        this.collisionObject = collisionObject;
        this.created = false;
        this.currentLife = this.maxLife;
    }

    public Obstacle(Obstacle obstacle, boolean isCreated) {
        this.type = obstacle.type;
        this.ignored = obstacle.ignored;
        this.shiftOrigin = new Pair<>(obstacle.shiftOrigin.getKey(), obstacle.shiftOrigin.getValue());
        this.maxLife = obstacle.maxLife;
        this.damage = obstacle.damage;
        this.collisionObject = obstacle.collisionObject;
        this.created = obstacle.created;
        this.currentLife = obstacle.currentLife;
        if (isCreated == true) {
            this.playerId = obstacle.playerId;
            this.playerPseudo = obstacle.playerPseudo;
            this.angle = obstacle.angle;
            this.id = obstacle.id;
            this.positions = new Pair<>(obstacle.positions.getKey(), obstacle.positions.getValue());
        }
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

    public String getPlayerPseudo() {
        return this.playerPseudo;
    }

    public float getXByIndex(int index) {
        if (index < this.collisionObject.size()) {
            return this.positions.getKey() + this.collisionObject.get(index).getShiftOrigin().getKey();
        } else {
            return this.positions.getKey();
        }
    }

    public float getYByIndex(int index) {
        if (index < this.collisionObject.size()) {
            return this.positions.getValue() + this.collisionObject.get(index).getShiftOrigin().getValue();
        } else {
            return this.positions.getValue();
        }
    }

    public float getSizeXByIndex(int index){
        if (index < this.collisionObject.size()) {
            return this.collisionObject.get(index).getSizes().getKey();
        }
        return 0;
    }

    public float getSizeYByIndex(int index){
        if (index < this.collisionObject.size()) {
            return this.collisionObject.get(index).getSizes().getValue();
        }
        return 0;
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
        return this.ignored;
    }

    // SETTERS

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public void setCurrentLife(int currentLife) {
        this.currentLife = currentLife;
    }

    public void setId(String id){
        this.id = id;
    }

    @Override
    public String toString(){
        return "Obstacle: " + this.id + " (" + this.type + ") from " + this.playerPseudo;
    }
}
