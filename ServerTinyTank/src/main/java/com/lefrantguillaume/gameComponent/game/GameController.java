package com.lefrantguillaume.gameComponent.game;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import com.lefrantguillaume.WindowController;
import com.lefrantguillaume.WindowObserver;
import com.lefrantguillaume.gameComponent.gameobjects.player.Player;
import com.lefrantguillaume.gameComponent.maps.Map;
import com.lefrantguillaume.gameComponent.maps.MapController;
import com.lefrantguillaume.network.MessageData;
import com.lefrantguillaume.network.SendFile;
import com.lefrantguillaume.network.TinyServer;
import com.lefrantguillaume.network.clientmsgs.*;
import com.lefrantguillaume.network.master.MasterServer;
import com.lefrantguillaume.userInterface.UserInterface;
import com.lefrantguillaume.userInterface.GraphicalUserInterface;
import com.lefrantguillaume.userInterface.ConsoleUserInterface;
import com.lefrantguillaume.utils.Callback;
import com.lefrantguillaume.utils.CallbackTask;
import com.lefrantguillaume.utils.MD5;
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
public class GameController extends Observable implements Observer{
    private MasterServer masterServer;
    private MapController mapController;
    private Game game = null;
    private TinyServer server;
    private UserInterface userInterface = null;
    private boolean gameStarted = false;

    public GameController(String type) throws JSONException {
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
        this.game = new Game(new JSONObject(content));
        this.masterServer = new MasterServer();
        this.mapController = new MapController();
        this.game.addObserver(this);
        this.server = new TinyServer();
        this.server.addObserver(this);
        this.loadMaps();
    }

