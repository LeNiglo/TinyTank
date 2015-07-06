package com.lefrantguillaume.gameComponent.collisionVote;

import com.lefrantguillaume.WindowController;
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
    private List<Pair<String, String>> users;

    public CollisionVoteElement(MessageCollision message){
        this.hitterId = message.getHitterId();
        this.targetId = message.getTargetId();
        this.type = message.getType();
        this.users = new ArrayList<>();
        this.users.add(new Pair<>(message.getId(), message.getPseudo()));
    }

    // FUNCTIONS

    public boolean containUser(MessageCollision message){
        for (Pair<String, String> values : this.users){
            if (values.getKey().equals(message.getId()) && values.getValue().equals(message.getPseudo())){
                return true;
            }
        }
        return false;
    }

    public void add(MessageCollision message){
        if (this.equals(message)) {
            if (this.containUser(message) == false) {
                this.users.add(new Pair<>(message.getId(), message.getPseudo()));
            } else {
                WindowController.addConsoleMsg("USER ALREADY IN");
            }
        }
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

    public float getNumberUsers(){
        return this.users.size();
    }

    @Override
    public String toString(){
        String result = "";

        result += "Collision: " + this.hitterId + " -> " + this.targetId;
        result += "\n voted by " + this.getNumberUsers() + "-> ";
        for (Pair<String, String> values : this.users){
            if (!result.equals("")){
                result += " , ";
            }
            result += values.getValue();
        }
        return result;
    }
}
