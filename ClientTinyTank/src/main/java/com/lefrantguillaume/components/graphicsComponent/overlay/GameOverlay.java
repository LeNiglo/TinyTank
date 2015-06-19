package com.lefrantguillaume.components.graphicsComponent.overlay;

import com.lefrantguillaume.Utils.stockage.Tuple;
import com.lefrantguillaume.components.taskComponent.EnumTargetTask;
import org.newdawn.slick.Graphics;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by andres_k on 20/06/2015.
 */
public class GameOverlay extends Observable implements Observer {
    private boolean activated;

    public GameOverlay() {
        this.activated = false;
    }


    // FUNCTIONS
    @Override
    public void update(Observable o, Object arg) {
        Tuple<EnumTargetTask, EnumTargetTask, Object> received = (Tuple<EnumTargetTask, EnumTargetTask, Object>) arg;

        if (received.getV2().equals(EnumTargetTask.GAME_OVERLAY)) {
        }
    }

    public void doAction(Object o) {

    }

    public void draw(Graphics g) {

    }

    // GETTERS

    public boolean isActivated() {
        return this.activated;
    }
}