    public void initInterface(String type){
        if (type.equals("GUI")) {
            this.userInterface = new GraphicalUserInterface(this);
        } else if (type.equals("Console")) {
            this.userInterface = new ConsoleUserInterface(GameController.this);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ((ConsoleUserInterface) GameController.this.userInterface).fromConsole();
                }
            }).start();
        }
        WindowObserver a = new WindowObserver(userInterface);
        new WindowController(a);


    }
    public void loadMaps() {
        this.mapController.clearMaps();
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
            this.mapController.addMap(map);
        } catch (Exception e) {
            System.out.println("Parse JSON: " + e.getMessage());
        }
    }

    public void newGame() {
        if (mapController.getMaps().size() > 0) {
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
                            mapController.setCurrentMapIndex(userInterface.getSelectedMapIndex());
                            //config = theInterface.getGameConfig();
                            //config.setMap(currentMap);
                            if (GameController.this.server.start()) {
                                GameController.this.game.onGameStart();
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
        game.onGameStop();
    }

    public List<Map> getMaps() {
        return this.mapController.getMaps();
    }

    public HashMap<String, Player> getPlayers() {
        return this.game.getPlayers();
    }

    public void update(Observable o, Object arg) {
        if (o instanceof UserInterface) {
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
        } else if (o instanceof Game) {
            if (arg instanceof MessageModel) {
                server.getServer().sendToAllTCP(arg);
            } else if (arg instanceof MessageData) {
                Connection connection = ((MessageData) arg).getConnection();
                server.getServer().sendToTCP(connection.getID(), ((MessageData) arg).getRequest());
            }
        } else if (o instanceof TinyServer) {
            if (arg instanceof MessageData) {
                MessageModel mm = ((MessageData) arg).getRequest();
                final Server server = this.server.getServer();
                final Connection connection = ((MessageData) arg).getConnection();
                if (mm instanceof MessageConnect) {
                    MessageConnect msg = (MessageConnect) mm;
                    WindowController.addConsoleMsg("Nouvelle connection: " + msg.getPseudo() + " est sous l'id " + msg.getId());
                    try {
                        String encodedMap = MD5.getMD5Checksum(this.mapController.getCurrentMapIndex().getImgPath());
                        String encodedJson = MD5.getMD5Checksum(this.mapController.getCurrentMapIndex().getFilePath());
                        MessageConnect response = new MessageConnect(this.mapController.getCurrentMapIndex().getName(), this.mapController.getCurrentMapIndex().getFileNameNoExt(), encodedMap, encodedJson, new ArrayList<String>());
                        server.sendToTCP(connection.getID(), response);
                    } catch (Exception e) {
                        Log.error("MD5: " + e.getMessage());
                    }
                } else if (mm instanceof MessageDownload) {
                    MessageDownload response = new MessageDownload(this.mapController.getCurrentMapIndex().getFileName(), this.mapController.getCurrentMapIndex().getFileLength());
                    server.sendToTCP(connection.getID(), response);
                    new Thread("upload") {
                        public void run() {
                            try {
                                new SendFile(mapController.getCurrentMapIndex().getFilePath());
                                MessageDownload response = new MessageDownload(mapController.getCurrentMapIndex().getImgName(), mapController.getCurrentMapIndex().getImgLength());
                                server.sendToTCP(connection.getID(), response);
                                new SendFile(mapController.getCurrentMapIndex().getImgPath());
                            } catch (Exception e) {
                                System.out.println("Cannot send file: " + e.getMessage());
                            }
                        }
                    }.start();
                } else if (mm instanceof MessagePlayerNew) {
                    MessagePlayerNew msg = (MessagePlayerNew) mm;
                    System.out.println("Nouveau joueur: " + msg.getPseudo() + " with :" + msg.getEnumTanks().getValue());
                    game.playerConnect(msg, connection);
                    userInterface.refreshPlayers();
                    masterServer.addUser(msg.getPseudo());
                } else if (mm instanceof MessageDelete) {
                    MessageDelete msg = (MessageDelete) mm;
                    System.out.println(msg.getPseudo() + " a envoyé un message DELETE");
                    game.playerDelete(msg);
                    userInterface.refreshPlayers();
                    masterServer.delUser(msg.getPseudo());
                } else if (mm instanceof MessageDisconnect) {
                    WindowController.addConsoleMsg("Disonnected: Client ID " + connection.getID());
                    game.playerDisconnect(connection);
                    userInterface.refreshPlayers();
                } else if (mm instanceof MessageCollision) {
                    MessageCollision msg = (MessageCollision) mm;
                    Log.info("Nouvelle collision (" + msg.getShotId() + ")");
                    game.processCollision(msg);
                } else if (mm instanceof MessagePutObstacle) {
                    MessagePutObstacle msg = (MessagePutObstacle) mm;
                    server.sendToAllTCP(msg);
                } else if (mm instanceof MessagePlayerUpdatePosition) {
                    MessagePlayerUpdatePosition msg = (MessagePlayerUpdatePosition) mm;
                    System.out.println("Update: " + msg.getX() + " / " + msg.getY());
                    server.sendToAllExceptTCP(connection.getID(), msg);
                } else if (mm instanceof MessageMove) {
                    MessageMove msg = (MessageMove) mm;
                    System.out.println("direction recue: " + msg.getDirection() + " // move : " + (msg.getMove() ? "true" : "false"));
                    server.sendToAllTCP(msg);
                } else if (mm instanceof MessageChangeTeam) {
                    MessageChangeTeam msg = (MessageChangeTeam) mm;
                    System.out.println(msg.getPseudo() + " change de team.");
                    server.sendToAllTCP(msg);
                } else if (mm instanceof MessageSpell) {
                    MessageSpell msg = (MessageSpell) mm;
                    System.out.println("sort de " + msg.getPseudo() + ": " + msg.getX() + ", " + msg.getY());
                    server.sendToAllTCP(msg);
                } else if (mm instanceof MessageNeedMap) {
                    MessageNeedMap msg = (MessageNeedMap) mm;
                    System.out.println("Il a besoin de la map");
                } else if (mm instanceof MessageShoot) {
                    MessageShoot msg = ((MessageShoot) mm);
                    game.playerShoot(msg);
                }
            }
        }
    }
}

/* NEW update(o, arg){
    Pair<EnumMaster, Object> task = (Pair...)arg;

    if (task.getKey().equals(EnumMaster.GAME){
        this.game.update(o, task.getValue());
    }
    else if (task.getKey().equals(EnumMaster.NETWORK){
        this.masterServer.update(o, task.getValue());
    }
    else if (task.getKey().equals(EnumMaster.USER_INTERFACE){
        this.userInterface.update(o, task.getValue());
    }

}
*/
