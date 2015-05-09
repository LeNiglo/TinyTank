package com.lefrantguillaume.game;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import com.lefrantguillaume.WindowController;
import com.lefrantguillaume.WindowObserver;
import com.lefrantguillaume.game.gameobjects.player.Player;
import com.lefrantguillaume.network.MessageData;
import com.lefrantguillaume.network.SendFile;
import com.lefrantguillaume.network.clientmsgs.*;
import com.lefrantguillaume.network.master.Master;
import com.lefrantguillaume.ui.IInterface;
import com.lefrantguillaume.ui.ServerGUI;
import com.lefrantguillaume.ui.UserIO;
import com.lefrantguillaume.utils.Callback;
import com.lefrantguillaume.utils.CallbackTask;
import com.lefrantguillaume.utils.GameConfig;
import com.lefrantguillaume.utils.MD5;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Styve on 08/05/2015.
 */
public class GameController implements Observer {
    private Game game = null;
    private GameConfig config = new GameConfig();
    private ArrayList<Map> maps = new ArrayList<>();
    private ArrayList<Player> players = new ArrayList<>();
    private Map currentMap = null;
    private Master master = new Master();
    private IInterface theInterface = null;
    private boolean gameStarted = false;

    public GameController(String type) {
        if (type.equals("GUI")) {
            this.theInterface = new ServerGUI(this);
        } else if (type.equals("Console")) {
            this.theInterface = new UserIO(this);
            ((UserIO) this.theInterface).fromConsole();
        }
        WindowObserver a = new WindowObserver(theInterface);
        new WindowController(a);
        try {
            this.game = new Game();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.game.addObserver(this);
        this.loadMaps();
    }

    public void loadMaps() {
        maps.clear();
        currentMap = null;
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
                    this.parseJSON(file, name);
                } else {
                    System.out.println("not valid");
                }
            }
        }
        theInterface.refreshMaps();
    }

    public void parseJSON(File file, String name) {
        JSONParser parser = new JSONParser();
        try {
            FileReader reader = new FileReader(file);
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
            maps.add(map);
        } catch (Exception e) {
            System.out.println("Parse JSON: " + e.getMessage());
        }
    }

    public void newGame() {
        if (maps.size() > 0) {
            new Thread() {
                public void run() {
                    new CallbackTask(new Runnable() {
                        public void run() {
                            WindowController.addConsoleMsg("Connecting to the master server...");
                            if (master.initServer()) {
                                WindowController.addConsoleMsg("Connected to master server !");
                            }
                        }
                    }, new Callback() {
                        @Override
                        public void complete() {
                            currentMap = maps.get(theInterface.getSelectedMapIndex());
                            config = theInterface.getGameConfig();
                            config.setMap(currentMap);
                            GameController.this.game.setConfig(config);
                            GameController.this.game.start();
                        }
                    }).run();
                }
            }.start();
        } else {
            theInterface.tellNoMap();
        }
    }

    public ArrayList<Map> getMaps() {
        return this.maps;
    }

    public ArrayList<Player> getPlayers() {
        return this.players;
    }

    public void update(Observable o, Object arg) {
        if (arg instanceof String) {
            String msg = (String) arg;
            switch (msg) {
                case "start game":
                    new Thread() {
                        public void run() {
                            GameController.this.newGame();
                        }
                    }.start();
                    break;
                case "stop":
                    gameStarted = false;
                    theInterface.gameStopped();
                    master.stopServer();
                    break;
                case "stop game":
                    game.stop();
                    gameStarted = false;
                    theInterface.gameStopped();
                    master.stopServer();
                    break;
                case "reload maps":
                    loadMaps();
                    break;
                default:
                    WindowController.addConsoleMsg("Not handled: " + msg);
                    break;
            }
        } else if (o instanceof Game) {
            if (arg instanceof Boolean) {
                if ((Boolean) arg) {
                    if (!gameStarted) {
                        gameStarted = true;
                        WindowController.addConsoleMsg("Starting server...");
                    } else {
                        WindowController.addConsoleMsg("Restarting server...");
                    }
                    theInterface.gameStarted();
                } else {
                    gameStarted = false;
                    theInterface.gameStopped();
                    WindowController.addConsoleMsg("Can't start server because you did not fill all the fields correctly !");
                }
            } else if (arg instanceof MessageData) {
                MessageModel mm = ((MessageData) arg).getRequest();
                final Server server = ((MessageData) arg).getServer();
                final Connection connection = ((MessageData) arg).getConnection();
                if (mm instanceof MessageConnect) {
                    MessageConnect msg = (MessageConnect) mm;
                    System.out.println("J'envoie un message Connect");
                    try {
                        String encodedMap = MD5.getMD5Checksum(currentMap.getImgPath());
                        String encodedJson = MD5.getMD5Checksum(currentMap.getFilePath());
                        MessageConnect response = new MessageConnect(currentMap.getName(), currentMap.getFileNameNoExt(), encodedMap, encodedJson, new ArrayList<String>());
                        server.sendToTCP(connection.getID(), response);
                    } catch (Exception e) {
                        Log.error("MD5: " + e.getMessage());
                    }
                } else if (mm instanceof MessageDownload) {
                    MessageDownload response = new MessageDownload(currentMap.getFileName(), currentMap.getFileLength());
                    server.sendToTCP(connection.getID(), response);
                    new Thread("upload") {
                        public void run() {
                            try {
                                new SendFile(currentMap.getFilePath());
                                MessageDownload response = new MessageDownload(currentMap.getImgName(), currentMap.getImgLength());
                                server.sendToTCP(connection.getID(), response);
                                new SendFile(currentMap.getImgPath());
                            } catch (Exception e) {
                                System.out.println("Cannot send file: " + e.getMessage());
                            }
                        }
                    }.start();
                } else if (mm instanceof MessagePlayerNew) {
                    MessagePlayerNew msg = (MessagePlayerNew) mm;
                    Log.info("GameController a recu le nouveau joueur '" + msg.getPseudo() + "'");
                    players.add(new Player(msg.getId(), msg.getPseudo(), msg., connection));
                    theInterface.refreshPlayers();
                    master.addUser(msg.getPseudo());
                } else if (mm instanceof MessageDelete) {
                    MessageDelete msg = (MessageDelete) mm;
                    Log.info("GameController a remove un joueur.");
                    theInterface.refreshPlayers();
                    master.delUser(msg.getPseudo());
                }
            }
        }
    }
}
