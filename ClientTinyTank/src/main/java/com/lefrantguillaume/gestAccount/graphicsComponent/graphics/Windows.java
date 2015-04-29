package com.lefrantguillaume.gestAccount.graphicsComponent.graphics;

import com.lefrantguillaume.gestGame.gameComponent.controllers.GameController;
import com.lefrantguillaume.gestGame.gameComponent.controllers.InterfaceController;
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
//        this.addState();
    }

    @Override
    public void initStatesList(GameContainer gameContainer) throws SlickException {
        this.enterState(EnumWindow.INTERFACE.getValue());
    }
}
