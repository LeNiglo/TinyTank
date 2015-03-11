package com.lefrantguillaume.master;

import com.lefrantguillaume.Utils.NetworkConfig;
import com.lefrantguillaume.graphicsComponent.input.InputGameObserver;
import com.lefrantguillaume.graphicsComponent.input.InputHomeObserver;
import com.lefrantguillaume.graphicsComponent.input.InputObserver;
import com.lefrantguillaume.Utils.WindowConfig;
import com.lefrantguillaume.graphicsComponent.graphics.WindowFactory;
import com.lefrantguillaume.graphicsComponent.graphics.WindowGame;
import com.lefrantguillaume.graphicsComponent.graphics.WindowHome;
import com.lefrantguillaume.networkComponent.NetworkController;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

/**
 * Created by andres_k on 10/03/2015.
 */
public class Master {
    private List<String> networkQueue;
    private WindowConfig windowConfig;
    private NetworkConfig networkConfig;
    private InputObserver inputGameObserver;
    private InputObserver inputHomeObserver;
    private NetworkController networkInputController;

    public Master(int x, int y) {
        init(x, y);
    }

    private void init(int x, int y) {
        this.networkQueue = new ArrayList<String>();
        this.windowConfig = new WindowConfig(x, y);
        this.networkConfig = new NetworkConfig();
        this.networkInputController = new NetworkController(this.networkQueue, networkConfig);
        this.inputGameObserver = new InputGameObserver(this.networkQueue, this.networkInputController);
        this.inputHomeObserver = new InputHomeObserver(this.networkQueue);
    }

    public void start() {
        if (this.launchHome() == true) {
            this.launchGame();
        }
    }

    public boolean launchHome() {
        try {
            List<Observer> observers = new ArrayList<Observer>();
            observers.add(inputHomeObserver);
            AppGameContainer game = WindowFactory.windowFactory(observers, WindowHome.class, this.windowConfig, false);
            game.start();
        } catch (SlickException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean launchGame() {
        try {
            List<Observer> observers = new ArrayList<Observer>();
            observers.add(inputGameObserver);
            AppGameContainer game = WindowFactory.windowFactory(observers, WindowGame.class, this.windowConfig, false);
            game.start();
        } catch (SlickException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return true;
    }
}
