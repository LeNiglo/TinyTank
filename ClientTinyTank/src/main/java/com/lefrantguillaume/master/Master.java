package com.lefrantguillaume.master;

import com.lefrantguillaume.Utils.configs.MasterConfig;
import com.lefrantguillaume.Utils.configs.CurrentUser;
import com.lefrantguillaume.Utils.configs.NetworkServerConfig;
import com.lefrantguillaume.interfaceComponent.InterfaceController;
import com.lefrantguillaume.gameComponent.controllers.GameController;
import com.lefrantguillaume.graphicsComponent.graphics.Windows;
import com.lefrantguillaume.graphicsComponent.input.InputGameObserver;
import com.lefrantguillaume.graphicsComponent.input.InputHomeObserver;
import com.lefrantguillaume.graphicsComponent.input.InputObserver;
import com.lefrantguillaume.Utils.configs.WindowConfig;
import com.lefrantguillaume.networkComponent.NetworkCall;
import com.lefrantguillaume.networkComponent.NetworkMessage;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

/**
 * Created by andres_k on 10/03/2015.
 */
public class Master {
    private CurrentUser currentUser;
    private GameController gameController;
    private InterfaceController interfaceController;
    private NetworkCall networkCall;
    private NetworkMessage masterRequestQueue;
    private NetworkMessage masterResponseQueue;
    private WindowConfig windowConfig;
    private InputObserver inputGameObserver;
    private InputObserver inputHomeObserver;
    private MasterRequestController masterRequestController;
    private MasterResponseController masterResponseController;

    public Master() throws SlickException {
        this.windowConfig = new WindowConfig();
        this.currentUser = new CurrentUser("unknown", "541d51");
        this.initGame();
        this.initNetwork();
        this.initInput();
    }

    private void initGame() throws SlickException {
        this.gameController = new GameController();
        this.interfaceController = new InterfaceController();
    }

    private void initNetwork() {
        this.masterRequestQueue = new NetworkMessage();
        this.masterResponseQueue = new NetworkMessage();
        this.masterResponseController = new MasterResponseController(this.masterResponseQueue);
        this.networkCall = new NetworkCall(new NetworkServerConfig(MasterConfig.getMasterUdpPort(), MasterConfig.getMasterTcpPort(), MasterConfig.getMasterAddress()));
        this.networkCall.addObserver(this.masterResponseController);
        this.masterRequestController = new MasterRequestController(this.masterRequestQueue, this.networkCall);
        this.masterRequestQueue.addObserver(this.masterRequestController);
    }

    private void initInput() {
        this.inputGameObserver = new InputGameObserver(this.masterRequestQueue);
        this.inputHomeObserver = new InputHomeObserver(this.masterRequestQueue);
    }

    public void start() {
        AppGameContainer appGame = null;
        try {
            this.startGame(appGame);
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

    private void startGame(AppGameContainer appGame) throws SlickException {
        List<Observer> interfaceObservers = new ArrayList<Observer>();
        interfaceObservers.add(inputHomeObserver);
        this.masterResponseController.addObserver(this.interfaceController);
        List<Observer> gameObservers = new ArrayList<Observer>();
        gameObservers.add(inputGameObserver);
        this.masterResponseController.addObserver(this.gameController);
        appGame = new AppGameContainer(new Windows("TinyTank", interfaceObservers, this.interfaceController, gameObservers, this.gameController));
        appGame.setDisplayMode(WindowConfig.getSizeX(), WindowConfig.getSizeY(), false);
        appGame.start();

    }
}
