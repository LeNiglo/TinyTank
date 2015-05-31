package com.lefrantguillaume.components.graphicsComponent.graphics;

import com.lefrantguillaume.components.gameComponent.controllers.AccountController;
import com.lefrantguillaume.components.taskComponent.GenericSendTask;
import org.codehaus.jettison.json.JSONException;
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

    private AccountController accountController;
    private int id;

    public WindowAccount(int id, GenericSendTask accountTask) throws JSONException {
        this.id = id;
        this.accountController = new AccountController();
        accountTask.addObserver(this.accountController);
        this.accountController.addObserver(accountTask);
    }

    @Override
    public int getID() {
        return this.id;
    }

    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        this.container = gameContainer;
        this.stateGame = stateBasedGame;
        this.accountController.setStateWindow(this.stateGame);
        this.container.setForceExit(false);
        this.accountController.init();
    }

    @Override
    public void enter(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        this.container.setTargetFrameRate(10);
        this.container.setShowFPS(false);
        this.container.setAlwaysRender(false);
        this.container.setVSync(false);
        this.accountController.createServerList();
    }

    @Override
    public void leave(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {

    }

    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics g) throws SlickException {
        this.accountController.render(g);
    }

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
            this.accountController.connectToServer();
        } else if (key == Input.KEY_ESCAPE) {
            this.container.exit();
        } else if (key == Input.KEY_SPACE) {
            this.accountController.createServerList();
        }
    }

}
