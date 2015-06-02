package com.lefrantguillaume.graphicsComponent.graphics;

import com.lefrantguillaume.Utils.tools.Debug;
import com.lefrantguillaume.Utils.tools.StringTools;
import com.lefrantguillaume.graphicsComponent.input.InputCheck;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import org.codehaus.jettison.json.JSONException;
import org.newdawn.slick.*;
import org.newdawn.slick.opengl.SlickCallable;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;


/**
 * Created by andres_k on 10/03/2015.
 */
public class WindowAccount extends BasicGameState implements ScreenController {
    private GameContainer container;
    private StateBasedGame stateGame;
    private InputCheck input;
    private int id;
    private Nifty nifty;


    public WindowAccount(int id, Nifty nifty) throws JSONException {
        this.id = id;
        this.nifty = nifty;
        // this.input = new InputCheck(StringTools.readFile("configInput.json"));
    }

    @Override
    public int getID() {
        return this.id;
    }

    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        this.container = gameContainer;
        this.stateGame = stateBasedGame;
        this.container.setForceExit(false);
        // this.background = new Image("assets/img/ex_acc.png");


    }

    @Override
    public void enter(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        Debug.debug("OLOLOLOAZJEO IEJAOIE JAZOE JAOZE JAZEOI H");

        this.container.setTargetFrameRate(60);
        this.container.setShowFPS(false);
        this.container.setAlwaysRender(false);
        this.container.setVSync(false);

        try {

            nifty.fromXml("assets/interface/gui-account.xml", "main", this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void leave(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {

    }

    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics g) throws SlickException {
        // g.drawImage(this.background, 0, 0);
        SlickCallable.enterSafeBlock();
        nifty.render(false);
        SlickCallable.leaveSafeBlock();
    }

    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i) throws SlickException {
        nifty.update();
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
        } else if (key == Input.KEY_ESCAPE) {
            this.container.exit();
        }
    }

    @Override
    public void bind(Nifty nifty, Screen screen) {
        Debug.debug("SCREEN BIND");
    }

    @Override
    public void onStartScreen() {
        Debug.debug("SCREEN START");
    }

    @Override
    public void onEndScreen() {
        Debug.debug("SCREEN END");
    }

    public void connect() {
        System.out.println("Cliqué sur Connect !");
    }
}
