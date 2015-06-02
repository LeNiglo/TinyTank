package com.lefrantguillaume.gameComponent.gameobjects.shots;


import com.lefrantguillaume.gameComponent.EnumGameObject;

/**
 * Created by andres_k on 13/03/2015.
 */
public class Shot {
    private String playerId;
    private String shotId;
    private EnumGameObject shotType;
    private float damageShot;
    private float speedShot;
    private float lifeShot;
    private long timestamp;

    public Shot(String shotId, String playerId, EnumGameObject shotType, float damageShot, float speedShot, float lifeShot) {
        this.playerId = playerId;
        this.shotId = shotId;
        this.shotType = shotType;
        this.damageShot = damageShot;
        this.speedShot = speedShot;
        this.lifeShot = lifeShot;
        this.timestamp = System.currentTimeMillis();
    }

    public String getPlayerId() {
        return this.playerId;
    }

    public String getShotId() {
        return this.shotId;
    }

    public EnumGameObject getShotType() {
        return this.shotType;
    }

    public float getDamageShot() {
        return this.damageShot;
    }

    public float getSpeedShot() {
        return this.speedShot;
    }

    public float getLifeShot() {
        return this.lifeShot;
    }

    public long getTimestamp() {
        return this.timestamp;
    }
}
