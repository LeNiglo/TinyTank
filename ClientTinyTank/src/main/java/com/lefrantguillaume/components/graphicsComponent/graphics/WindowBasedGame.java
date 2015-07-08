package com.lefrantguillaume.components.graphicsComponent.graphics;

import org.newdawn.slick.state.BasicGameState;

/**
 * Created by andres_k on 08/07/2015.
 */
public abstract class WindowBasedGame extends BasicGameState {

    public abstract void clean();
    public abstract void quit();
}
