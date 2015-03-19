package com.lefrantguillaume.graphicsComponent.graphics;

import com.lefrantguillaume.Utils.tools.Debug;
import com.lefrantguillaume.authComponent.AuthenticationController;
import com.lefrantguillaume.gameComponent.animations.AnimatorData;
import com.lefrantguillaume.graphicsComponent.input.EnumInput;
import com.lefrantguillaume.graphicsComponent.input.InputCheck;
import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.util.List;
import java.util.Observer;

/**
 * Created by andres_k on 10/03/2015.
 */
public class WindowHome extends BasicGameState {
    private AnimatorData animatorData;
    private AuthenticationController authController;
    private GameContainer container;
    private StateBasedGame stateGame;
    private InputCheck input;
    private int id;

    public WindowHome(int id, List<Observer> observers, Object authController) {
        this.id = id;
        this.animatorData = new AnimatorData();
        this.authController = (AuthenticationController) authController;
        this.input = new InputCheck();
        for (int i = 0; i < observers.size(); ++i) {
            this.input.addObserver(observers.get(i));
        }
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        Debug.debug("init Home");
        this.container = gameContainer;
        this.stateGame = stateBasedGame;
        this.container.setForceExit(false);
    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {

    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i) throws SlickException {

    }

    @Override
    public void keyPressed(int key, char c) {

    }

    @Override
    public void keyReleased(int key, char c) {
        if (key == Input.KEY_RETURN) {
            this.stateGame.enterState(EnumWindow.GAME.getValue());
        }
        else if (key == Input.KEY_ESCAPE){
            this.container.exit();
        }
    }

}
