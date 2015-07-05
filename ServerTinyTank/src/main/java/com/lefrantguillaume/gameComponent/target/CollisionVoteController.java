package com.lefrantguillaume.gameComponent.target;

import com.lefrantguillaume.networkComponent.gameServerComponent.clientmsgs.MessageCollision;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andres_k on 05/07/2015.
 */
public class CollisionVoteController {
    private List<CollisionVoteElement> collisionVotes;

    public CollisionVoteController(){
        this.collisionVotes = new ArrayList<>();
    }

    public boolean contains(MessageCollision message){
        for (CollisionVoteElement collision : this.collisionVotes){
            if (collision.equals(message)){
                return true;
            }
        }
        return false;
    }

    public void update(){
        this.collisionVotes.forEach(CollisionVoteElement::addTime);
        this.clearVotes();
    }

    public CollisionVoteElement add(MessageCollision message) {
        CollisionVoteElement existed = this.getVote(message);

        if (existed != null) {
            existed.add(message);
            return null;
        } else {
            this.collisionVotes.add(0, new CollisionVoteElement(message));
            return this.getVote(message);
        }
    }

    public void clearVotes(){
        for (int i = 0; i < this.collisionVotes.size(); ++i){
            if (this.collisionVotes.get(i).getTime() > 1000){
                this.collisionVotes.remove(i);
            }
        }
    }

    public CollisionVoteElement getVote(MessageCollision message){
        for (CollisionVoteElement collision : this.collisionVotes){
            if (collision.equals(message)){
                return collision;
            }
        }
        return null;
    }
}
