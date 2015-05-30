package com.lefrantguillaume.graphicsComponent.graphics;

import com.lefrantguillaume.Utils.configs.CurrentUser;
import com.lefrantguillaume.Utils.stockage.Pair;
import com.lefrantguillaume.Utils.tools.Debug;
import com.lefrantguillaume.Utils.tools.StringTools;
import com.lefrantguillaume.gameComponent.animations.AnimatorInterfaceData;
import com.lefrantguillaume.graphicsComponent.input.InputCheck;
import com.lefrantguillaume.gameComponent.controllers.InterfaceController;
import com.lefrantguillaume.networkComponent.ServerEntry;
import com.lefrantguillaume.networkComponent.networkData.DataServer;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Observer;

/**
 * Created by andres_k on 10/03/2015.
 */
public class WindowInterface extends BasicGameState {
    private AnimatorInterfaceData animatorData;
    private InterfaceController interfaceController;
    private GameContainer container;
    private StateBasedGame stateGame;
    private InputCheck input;
    private int id;

    public WindowInterface(int id, List<Observer> observers, Object interfaceController) throws JSONException {
        this.id = id;
        this.animatorData = new AnimatorInterfaceData();
        this.interfaceController = (InterfaceController) interfaceController;
        this.input = new InputCheck(StringTools.readFile("configInput.json"));
        for (int i = 0; i < observers.size(); ++i) {
            this.input.addObserver(observers.get(i));
            this.interfaceController.addObserver(observers.get(i));
        }
    }

    @Override
    public int getID() {
        return this.id;
    }

    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException
    {
        Debug.debug("init Home");
        this.stateGame = sbg;
        this.container = gc;
        this.container.setForceExit(false);
        this.animatorData.init();
        this.interfaceController.initAnimator(this.animatorData.getBackgroundAnimators(), this.animatorData.getButtonAnimators(),
                this.animatorData.getTankStatAnimators(), this.animatorData.getTankPreviewAnimators());
    }

    public void enter(GameContainer gameContainer, StateBasedGame sbg) throws SlickException {
        container = gameContainer;
        this.container.setTargetFrameRate(10);
        this.container.setShowFPS(false);
        this.container.setAlwaysRender(false);
        this.container.setVSync(false);

    }

    public void leave(GameContainer gameContainer, StateBasedGame sbg) throws SlickException {

    }

    public void render(GameContainer gameContainer, StateBasedGame sbg, Graphics g) throws SlickException {
        this.interfaceController.drawBackground(g);
        this.interfaceController.drawCurrentTankStat(g);
    }

    public void update(GameContainer gameContainer, StateBasedGame sbg, int i) throws SlickException {

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
            if (this.interfaceController.loadGame() == true) {
                this.stateGame.enterState(EnumWindow.GAME.getValue());
            }
        } else if (key == Input.KEY_LEFT || key == Input.KEY_RIGHT) {
            this.interfaceController.changeCurrentTank(key);
        } else if (key == Input.KEY_ESCAPE) {
            this.stateGame.enterState(EnumWindow.ACCOUNT.getValue());
        }
    }

}
