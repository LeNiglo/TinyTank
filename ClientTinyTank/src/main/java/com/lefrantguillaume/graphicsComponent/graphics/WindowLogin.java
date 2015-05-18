package com.lefrantguillaume.graphicsComponent.graphics;

import com.lefrantguillaume.Utils.configs.CurrentUser;
import com.lefrantguillaume.Utils.tools.Debug;
import com.lefrantguillaume.graphicsComponent.input.InputCheck;
import org.newdawn.slick.*;
import org.newdawn.slick.gui.TextField;
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
    private TextField fLogin = null;
    private TextField fPassword = null;
    private Image logo = null;
    private Font font = null;


    public WindowLogin(int id) {
        this.id = id;
        this.input = new InputCheck();

    }

    @Override
    public int getID() {
        return this.id;
    }

    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        this.container = gameContainer;
        this.stateGame = stateBasedGame;
        this.container.setForceExit(false);
        this.logo = new Image("assets/img/logo.png");
        this.font = new TrueTypeFont(new java.awt.Font(java.awt.Font.SANS_SERIF, java.awt.Font.PLAIN, 26), false);
        this.fLogin = new TextField(gameContainer, this.font, 320, 300, 640, 40);
        this.fPassword = new TextField(gameContainer, this.font, 320, 380, 640, 40);
    }

    @Override
    public void enter(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        this.container.setTargetFrameRate(10);
        this.container.setShowFPS(false);
        this.container.setAlwaysRender(false);
        this.container.setVSync(false);
    }

    @Override
    public void leave(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {

    }

    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics g) throws SlickException {
        //TODO animation in background ? a better UI plz
        //TODO SCALE THIS PLLZZZZZZ
        g.setBackground(org.newdawn.slick.Color.white);
        this.logo.draw(320, 100, 0.5f);
        this.fLogin.render(gameContainer, g);
        this.fPassword.render(gameContainer, g);
    }

    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i) throws SlickException {

    }

    @Override
    public void keyPressed(int key, char c) {
    }

    @Override
    public void keyReleased(int key, char c) {
        if (key == Input.KEY_RETURN) {
            if (this.container.getInput().isKeyDown(Input.KEY_LCONTROL) || this.container.getInput().isKeyDown(Input.KEY_RCONTROL)) {

                CurrentUser.setId(UUID.randomUUID().toString());
                //TODO this is supposed to be the result of the login action. the "_id" variable.
                CurrentUser.setPseudo(this.fLogin.getText());
                this.stateGame.enterState(EnumWindow.ACCOUNT.getValue());

            } else {
                Debug.debug("Login :\nUsername = " + this.fLogin.getText() + "\nPassword = " + this.fPassword.getText());
            }
        } else if (key == Input.KEY_TAB) {
            if (this.fLogin.hasFocus())
                this.fPassword.setFocus(true);
            else if (this.fPassword.hasFocus()) {
                this.fLogin.setFocus(true);
            }
        } else if (key == Input.KEY_ESCAPE) {
            this.container.exit();
        }
    }

}
