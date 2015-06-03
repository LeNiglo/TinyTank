package com.lefrantguillaume.gameComponent.gameobjects.shots;


import com.lefrantguillaume.gameComponent.EnumGameObject;
import com.lefrantguillaume.gameComponent.gameobjects.player.Player;
import com.lefrantguillaume.networkComponent.gameServerComponent.clientmsgs.MessageShotUpdateState;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andres_k on 13/03/2015.
 */
public class Shot {
    private String playerId;
    private String id;
    private EnumGameObject type;
    private float currentDamageShot;
    private float speedShot;
    private long timestamp;

    public Shot(String id, String playerId, EnumGameObject type, float currentDamageShot, float speedShot) {
        this.playerId = playerId;
        this.id = id;
        this.type = type;
        this.currentDamageShot = currentDamageShot;
        this.speedShot = speedShot;
        this.timestamp = System.currentTimeMillis();
    }

    // FUNCTIONS

    public MessageShotUpdateState getHit(Player player, float damage) {
        MessageShotUpdateState msg;

        this.currentDamageShot -= damage;
        if (this.currentDamageShot < 0) {
            this.currentDamageShot = 0;
        }
        msg = new MessageShotUpdateState(player.getPseudo(), this.playerId, this.id, this.currentDamageShot);
        return msg;
    }

    public void getDamageByCollision(float life) {
        this.currentDamageShot -= life;
        if (this.currentDamageShot < 0)
            this.currentDamageShot = 0;
    }

    // GETTERS
    public String getPlayerId() {
        return this.playerId;
    }

    public String getId() {
        return this.id;
    }

    public EnumGameObject getType() {
        return this.type;
    }

    public float getCurrentDamageShot() {
        return this.currentDamageShot;
    }

    public float getSpeedShot() {
        return this.speedShot;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public List<EnumGameObject> getIgnoredObjectList() {
        List<EnumGameObject> types = new ArrayList<>();

        if (this.type.equals(EnumGameObject.LASER))
            types.add(EnumGameObject.PLASMA_WALL);
        return types;
    }
}
