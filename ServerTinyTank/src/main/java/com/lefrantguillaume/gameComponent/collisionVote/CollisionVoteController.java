package com.lefrantguillaume.gameComponent.collisionVote;

import com.lefrantguillaume.WindowController;
import com.lefrantguillaume.networkComponent.gameServerComponent.clientmsgs.MessageCollision;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andres_k on 05/07/2015.
 */
public class CollisionVoteController {
    private List<CollisionVoteElement> collisionVotes;

    public CollisionVoteController() {
        this.collisionVotes = new ArrayList<>();
    }

    public int contains(MessageCollision message) {
        for (int i = 0; i < this.collisionVotes.size(); ++i) {
            if (this.collisionVotes.get(i).equals(message)) {
                return i;
            }
        }
        return -1;
    }

    public CollisionVoteElement add(MessageCollision message) {
        CollisionVoteElement existed = this.getVote(message);

        WindowController.addConsoleMsg("\nVOTE TRY ADD: " + message.getHitterId() + " - " + message.getTargetId() + " - " + message.getType());
        if (existed != null) {
            WindowController.addConsoleMsg("ADD VOTE");
            existed.add(message);
            return null;
        } else {
            WindowController.addConsoleMsg("NEW VOTE");
            this.collisionVotes.add(0, new CollisionVoteElement(message));
            return this.getVote(message);
        }
    }

    public void delete(MessageCollision message) {
        int index = this.contains(message);

        if (index >= 0) {
            WindowController.addConsoleMsg("VOTE DELETE: " + message.getHitterId() + " - " + message.getTargetId());
            this.collisionVotes.remove(index);
        } else {
            WindowController.addConsoleMsg("DELETE FAILED " + message.getHitterId() + " - " + message.getTargetId() + " - " + message.getType());
        }
    }

    public CollisionVoteElement getVote(MessageCollision message) {
        for (CollisionVoteElement collision : this.collisionVotes) {
            if (collision.equals(message)) {
                return collision;
            }
        }
        return null;
    }
}
