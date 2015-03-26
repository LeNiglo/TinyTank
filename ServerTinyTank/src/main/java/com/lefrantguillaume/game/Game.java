package com.lefrantguillaume.game;

import com.esotericsoftware.minlog.Log;
import com.lefrantguillaume.WindowController;
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
    private List<String> playerNames = new ArrayList<String>();
    private HashMap<String, Player> players = new HashMap<String, Player>();
    private HashMap<String, Shot> shots = new HashMap<String, Shot>();
    private HashMap<String, MessageCollision> collisions = new HashMap<String, MessageCollision>();

    public Game() {
        this.server = new TinyServer();
        this.server.addObserver(this);
    }

    public void start() {
        server.stop();
        boolean res = server.start();
        this.setChanged();
        this.notifyObservers(res);
        WindowController.addConsoleMsg("Game started");
    }

    public void stop() {
        server.stop();
        this.setChanged();
        this.notifyObservers("stop");
    }

    public boolean kick(String pseudo) {
        int kicked = 0;
        for (Map.Entry<String, Player> entry : players.entrySet()){
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
        for (Map.Entry<String, Player> entry : players.entrySet()){
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
            MessageShootRequestData msd = ((MessageShootRequestData) arg);
            Shot lastShot = getLastShot(EnumAttack.BASIC, msd.getRequest().getId());
            if (lastShot == null || System.currentTimeMillis() - lastShot.getTimestamp() > 500) {
                msd.getRequest().setShootId(UUID.randomUUID().toString());
                shots.put(msd.getRequest().getShotId(), new Shot(msd.getRequest().getShotId(), msd.getRequest().getId()));
                msd.getServer().sendToAllTCP(msd.getRequest());
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
            MessageCollision mc = ((MessageCollisionData)arg).getRequest();
            Log.info("Nouvelle collision (" + mc.getPosX() + ", " + mc.getPosY() + "):" + mc.getShotId());
            collisions.put(mc.getShotId(), mc);
        } else if (arg instanceof MessagePutObject) {
            MessagePutObject mpo = ((MessagePutObject) arg);
            server.getServer().sendToAllTCP(mpo);
        }
        this.setChanged();
        this.notifyObservers(arg);
    }

    public Shot getLastShot(EnumAttack type, String playerId) {
        Shot shot = null;
        switch (type) {
            case BASIC:
                ArrayList<String> keys = new ArrayList<String>(shots.keySet());
                for (int i = shots.size() - 1; i >= 0; i--) {
                    Shot theShot = shots.get(keys.get(i));
                    if (theShot.getPlayerId().equals(playerId)) {
                        shot = theShot;
                        break;
                    }
                }
                break;
            case SPELL:
                break;
            default:
                break;
        }
        return shot;
    }

    public GameConfig getConfig() {return config;}
    public void setConfig(GameConfig config) {this.config = config;}
    public List<String> getPlayerNames() {return playerNames;}
    public HashMap<String, Player> getPlayers() { return players;}
}
