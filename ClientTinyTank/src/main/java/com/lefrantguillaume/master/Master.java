package com.lefrantguillaume.master;

import com.lefrantguillaume.Utils.configs.NetworkConfig;
import com.lefrantguillaume.Utils.configs.User;
import com.lefrantguillaume.authComponent.AuthenticationController;
import com.lefrantguillaume.gameComponent.GameController;
import com.lefrantguillaume.gameComponent.animations.AnimatorData;
import com.lefrantguillaume.graphicsComponent.input.InputGameObserver;
import com.lefrantguillaume.graphicsComponent.input.InputHomeObserver;
import com.lefrantguillaume.graphicsComponent.input.InputObserver;
import com.lefrantguillaume.Utils.configs.WindowConfig;
import com.lefrantguillaume.graphicsComponent.graphics.WindowFactory;
import com.lefrantguillaume.graphicsComponent.graphics.WindowGame;
import com.lefrantguillaume.graphicsComponent.graphics.WindowHome;
import com.lefrantguillaume.networkComponent.NetworkCall;
import com.lefrantguillaume.networkComponent.NetworkMessage;
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
    private User user;
    private GameController gameController;
    private AuthenticationController authController;
    private AnimatorData animatorData;
    private NetworkCall networkCall;
    private NetworkMessage masterRequestQueue;
    private NetworkMessage masterResponseQueue;
    private WindowConfig windowConfig;
    private NetworkConfig networkConfig;
    private InputObserver inputGameObserver;
    private InputObserver inputHomeObserver;
    private MasterRequestController masterRequestController;
    private MasterResponseController masterResponseController;

    public Master(int x, int y) {
        init(x, y);
    }

    private void init(int x, int y) {
        this.windowConfig = new WindowConfig(x, y);
        this.user = new User("unknown", 0);
        this.animatorData = new AnimatorData();
        this.initGame();
        this.initNetwork();
        this.initInput();
    }
    private void initGame(){
        this.gameController = new GameController();
    }
    private void initNetwork() {
        this.masterRequestQueue = new NetworkMessage();
        this.masterResponseQueue = new NetworkMessage();
        this.masterResponseController = new MasterResponseController(this.masterResponseQueue);
        this.networkConfig = new NetworkConfig();
        this.networkCall = new NetworkCall(this.networkConfig);
        this.networkCall.addObserver(this.masterResponseController);
        this.masterRequestController = new MasterRequestController(this.masterRequestQueue, this.networkCall);
        this.masterRequestQueue.addObserver(this.masterRequestController);
    }

    private void initInput() {
        this.inputGameObserver = new InputGameObserver(this.masterRequestQueue);
        this.inputHomeObserver = new InputHomeObserver(this.masterRequestQueue);
    }

    public void start() {
 //       if (this.launchHome() == true) {
            this.launchGame();
 //       }
    }

    public boolean launchHome() {
        try {
            List<Observer> observers = new ArrayList<Observer>();
            observers.add(inputHomeObserver);
            this.masterResponseController.addObserver(this.authController);
            AppGameContainer game = WindowFactory.windowFactory(observers, WindowHome.class, this.windowConfig, this.animatorData, this.authController, false);
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
        this.masterResponseController.deleteObservers();
        return true;
    }

    public boolean launchGame() {
        try {
            List<Observer> observers = new ArrayList<Observer>();
            observers.add(inputGameObserver);
            this.masterResponseController.addObserver(this.gameController);
            AppGameContainer game = WindowFactory.windowFactory(observers, WindowGame.class, this.windowConfig, this.animatorData, this.gameController, false);
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
        this.masterResponseController.deleteObservers();
        return true;
    }
}
