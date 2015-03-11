package com.lefrantguillaume.graphicsComponent.graphics;

import com.lefrantguillaume.graphicsComponent.input.InputCheck;
import org.newdawn.slick.*;

import java.util.List;
import java.util.Observer;

/**
 * Created by andres_k on 10/03/2015.
 */
public class WindowHome extends BasicGame {

    GameContainer container;
    InputCheck input;

    public WindowHome(List<Observer> observers) {
        super("Home");
        this.input = new InputCheck();
        for (int i = 0; i < observers.size(); ++i ){
            this.input.addObserver(observers.get(i));
        }
    }

    @Override
    public void init(GameContainer container) throws SlickException {
        this.container = container;
        this.container.setForceExit(false);
    }

    @Override
    public void keyReleased(int key, char c) {
        if (input.keyCheck(key) == -1)
            container.exit();
    }

    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
    }
}
