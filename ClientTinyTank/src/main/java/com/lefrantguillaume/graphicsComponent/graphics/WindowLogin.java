package com.lefrantguillaume.graphicsComponent.graphics;

import com.lefrantguillaume.Utils.configs.CurrentUser;
import com.lefrantguillaume.graphicsComponent.input.InputCheck;
import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.util.UUID;


/**
 * Created by andres_k on 10/03/2015.
 */
public class WindowLogin extends BasicGameState {
    private GameContainer container;
    private StateBasedGame stateGame;
    private InputCheck input;
    private int id;
    private Image background = null;

    private Animation tmp;

    public WindowLogin(int id) {
        this.id = id;
        this.input = new InputCheck();

        tmp = new Animation();
    }

    @Override
    public int getID() {
        return this.id;
    }

    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        this.container = gameContainer;
        this.stateGame = stateBasedGame;
        this.container.setForceExit(false);
        this.background = new Image("assets/img/login.png");
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

    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics g) throws SlickException {
        g.drawImage(this.background, 0, 0);
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
            CurrentUser.setId(UUID.randomUUID().toString());
            CurrentUser.setPseudo("Kevin");
            this.stateGame.enterState(EnumWindow.ACCOUNT.getValue());
        }
        else if (key == Input.KEY_ESCAPE) {
            this.container.exit();
        }
    }

}
