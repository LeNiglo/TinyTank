package com.lefrantguillaume.graphicsComponent.graphics;

import com.lefrantguillaume.graphicsComponent.input.*;
import org.newdawn.slick.*;
import org.newdawn.slick.tiled.TiledMap;

import java.util.List;
import java.util.Observer;


/**
 * Created by andres_k on 10/03/2015.
 */

public class WindowGame extends BasicGame {

    GameContainer container;
    TiledMap map;
    InputCheck input;

    public WindowGame(List<Observer> observers) {

        super("Game");
        this.input = new InputCheck();
        for (int i = 0; i < observers.size(); ++i ){
            this.input.addObserver(observers.get(i));
        }
    }

    @Override
    public void init(GameContainer container) throws SlickException {
        this.container = container;
        this.container.setForceExit(false);
        this.map = new TiledMap("map/background.tmx");
    }

    @Override
    public void keyReleased(int key, char c) {
        if (input.keyCheck(key) == -1)
            container.exit();
    }

    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
        this.map.render(0, 0);
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
    }
}
