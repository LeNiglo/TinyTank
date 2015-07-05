package com.lefrantguillaume.gameComponent.target;

import com.lefrantguillaume.gameComponent.EnumCollision;
import com.lefrantguillaume.networkComponent.gameServerComponent.clientmsgs.MessageCollision;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andres_k on 05/07/2015.
 */
public class CollisionVoteElement {
    private final String hitterId;
    private final String targetId;
    private final EnumCollision type;
    private List<Pair<String, String>> players;
    private int time;

    public CollisionVoteElement(MessageCollision message){
        this.hitterId = message.getHitterId();
        this.targetId = message.getTargetId();
        this.type = message.getType();
        this.players = new ArrayList<>();
        this.players.add(new Pair<>(message.getId(), message.getPseudo()));
        this.time = 0;
    }

    // FUNCTIONS

    public void add(MessageCollision message){
        if (this.equals(message)){
            this.players.add(new Pair<>(message.getId(), message.getPseudo()));
        }
    }

    public void addTime(){
        this.time += 1;
    }

    public boolean equals(MessageCollision message) {
        if (this.hitterId.equals(message.getHitterId()) && this.targetId.equals(message.getTargetId()) && this.type == message.getType()) {
            return true;
        }
        return false;
    }

    // GETTERS
    public String getId(){
        return this.hitterId + this.targetId;
    }

    public String getHitterId() {
        return this.hitterId;
    }

    public String getTargetId() {
        return this.targetId;
    }

    public EnumCollision getType() {
        return this.type;
    }

    public int getNumberPlayers(){
        return this.players.size();
    }

    public int getTime(){
        return this.time;
    }

    @Override
    public String toString(){
        String result = "";

        result += "Collision: " + this.hitterId + " -> " + this.targetId;
        result += "\n voted by " + this.getNumberPlayers() + ": ";
        for (Pair<String, String> values : this.players){
            result += " - " + values.getValue();
        }
        return result;
    }
}
