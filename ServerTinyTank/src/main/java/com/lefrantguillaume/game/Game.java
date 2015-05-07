package com.lefrantguillaume.game;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import com.lefrantguillaume.WindowController;
import com.lefrantguillaume.game.gameobjects.player.Player;
import com.lefrantguillaume.game.gameobjects.tanks.tools.TankConfigData;
import com.lefrantguillaume.network.TinyServer;
import com.lefrantguillaume.network.clientmsgs.*;
import com.lefrantguillaume.network.msgdatas.MessageData;
import com.lefrantguillaume.utils.GameConfig;
import org.codehaus.jettison.json.JSONObject;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.Map;

/**
 * Created by Styve on 10/03/2015.
 */

public class Game extends Observable implements Observer {
    private TinyServer server;
    private GameConfig config;
    private boolean playable;
    private List<String> playerNames = new ArrayList<String>();
    private Targets targets = null;
    private TankConfigData tankConfigData = null;
    private HashMap<String, HashMap<String, List<List<String>>>> collisions = new HashMap<String, HashMap<String, List<List<String>>>>();
    /////////////// idShot /////// idTarget //////// idPlayer

    public Game() throws Exception {
        this.server = new TinyServer();
        this.server.addObserver(this);
        this.playable = false;
        this.tankConfigData = new TankConfigData();
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
        this.tankConfigData.initTanks(new JSONObject(content));
        this.targets = new Targets();
    }

    public void start() {
        server.stop();
        boolean res = server.start();
        this.setChanged();
        this.notifyObservers(res);
        WindowController.addConsoleMsg("Game started");
        playable = true;
    }

    public void stop() {
        server.stop();
        this.setChanged();
        this.notifyObservers("stop");
    }

    public boolean kick(String pseudo) {
        int kicked = 0;
        for (Map.Entry<String, Player> entry : this.targets.getPlayers().entrySet()) {
            if (entry.getValue().getPseudo().equals(pseudo)) {
                this.targets.deletePlayer(entry.getValue().getId());
                entry.getValue().getConnection().close();
                updatePlayerList();
                kicked++;
            }
        }
        return kicked > 0;
    }


    public void updatePlayerList() {
        playerNames.clear();
        Log.info("J'efface la liste des joueurs.");
        for (Map.Entry<String, Player> entry : this.targets.getPlayers().entrySet()) {
            Log.info("Joueur : " + entry.getValue().getPseudo());
            playerNames.add(entry.getValue().getPseudo());
        }
        Log.info("\n");
    }

    public void update(Observable o, Object arg) {
        if (arg instanceof MessageData) {
            MessageModel mm = ((MessageData) arg).getRequest();
            Server theServer = ((MessageData) arg).getServer();
            Connection connection = ((MessageData) arg).getConnection();
            if (mm instanceof MessagePlayerNew) {
                MessagePlayerNew msg = ((MessagePlayerNew) mm);
                System.out.println("Nouveau joueur: " + msg.getPseudo() + " with :" + msg.getEnumTanks().getValue());
                msg.setPosX(50f);
                msg.setPosY(50f);
                theServer.sendToAllTCP(msg);

                WindowController.addConsoleMsg("nombre de joueurs: " + this.targets.getPlayers().size());
                for (Map.Entry<String, Player> entry : this.targets.getPlayers().entrySet()) {
                    MessagePlayerNew a = new MessagePlayerNew(msg);
                    a.setEnumTanks(entry.getValue().getTank().getTankState().getTankType());
                    a.setId(entry.getValue().getId());
                    a.setPseudo(entry.getValue().getPseudo());
                    a.setPosX(-50f);
                    a.setPosY(-50f);
                    theServer.sendToTCP(connection.getID(), a);
                }

                WindowController.addConsoleMsg("new Player: " + msg.getId());
                this.targets.addPlayer(msg.getId(), new Player(msg.getId(), msg.getPseudo(), this.tankConfigData.getTank(msg.getEnumTanks()), connection));
                updatePlayerList();
            } else if (mm instanceof MessageDelete) {
                MessageDelete msg = (MessageDelete) mm;
                System.out.println(msg.getPseudo() + " a envoyé un message DELETE");
                theServer.sendToAllTCP(msg);
                this.targets.deletePlayer(msg.getId());
                updatePlayerList();
            } else if (mm instanceof MessageShoot) {
                MessageShoot msg = ((MessageShoot) mm);
                if (!this.playable) return;
                WindowController.addConsoleMsg("nbShooter: " + this.targets.getPlayers().size());
                final Player player = this.targets.getPlayer(msg.getId());
                if (player != null && player.isCanShoot()) {
                    player.setCanShoot(false);
                    System.out.println("tir de " + msg.getPseudo() + " / angle: " + msg.getAngle());
                    msg.setShootId(UUID.randomUUID().toString());
                    WindowController.addConsoleMsg("new Shoot : " + msg.getShotId());
                    this.targets.addShot(msg.getShotId(), player.getTank().getTankWeapon().generateShot(msg.getShotId(), player.getId()));
                    theServer.sendToAllTCP(msg);

                    TimerTask tt = new TimerTask() {
                        @Override
                        public void run() {
                            player.setCanShoot(true);
                        }
                    };
                    Timer timer = new Timer();
                    timer.schedule(tt, player.getAmmoCooldown());
                }
            } else if (mm instanceof MessageDisconnect) {
                // msg useless
                //MessageDisconnect msg = (MessageDisconnect) mm;
                WindowController.addConsoleMsg("Disonnected: Client ID " + connection.getID());
                for (Map.Entry<String, Player> entry : this.targets.getPlayers().entrySet()) {
                    if (entry.getValue().getConnection().getID() == connection.getID()) {
                        //msg.setPseudo(entry.getValue().getPseudo());
                        //msg.setPlayerId(entry.getValue().getId());
                        this.targets.deletePlayer(entry.getKey());
                        updatePlayerList();
                        break;
                    }
                }
            } else if (mm instanceof MessageCollision) {
                MessageCollision msg = (MessageCollision) mm;
                if (!playable) return;
                Log.info("Nouvelle collision (" + msg.getShotId() + ")");
                processCollision(msg);
            } else if (mm instanceof MessagePutObstacle) {
                MessagePutObstacle msg = (MessagePutObstacle) mm;
                theServer.sendToAllTCP(msg);
            } else if (mm instanceof MessagePlayerUpdatePosition) {
                MessagePlayerUpdatePosition msg = (MessagePlayerUpdatePosition) mm;
                System.out.println("Update: " + msg.getX() + " / " + msg.getY());
                theServer.sendToAllExceptTCP(connection.getID(), msg);
            } else if (mm instanceof MessageMove) {
                MessageMove msg = (MessageMove) mm;
                System.out.println("direction recue: " + msg.getDirection() + " // move : " + (msg.getMove() ? "true" : "false"));
                theServer.sendToAllTCP(msg);
            } else if (mm instanceof MessageChangeTeam) {
                MessageChangeTeam msg = (MessageChangeTeam) mm;
                System.out.println(msg.getPseudo() + " change de team.");
                theServer.sendToAllTCP(msg);
            } else if (mm instanceof MessageSpell) {
                MessageSpell msg = (MessageSpell) mm;
                System.out.println("sort de " + msg.getPseudo() + ": " + msg.getX() + ", " + msg.getY());
                theServer.sendToAllTCP(msg);
            } else if (mm instanceof MessageNeedMap) {
                MessageNeedMap msg = (MessageNeedMap) mm;
                System.out.println("Il a besoin de la map");
            } else if (mm instanceof MessageConnect) {
                MessageConnect msg = (MessageConnect) mm;
                System.out.println("Nouvelle connection: " + msg.getPseudo() + " est sous l'id " + msg.getId());
            }
        }
        this.setChanged();
        this.notifyObservers(arg);
    }

