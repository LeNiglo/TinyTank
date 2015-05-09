package com.lefrantguillaume.game;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import com.lefrantguillaume.WindowController;
import com.lefrantguillaume.game.gameobjects.player.Player;
import com.lefrantguillaume.game.gameobjects.tanks.tools.TankConfigData;
import com.lefrantguillaume.network.TinyServer;
import com.lefrantguillaume.network.clientmsgs.*;
import com.lefrantguillaume.network.MessageData;
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

public class Game extends Observable {
    private GameConfig config;
    private boolean playable;
    private List<String> playerNames = new ArrayList<String>();
    private Targets targets = null;
    private TankConfigData tankConfigData = null;
    private HashMap<String, HashMap<String, List<List<String>>>> collisions = new HashMap<String, HashMap<String, List<List<String>>>>();
    /////////////// idShot /////// idTarget //////// idPlayer

    public Game() throws Exception {
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

    public void onGameStart() {
        WindowController.addConsoleMsg("Game started");
        playable = true;
    }

    public void onGameStop() {
        WindowController.addConsoleMsg("Game stopped");
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
    }

    public void playerConnect(MessagePlayerNew msg, Connection connection) {
        msg.setPosX(50f);
        msg.setPosY(50f);
        this.setChanged();
        this.notifyObservers(msg);
        WindowController.addConsoleMsg("nombre de joueurs: " + this.targets.getPlayers().size());
        for (java.util.Map.Entry<String, Player> entry : this.targets.getPlayers().entrySet()) {
            MessagePlayerNew a = new MessagePlayerNew(msg);
            a.setEnumTanks(entry.getValue().getTank().getTankState().getTankType());
            a.setId(entry.getValue().getId());
            a.setPseudo(entry.getValue().getPseudo());
            a.setPosX(-50f);
            a.setPosY(-50f);
            this.setChanged();
            this.notifyObservers(new MessageData(connection, a));
        }

        WindowController.addConsoleMsg("new Player: " + msg.getId());
        this.targets.addPlayer(msg.getId(), new Player(msg.getId(), msg.getPseudo(), this.tankConfigData.getTank(msg.getEnumTanks()), connection));
        updatePlayerList();
    }

    public void playerDisconnect(Connection connection) {
        for (java.util.Map.Entry<String, Player> entry : this.targets.getPlayers().entrySet()) {
            if (entry.getValue().getConnection().getID() == connection.getID()) {
                this.targets.deletePlayer(entry.getKey());
                updatePlayerList();
                this.setChanged();
                this.notifyObservers(new MessageDelete(entry.getKey(), entry.getValue().getPseudo()));
                break;
            }
        }
    }

    public void playerDelete(MessageDelete msg) {
        this.targets.deletePlayer(msg.getId());
        updatePlayerList();
        this.setChanged();
        this.notifyObservers(msg);
    }

    public void playerShoot(MessageShoot msg) {
        if (!this.playable) return;
        WindowController.addConsoleMsg("nbShooter: " + this.targets.getPlayers().size());
        final Player player = this.targets.getPlayer(msg.getId());
        if (player != null && player.isCanShoot()) {
            player.setCanShoot(false);
            //System.out.println("tir de " + msg.getPseudo() + " / angle: " + msg.getAngle());
            msg.setShootId(UUID.randomUUID().toString());
            WindowController.addConsoleMsg("new Shoot : " + msg.getShotId());
            this.targets.addShot(msg.getShotId(), player.getTank().getTankWeapon().generateShot(msg.getShotId(), player.getId()));

            this.setChanged();
            this.notifyObservers(msg);

            TimerTask tt = new TimerTask() {
                @Override
                public void run() {
                    player.setCanShoot(true);
                }
            };
            Timer timer = new Timer();
            timer.schedule(tt, player.getAmmoCooldown());
        }
    }

    public void processCollision(final MessageCollision mc) {
        if (!playable) return;
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
        // Pourquoi ça ici ?
        //this.setChanged();
        //this.notifyObservers(values);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                MessageModel msg = new MessagePlayerRevive(values.getPseudo(), values.getId(), 100, 100);
                targets.getPlayer(values.getId()).revive();
                //server.getServer().sendToAllTCP(msg);
                Game.this.setChanged();
                Game.this.notifyObservers(msg);
            }
        }, 3000);
    }

    private void gestCollision(String shotId, String targetId) {

        WindowController.addConsoleMsg("Shot : " + shotId + ", target : " + targetId);
        MessageModel msg = targets.doCollision(shotId, targetId);

        if (msg != null) {
            //WindowController.addConsoleMsg("nb connect :" + this.server.getServer().getConnections().length);
            if (((MessagePlayerUpdateState)msg).getCurrentLife() <= 0) {
                Player tmp = this.targets.getPlayer(targetId);
                if (tmp != null) {
                    tmp.addDeath();
                    this.targets.getPlayer(this.targets.getShot(shotId).getPlayerId()).addKill();
                    this.addReviveTimer(msg);
                }
            }
            //this.server.getServer().sendToAllTCP(msg);
            this.setChanged();
            this.notifyObservers(msg);
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
