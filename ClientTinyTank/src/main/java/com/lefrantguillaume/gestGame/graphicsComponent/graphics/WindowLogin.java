package com.lefrantguillaume.gestGame.graphicsComponent.graphics;

import com.lefrantguillaume.gestGame.Utils.configs.CurrentUser;
import com.lefrantguillaume.gestGame.graphicsComponent.input.InputCheck;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Shape;
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

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        this.container = gameContainer;
        this.stateGame = stateBasedGame;
        this.container.setForceExit(false);
//        Image img = new Image("assets/img/game/effect/account");
  //      tmp.addFrame(img, 150);

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
    //    g.drawAnimation(tmp, 0, 0);
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
            CurrentUser.setId(UUID.randomUUID().toString());
            CurrentUser.setPseudo("Kevin");
            this.stateGame.enterState(EnumWindow.ACCOUNT.getValue());
        }
        else if (key == Input.KEY_ESCAPE) {
            this.container.exit();
        }
    }

}
