package com.lefrantguillaume.game;

/**
 * Created by Styve on 25/03/2015.
 */
public class Shot {
    private String playerId;
    private String shotId;
    private long timestamp;

    public Shot(String shotId, String playerId) {
        this.playerId = playerId;
        this.shotId = shotId;
        this.timestamp = System.currentTimeMillis();
    }

    public String getPlayerId() {return playerId;}
    public String getShotId() {return shotId;}
    public long getTimestamp() {return timestamp;}
}
