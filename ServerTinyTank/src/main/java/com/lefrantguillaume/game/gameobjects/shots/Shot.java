package com.lefrantguillaume.game.gameobjects.shots;


/**
 * Created by Styve on 25/03/2015.
 */
public class Shot {
    private String playerId;
    private String shotId;
    private EnumShots shotType;
    private float damageShot;
    private float speedShot;
    private float lifeShot;
    private long timestamp;

    public Shot(String shotId, String playerId, EnumShots shotType, float damageShot, float speedShot, float lifeShot) {
        this.playerId = playerId;
        this.shotId = shotId;
        this.shotType = shotType;
        this.damageShot = damageShot;
        this.speedShot = speedShot;
        this.lifeShot = lifeShot;
        this.timestamp = System.currentTimeMillis();
    }

    public String getPlayerId() {return playerId;}
    public String getShotId() {return shotId;}
    public EnumShots getShotType() {return shotType;}
    public float getDamageShot() {return damageShot;}
    public float getSpeedShot() {return speedShot;}
    public float getLifeShot() {return lifeShot;}
    public long getTimestamp() {return timestamp;}
}
