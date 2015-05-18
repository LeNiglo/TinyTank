package com.lefrantguillaume.master;

import com.lefrantguillaume.WindowController;
import com.lefrantguillaume.WindowObserver;
import com.lefrantguillaume.gameComponent.game.GameController;
import com.lefrantguillaume.gameComponent.gameobjects.player.Player;
import com.lefrantguillaume.gameComponent.maps.Map;
import com.lefrantguillaume.network.TinyServer;
import com.lefrantguillaume.network.master.MasterServer;
import com.lefrantguillaume.userInterface.UserInterface;
import com.lefrantguillaume.userInterface.GraphicalUserInterface;
import com.lefrantguillaume.userInterface.ConsoleUserInterface;
import com.lefrantguillaume.utils.Callback;
import com.lefrantguillaume.utils.CallbackTask;
import javafx.util.Pair;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.*;

/**
 * Created by Styve on 08/05/2015.
 */
public class MasterController extends Observable implements Observer {
    private MasterServer masterServer;
    private GameController gameController = null;
    private TinyServer server;
    private UserInterface userInterface = null;
    private boolean gameStarted = false;

    public MasterController(String type) throws JSONException {
        /*
        TODO Get this from the master.
        */
        String content = null;
        File file = new File("tanks.json");
        try {
            FileReader reader = new FileReader(file);
            char[] chars = new char[(int) file.length()];
            reader.read(chars);
            content = new String(chars);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*
        Until here
         */
        this.initInterface(type);
        this.gameController = new GameController(new JSONObject(content));
        this.masterServer = new MasterServer();
        this.gameController.addObserver(this);
        this.server = new TinyServer();
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
        WindowController.addConsoleMsg("load maps");
        this.gameController.getMapController().clearMaps();
        File dir = new File("maps");
        File[] files = dir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String filename) {
                return filename.endsWith(".json");
            }
        });

        if (files != null && files.length > 0) {
            for (File file : files) {
                String name = file.getName().substring(0, file.getName().lastIndexOf("."));
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
            FileReader reader = new FileReader(file);
            JSONParser parser = new JSONParser();
            JSONObject object = (JSONObject) parser.parse(reader);
            Map map = new Map();
            map.setFileNameNoExt(name);
            map.setName((String) (object.get("name") != null ? object.get("name") : name));
            map.setFilePath(file.getAbsolutePath());
            map.setFileName(file.getName());
            map.setFileLength(file.length());
            file = new File("maps/" + name + ".jpg");
            map.setImgName(file.getName());
            map.setImgPath(file.getPath());
            map.setImgLength(file.length());
            this.gameController.getMapController().addMap(map);
        } catch (Exception e) {
            System.out.println("Parse JSON: " + e.getMessage());
        }
    }

    public void newGame() {
        if (this.gameController.getMapController().getMaps().size() > 0) {
            new Thread() {
                public void run() {
                    new CallbackTask(new Runnable() {
                        public void run() {
                            WindowController.addConsoleMsg("Connecting to the master server...");
                            if (masterServer.initServer()) {
                                WindowController.addConsoleMsg("Connected to master server !");
                            }
                        }
                    }, new Callback() {
                        @Override
                        public void complete() {
                            gameController.getMapController().setCurrentMapIndex(userInterface.getSelectedMapIndex());
                            //config = theInterface.getGameConfig();
                            //config.setMap(currentMap);
                            if (MasterController.this.server.start()) {
                                MasterController.this.gameController.onGameStart();
                                if (!gameStarted) {
                                    gameStarted = true;
                                    //WindowController.addConsoleMsg("Starting server...");
                                } else {
                                    //WindowController.addConsoleMsg("Restarting server...");
                                }
                                userInterface.gameStarted();
                            } else {
                                gameStarted = false;
                                userInterface.gameStopped();
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
        userInterface.gameStopped();
        masterServer.stopServer();
        gameController.onGameStop();
    }

    public List<Map> getMaps() {
        return this.gameController.getMapController().getMaps();
    }

    public HashMap<String, Player> getPlayers() {
        return this.gameController.getPlayers();
    }

    public void update(Observable o, Object arg) {
        Pair<EnumController, Object> task = (Pair<EnumController, Object>) arg;

        if (task.getKey().equals(EnumController.GAME)) {
            this.gameController.doTask(o, task.getValue());
        } else if (task.getKey().equals(EnumController.NETWORK)) {
            this.server.doTask(o, task.getValue());
        } else if (task.getKey().equals(EnumController.MASTER_SERVER)) {
            this.masterServer.doTask(o, task.getValue());
            this.userInterface.refreshPlayers();
        } else if (task.getKey().equals(EnumController.MASTER_CONTROLLER)) {
            this.doTask(o, arg);
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
