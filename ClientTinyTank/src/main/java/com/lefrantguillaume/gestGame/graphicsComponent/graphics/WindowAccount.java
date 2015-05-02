package com.lefrantguillaume.gestGame.graphicsComponent.graphics;

import com.lefrantguillaume.gestGame.Utils.tools.Debug;
import com.lefrantguillaume.gestGame.graphicsComponent.input.InputCheck;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;


/**
 * Created by andres_k on 10/03/2015.
 */
public class WindowAccount extends BasicGameState {
    private GameContainer container;
    private StateBasedGame stateGame;
    private InputCheck input;
    private int id;

    public WindowAccount(int id) {
        this.id = id;
        this.input = new InputCheck();
    }

    @Override
    public int getID() {
        return this.id;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        this.container = gameContainer;
        this.stateGame = stateBasedGame;
        this.container.setForceExit(false);
    }

    @Override
    public void enter(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException{
        this.container.setTargetFrameRate(10);
        this.container.setShowFPS(false);
        this.container.setAlwaysRender(false);
        this.container.setVSync(false);
    }

    @Override
    public void leave(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {

    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics g) throws SlickException {
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i) throws SlickException {
    }

    @Override
    public void mouseWheelMoved(int change) {
    }

    @Override
    public void keyPressed(int key, char c) {
    }

    @Override
    public void keyReleased(int key, char c) {
        if (key == Input.KEY_RETURN) {
            this.stateGame.enterState(EnumWindow.INTERFACE.getValue());
        }
        else if (key == Input.KEY_ESCAPE) {
            this.container.exit();
        }
    }

}