    public void processCollision(final MessageCollision mc) {
        boolean added = false;

        String it1 = mc.getShotId();
        String it2 = mc.getTargetId();
        int it3 = -1;

        if (this.collisions.containsKey(mc.getShotId())) {
            HashMap<String, List<List<String>>> shootVal = collisions.get(mc.getShotId());
            if (shootVal.containsKey(mc.getTargetId())) {
                List<List<String>> targetVal = shootVal.get(mc.getTargetId());
                for (int i = 0; i > targetVal.size(); ++i) {
                    final List<String> listVal = targetVal.get(i);
                    if (!listVal.contains(mc.getId())) {
                        listVal.add(mc.getId());
                        added = true;
                        break;
                    }
                }
                if (!added) {
                    List<String> listVal = new ArrayList<String>();
                    listVal.add(mc.getId());
                    targetVal.add(listVal);

                    it3 = targetVal.size() - 1;

                }
            } else {
                List<List<String>> targetVal = new ArrayList<List<String>>();
                List<String> listVal = new ArrayList<String>();
                listVal.add(mc.getId());
                targetVal.add(listVal);
                shootVal.put(mc.getTargetId(), targetVal);

                it3 = 0;

            }
        } else {
            HashMap<String, List<List<String>>> shootVal = new HashMap<String, List<List<String>>>();
            List<List<String>> targetVal = new ArrayList<List<String>>();
            List<String> listVal = new ArrayList<String>();
            listVal.add(mc.getId());
            targetVal.add(listVal);
            shootVal.put(mc.getTargetId(), targetVal);
            this.collisions.put(mc.getShotId(), shootVal);

            it3 = 0;

        }

        if (!added)
            this.addCollisionTimer(it1, it2, it3);
    }

    private void addCollisionTimer(final String it1, final String it2, final int it3) {

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                List<String> targeted = collisions.get(it1).get(it2).get(it3);
                int playerCount = targets.getPlayers().size();
                if (targeted.size() >= playerCount / 2) {
                    gestCollision(it1, it2);
                }
            }
        }, 150);
    }

    private void addReviveTimer(final MessageModel values) {

        Timer timer = new Timer();
        this.setChanged();
        this.notifyObservers(values);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                MessageModel msg = new MessagePlayerRevive(values.getPseudo(), values.getId(), 100, 100);
                targets.getPlayer(values.getId()).revive();
                server.getServer().sendToAllTCP(msg);
            }
        }, 3000);
    }

    private void gestCollision(String shotId, String targetId) {

        WindowController.addConsoleMsg("Shot : " + shotId + ", target : " + targetId);
        MessageModel msg = targets.doCollision(shotId, targetId);

        if (msg != null) {
            WindowController.addConsoleMsg("nb connect :" + this.server.getServer().getConnections().length);
            if (((MessagePlayerUpdateState)msg).getCurrentLife() <= 0) {
                Player tmp = this.targets.getPlayer(targetId);
                if (tmp != null) {
                    tmp.addDeath();
                    this.targets.getPlayer(this.targets.getShot(shotId).getPlayerId()).addKill();
                    this.addReviveTimer(msg);
                }
            }
            this.server.getServer().sendToAllTCP(msg);
        } else {
            WindowController.addConsoleMsg("msg = null");
        }
    }

    public GameConfig getConfig() {
        return config;
    }

    public void setConfig(GameConfig config) {
        this.config = config;
    }

    public List<String> getPlayerNames() {
        return playerNames;
    }

    public HashMap<String, Player> getPlayers() {
        return this.targets.getPlayers();
    }
}
