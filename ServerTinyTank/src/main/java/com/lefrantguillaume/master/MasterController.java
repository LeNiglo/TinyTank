package com.lefrantguillaume.master;

import com.lefrantguillaume.WindowController;
import com.lefrantguillaume.WindowObserver;
import com.lefrantguillaume.gameComponent.controllers.GameController;
import com.lefrantguillaume.gameComponent.gameobjects.player.Player;
import com.lefrantguillaume.gameComponent.maps.Map;
import com.lefrantguillaume.networkComponent.dataServerComponent.DataServer;
import com.lefrantguillaume.networkComponent.gameServerComponent.GameServer;
import com.lefrantguillaume.userInterface.ConsoleUserInterface;
import com.lefrantguillaume.userInterface.GraphicalUserInterface;
import com.lefrantguillaume.userInterface.UserInterface;
import com.lefrantguillaume.utils.Callback;
import com.lefrantguillaume.utils.CallbackTask;
import com.lefrantguillaume.utils.StringTools;
import javafx.util.Pair;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Styve on 08/05/2015.
 */
public class MasterController extends Observable implements Observer {
    private DataServer dataServer;
    private GameController gameController = null;
    private GameServer server;
    private UserInterface userInterface = null;
    private boolean gameStarted = false;

    public MasterController(String type) throws JSONException {
        /* TODO Get this from the data server. */
        String content = StringTools.readFile("tanks.json");

        this.initInterface(type);
        this.gameController = new GameController(new JSONObject(content));
        this.dataServer = new DataServer();
        this.gameController.addObserver(this);
        this.server = new GameServer();
        this.server.addObserver(this);
        this.loadMaps();
    }

    public void initInterface(String type) {
        if (type.equals("GUI")) {
            this.userInterface = new GraphicalUserInterface(this);
        } else if (type.equals("Console")) {
            this.userInterface = new ConsoleUserInterface(MasterController.this);
            new Thread(() -> {
                ((ConsoleUserInterface) MasterController.this.userInterface).fromConsole();
            }).start();
        }
        WindowObserver a = new WindowObserver(userInterface);
        new WindowController(a);
    }

    public void loadMaps() {
        WindowController.addConsoleMsg("load maps from : " + System.getProperty("user.dir") + "/maps");
        this.gameController.getMapController().clearMaps();
        File dir = new File("maps");
        File[] files = dir.listFiles((dir1, filename) -> {
            return filename.endsWith(".json");
        });

        if (files != null && files.length > 0) {
            for (File file : files) {
                String name = file.getName().substring(0, file.getName().lastIndexOf("."));
                WindowController.addConsoleMsg("mapName: " + name);
                if (new File("maps/" + name + ".jpg").exists()) {
                    this.parseJsonMap(file, name);
                } else {
                    System.out.println("not valid");
                }
            }
        }
        userInterface.refreshMaps();
    }

    public void parseJsonMap(File file, String name) {
        try {
            JSONObject object = new JSONObject(StringTools.readFile(file.getAbsolutePath()));
            Map map = new Map(this.gameController.getObstacleConfigData(), file, new File("maps/" + name + ".jpg"), object);
            this.gameController.addMap(map);
        } catch (Exception e) {
            System.out.println("Error in parseJson: " + e.getMessage());
        }
    }

    public void newGame() {
        if (this.gameController.getMapController().getMaps().size() > 0) {
            new Thread() {
                public void run() {
                    new CallbackTask(new Runnable() {
                        public void run() {
                            WindowController.addConsoleMsg("Connecting to the data server...");
                            if (dataServer.initServer()) {
                                WindowController.addConsoleMsg("Connected to data server !");
                            }
                        }
                    }, new Callback() {
                        @Override
                        public void complete() {
                            gameController.getMapController().setCurrentMapIndex(userInterface.getSelectedMapIndex());
                            //config = theInterface.getGameConfig();
                            //config.setMap(currentMap);
                            if (MasterController.this.server.start()) {
                                MasterController.this.gameController.startGame();
                                if (!gameStarted) {
                                    gameStarted = true;
                                    //WindowController.addConsoleMsg("Starting server...");
                                } else {
                                    //WindowController.addConsoleMsg("Restarting server...");
                                }
                                userInterface.startGame();
                            } else {
                                gameStarted = false;
                                userInterface.stopGame();
                                //WindowController.addConsoleMsg("Can't start server because you did not fill all the fields correctly !");
                            }


                        }
                    }).run();
                }
            }.start();
        } else {
            userInterface.tellNoMap();
        }
    }

    public void stopGame() {
        gameStarted = false;
        userInterface.stopGame();
        dataServer.stopServer();
        gameController.stopGame();
    }

    public List<Map> getMaps() {
        return this.gameController.getMapController().getMaps();
    }

    public HashMap<String, Player> getPlayers() {
        return this.gameController.getPlayers();
    }

    public void update(Observable o, Object arg) {
        Pair<EnumTargetTask, Object> task = (Pair<EnumTargetTask, Object>) arg;

        if (task.getKey().equals(EnumTargetTask.GAME)) {
            this.gameController.doTask(o, task.getValue());
        } else if (task.getKey().equals(EnumTargetTask.NETWORK)) {
            this.server.doTask(o, task.getValue());
        } else if (task.getKey().equals(EnumTargetTask.MASTER_SERVER)) {
            this.dataServer.doTask(o, task.getValue());
            this.userInterface.refreshPlayers();
        } else if (task.getKey().equals(EnumTargetTask.MASTER_CONTROLLER)) {
            this.doTask(o, task.getValue());
        }
    }

    public void doTask(Observable o, Object arg){
        if (arg instanceof String) {
            String msg = (String) arg;
            switch (msg) {
                case "start game":
                    new Thread() {
                        public void run() {
                            newGame();
                        }
                    }.start();
                    break;
                case "stop game":
                    stopGame();
                    break;
                case "reload maps":
                    loadMaps();
                    break;
                default:
                    WindowController.addConsoleMsg("Not handled: " + msg);
                    break;
            }
        }
    }
}
