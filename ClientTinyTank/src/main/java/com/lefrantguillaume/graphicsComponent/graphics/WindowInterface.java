package com.lefrantguillaume.graphicsComponent.graphics;

import com.lefrantguillaume.Utils.tools.Debug;
import com.lefrantguillaume.gameComponent.animations.AnimatorInterfaceData;
import com.lefrantguillaume.graphicsComponent.input.InputCheck;
import com.lefrantguillaume.interfaceComponent.InterfaceController;
import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

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

    public WindowInterface(int id, List<Observer> observers, Object interfaceController) {
        this.id = id;
        this.animatorData = new AnimatorInterfaceData();
        this.interfaceController = (InterfaceController) interfaceController;
        this.input = new InputCheck();
        for (int i = 0; i < observers.size(); ++i) {
            this.input.addObserver(observers.get(i));
        }
    }

    @Override
    public int getID() {
        return this.id;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        Debug.debug("init Home");
        this.container = gameContainer;
        this.stateGame = stateBasedGame;
        this.container.setForceExit(false);
        this.animatorData.init();
        this.interfaceController.initAnimator(this.animatorData.getBackgroundAnimators(), this.animatorData.getButtonAnimators(),
                this.animatorData.getTankStatAnimators(), this.animatorData.getTankPreviewAnimators());
    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics g) throws SlickException {
        this.interfaceController.drawBackground(g);
        this.interfaceController.drawCurrentTankStat(g);
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i) throws SlickException {

    }

    @Override
    public void mouseWheelMoved(int change) {
        System.out.println(change);
    }

    @Override
    public void keyPressed(int key, char c) {

    }

    @Override
    public void keyReleased(int key, char c) {
        if (key == Input.KEY_RETURN) {
            this.stateGame.enterState(EnumWindow.GAME.getValue());
        }
        else if (key == Input.KEY_LEFT || key == Input.KEY_RIGHT){
            this.interfaceController.changeCurrentTank(key);
        }
        else if (key == Input.KEY_ESCAPE) {
            this.container.exit();
        }
    }

}
