package com.lefrantguillaume.graphicsComponent.graphics;

import com.lefrantguillaume.Utils.tools.Debug;
import com.lefrantguillaume.authComponent.AuthenticationController;
import com.lefrantguillaume.gameComponent.controllers.GameController;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import java.util.List;
import java.util.Observer;

/**
 * Created by andres_k on 17/03/2015.
 */
public class Windows extends StateBasedGame {

    public Windows(String name, List<Observer> homeObservers, AuthenticationController homeController, List<Observer> gameObservers, GameController gameController) {
        super(name);
        this.addState(new WindowHome(EnumWindow.HOME.getValue(), homeObservers, homeController));
        this.addState(new WindowGame(EnumWindow.GAME.getValue(), gameObservers, gameController));
    }

    @Override
    public void initStatesList(GameContainer gameContainer) throws SlickException {
        this.enterState(EnumWindow.HOME.getValue());
    }
}
