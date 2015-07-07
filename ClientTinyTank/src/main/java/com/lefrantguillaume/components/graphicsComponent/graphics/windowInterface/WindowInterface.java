package com.lefrantguillaume.components.graphicsComponent.graphics.windowInterface;

import com.lefrantguillaume.components.gameComponent.animations.AnimatorInterfaceData;
import com.lefrantguillaume.components.gameComponent.controllers.InterfaceController;
import com.lefrantguillaume.components.graphicsComponent.graphics.EnumWindow;
import com.lefrantguillaume.components.taskComponent.GenericSendTask;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import org.codehaus.jettison.json.JSONException;
import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Created by andres_k on 10/03/2015.
 */
public class WindowInterface extends BasicGameState implements ScreenController {
    private GameContainer container;
    private StateBasedGame stateWindow;

    private AnimatorInterfaceData animatorData;
    private InterfaceController interfaceController;
    private Music theme;
    private Nifty nifty;
    private int id;

    public WindowInterface(int id, Nifty nifty, GenericSendTask interfaceTask) throws JSONException {
        this.id = id;
        this.nifty = nifty;
        this.animatorData = new AnimatorInterfaceData();
        this.interfaceController = new InterfaceController();

        interfaceTask.addObserver(this.interfaceController);
        this.interfaceController.addObserver(interfaceTask);
    }

    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException
    {
        this.stateWindow = sbg;
        this.interfaceController.setStateWindow(this.stateWindow);
        this.container = gc;
        this.container.setForceExit(false);
        this.animatorData.init();
        this.interfaceController.initAnimator(this.animatorData.getBackgroundAnimators(), this.animatorData.getButtonAnimators(),
                this.animatorData.getTankStatAnimators(), this.animatorData.getTankPreviewAnimators());
        this.theme = new Music("assets/old/music/select.ogg");
    }

    public void enter(GameContainer gameContainer, StateBasedGame sbg) throws SlickException {
        this.container = gameContainer;
        this.container.setTargetFrameRate(10);
        this.container.setShowFPS(false);
        this.container.setAlwaysRender(false);
        this.container.setVSync(false);

        this.nifty.gotoScreen("screen-interface");
        this.theme.loop();
    }

    public void leave(GameContainer gameContainer, StateBasedGame sbg) throws SlickException {
        this.theme.stop();
    }

    public void render(GameContainer gameContainer, StateBasedGame sbg, Graphics g) throws SlickException {
        this.interfaceController.draw(g);
    }

    public void update(GameContainer gameContainer, StateBasedGame sbg, int i) throws SlickException {
        this.nifty.update();
    }

    @Override
    public void mouseWheelMoved(int change) {
    }

    @Override
    public int getID() {
        return this.id;
    }

    @Override
    public void keyPressed(int key, char c) {
    }

    @Override
    public void keyReleased(int key, char c) {
        if (key == Input.KEY_RETURN) {
            if (this.interfaceController.loadGame() == true) {
                this.stateWindow.enterState(EnumWindow.GAME.getValue());
            }
        } else if (key == Input.KEY_LEFT || key == Input.KEY_RIGHT) {
            this.interfaceController.changeCurrentTank(key);
        } else if (key == Input.KEY_ESCAPE) {
            this.stateWindow.enterState(EnumWindow.ACCOUNT.getValue());
        }
    }

    @Override
    public void bind(Nifty nifty, Screen screen) {

    }

    @Override
    public void onStartScreen() {}

    @Override
    public void onEndScreen() {}
}
