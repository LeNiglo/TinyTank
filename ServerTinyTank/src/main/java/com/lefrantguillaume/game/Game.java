package com.lefrantguillaume.game;

import com.esotericsoftware.minlog.Log;
import com.lefrantguillaume.WindowController;
import com.lefrantguillaume.network.*;
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

    public List<String> getPlayerNames() {return playerNames;}
    public HashMap<String, Player> getPlayers() { return players;}

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
                Network.MessagePlayerNew a = ((MessageTankData) arg).getRequest();
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
        }
        this.setChanged();
        this.notifyObservers(arg);
    }

    public GameConfig getConfig() {return config;}
    public void setConfig(GameConfig config) {this.config = config;}
}
