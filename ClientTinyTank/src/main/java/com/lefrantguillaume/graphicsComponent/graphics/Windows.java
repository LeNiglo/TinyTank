package com.lefrantguillaume.graphicsComponent.graphics;

import com.lefrantguillaume.gameComponent.controllers.InterfaceController;
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

    public Windows(String name, List<Observer> observers, InterfaceController interfaceController, GameController gameController) {
        super(name);
        this.addState(new WindowInterface(EnumWindow.HOME.getValue(), observers, interfaceController));
        this.addState(new WindowGame(EnumWindow.GAME.getValue(), observers, gameController));
    }

    @Override
    public void initStatesList(GameContainer gameContainer) throws SlickException {
        this.enterState(EnumWindow.HOME.getValue());
    }
}
