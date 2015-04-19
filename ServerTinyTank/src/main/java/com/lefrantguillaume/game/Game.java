package com.lefrantguillaume.game;

import com.esotericsoftware.minlog.Log;
import com.lefrantguillaume.WindowController;
import com.lefrantguillaume.game.gameobjects.player.Player;
import com.lefrantguillaume.network.TinyServer;
import com.lefrantguillaume.network.clientmsgs.MessageCollision;
import com.lefrantguillaume.network.clientmsgs.MessagePlayerNew;
import com.lefrantguillaume.network.clientmsgs.MessagePutObject;
import com.lefrantguillaume.network.msgdatas.*;
import com.lefrantguillaume.utils.GameConfig;

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
    private HashMap<String, Player> players = new HashMap<String, Player>();
    private HashMap<String, Player> targets = new HashMap<String, Player>();
    // private HashMap<String, Shot> shots = new HashMap<String, Shot>();
    private HashMap<String, HashMap<String, List<List<String>>>> collisions = new HashMap<String, HashMap<String, List<List<String>>>>();
    /////////////// idShot /////// idTarget //////// idPlayer

    public Game() {
        this.server = new TinyServer();
        this.server.addObserver(this);
        this.playable = false;
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
        for (Map.Entry<String, Player> entry : players.entrySet()) {
            if (entry.getValue().getPseudo().equals(pseudo)) {
                players.remove(entry.getValue().getId());
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
        for (Map.Entry<String, Player> entry : players.entrySet()) {
            Log.info("Joueur : " + entry.getValue().getPseudo());
            playerNames.add(entry.getValue().getPseudo());
        }
        Log.info("\n");
    }

    public void update(Observable o, Object arg) {
        if (arg instanceof MessageTankData) {
            MessageTankData mtd = ((MessageTankData) arg);
            mtd.getServer().sendToAllExceptTCP(mtd.getConnection().getID(), mtd.getRequest());
            players.put(((MessageTankData) arg).getRequest().getId(), new Player(mtd.getRequest().getId(), mtd.getRequest().getPseudo(), mtd.getRequest().getEnumTanks(), mtd.getConnection()));
            for (Map.Entry<String, Player> entry : players.entrySet()) {
                MessagePlayerNew a = ((MessageTankData) arg).getRequest();
                a.setEnumTanks(entry.getValue().getTank());
                a.setId(entry.getValue().getId());
                a.setPseudo(entry.getValue().getPseudo());
                mtd.getServer().sendToTCP(mtd.getConnection().getID(), a);
            }
            updatePlayerList();
        } else if (arg instanceof MessageDeleteData) {
            MessageDeleteData mtd = (MessageDeleteData) arg;
            players.remove(mtd.getRequest().getId());
            updatePlayerList();
        } else if (arg instanceof MessageShootRequestData) {
            if (!playable) return;
            MessageShootRequestData msd = ((MessageShootRequestData) arg);
            final Player player = players.get(msd.getRequest().getId());
            if (player.isCanShoot()) {
                player.setCanShoot(false);
                System.out.println("tir de " + msd.getRequest().getPseudo() + " / angle: " + msd.getRequest().getAngle());
                msd.getRequest().setShootId(UUID.randomUUID().toString());
                msd.getServer().sendToAllTCP(msd.getRequest());
                TimerTask tt = new TimerTask() {
                    @Override
                    public void run() {
                        player.setCanShoot(true);
                    }
                };
                Timer timer = new Timer();
                timer.schedule(tt, player.getAmmoCooldown());
            }
        } else if (arg instanceof MessageDisconnectData) {
            for (Map.Entry<String, Player> entry : players.entrySet()) {
                if (entry.getValue().getConnection().getID() == ((MessageDisconnectData) arg).getConnection().getID()) {
                    ((MessageDisconnectData) arg).setPseudo(entry.getValue().getPseudo());
                    ((MessageDisconnectData) arg).setPlayerId(entry.getValue().getId());
                    players.remove(entry.getKey());
                    updatePlayerList();
                    break;
                }
            }
        } else if (arg instanceof MessageCollisionData) {
            if (!playable) return;
            MessageCollision mc = ((MessageCollisionData) arg).getRequest();
            Log.info("Nouvelle collision (" + mc.getShotId() + ")");
            processCollision(mc);
        } else if (arg instanceof MessagePutObject) {
            MessagePutObject mpo = ((MessagePutObject) arg);
            server.getServer().sendToAllTCP(mpo);
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

        this.addTimer(it1, it2, it3);
    }

    private void addTimer(final String it1, final String it2, final int it3) {

        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                List<String> targeted = collisions.get(it1).get(it2).get(it3);
                int playerCount = players.size();

                if (targeted.size() >= playerCount / 2) {

                    gestCollision(it1, it2);

                }

            }
        }, 150);

    }

    private void gestCollision(String shotId, String targetId) {



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
        return players;
    }
}
