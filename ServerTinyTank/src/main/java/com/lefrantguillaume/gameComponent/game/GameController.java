package com.lefrantguillaume.gameComponent.game;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.minlog.Log;
import com.lefrantguillaume.WindowController;
import com.lefrantguillaume.gameComponent.gameMode.GameModeController;
import com.lefrantguillaume.gameComponent.maps.MapController;
import com.lefrantguillaume.gameComponent.target.Targets;
import com.lefrantguillaume.gameComponent.gameobjects.player.Player;
import com.lefrantguillaume.gameComponent.gameobjects.tanks.tools.TankConfigData;
import com.lefrantguillaume.master.EnumController;
import com.lefrantguillaume.network.MessageData;
import com.lefrantguillaume.network.SendFile;
import com.lefrantguillaume.network.clientmsgs.*;
import com.lefrantguillaume.utils.MD5;
import javafx.util.Pair;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.util.*;
import java.util.Map;

/**
 * Created by Styve on 10/03/2015.
 */

public class GameController extends Observable {
    private boolean playable;
    private MapController mapController;
    private GameModeController gameModeController;
    private Targets targets = null;
    private TankConfigData tankConfigData = null;
    private HashMap<String, HashMap<String, List<List<String>>>> collisions = new HashMap<>();
    /////////////// idShot /////// idTarget //////// idPlayer

    public GameController(JSONObject configFile) throws JSONException {
        this.playable = false;
        this.tankConfigData = new TankConfigData();
        this.tankConfigData.initTanks(configFile);
        this.mapController = new MapController();
        this.targets = new Targets();
        this.gameModeController = new GameModeController();
        this.addObserver(this.gameModeController);
    }

    // FUNCTIONS
    public void doTask(Observable o, Object arg) {
        if (arg instanceof MessageData) {
            MessageModel received = ((MessageData) arg).getRequest();
            final Connection connection = ((MessageData) arg).getConnection();
            if (received instanceof MessageConnect) {
                MessageConnect msg = (MessageConnect) received;
                WindowController.addConsoleMsg("Nouvelle connection: " + msg.getPseudo() + " avec l'id " + msg.getId());
                try {
                    String encodedMap = MD5.getMD5Checksum(this.mapController.getCurrentMapIndex().getImgPath());
                    String encodedJson = MD5.getMD5Checksum(this.mapController.getCurrentMapIndex().getFilePath());
                    MessageConnect response = new MessageConnect(this.mapController.getCurrentMapIndex().getName(), this.mapController.getCurrentMapIndex().getFileNameNoExt(), encodedMap, encodedJson, new ArrayList<String>());

                    MessageData task = new MessageData(connection, response);
                    setChanged();
                    notifyObservers(new Pair<>(EnumController.NETWORK, task));
                } catch (Exception e) {
                    Log.error("MD5: " + e.getMessage());
                }
            } else if (received instanceof MessageDownload) {
                MessageDownload response = new MessageDownload(this.mapController.getCurrentMapIndex().getFileName(), this.mapController.getCurrentMapIndex().getFileLength());
                MessageData task = new MessageData(connection, response);
                setChanged();
                notifyObservers(new Pair<>(EnumController.NETWORK, task));
                new Thread("upload") {
                    public void run() {
                        try {
                            new SendFile(mapController.getCurrentMapIndex().getFilePath());
                            MessageDownload response = new MessageDownload(mapController.getCurrentMapIndex().getImgName(), mapController.getCurrentMapIndex().getImgLength());

                            MessageData task = new MessageData(connection, response);
                            setChanged();
                            notifyObservers(new Pair<>(EnumController.NETWORK, task));
                            new SendFile(mapController.getCurrentMapIndex().getImgPath());
                        } catch (Exception e) {
                            System.out.println("Cannot send file: " + e.getMessage());
                        }
                    }
                }.start();
            } else if (received instanceof MessagePlayerNew) {
                MessagePlayerNew msg = (MessagePlayerNew) received;
                System.out.println("Nouveau joueur: " + msg.getPseudo() + " with :" + msg.getEnumTanks().getValue());
                this.playerConnect(msg, connection);

                this.setChanged();
                this.notifyObservers(new Pair<>(EnumController.MASTER_SERVER, received));
            } else if (received instanceof MessageDelete) {
                MessageDelete msg = (MessageDelete) received;
                System.out.println(msg.getPseudo() + " a envoyé un message DELETE");
                this.playerDelete(msg);

                this.setChanged();
                this.notifyObservers(new Pair<>(EnumController.MASTER_SERVER, received));
            } else if (received instanceof MessageDisconnect) {
                WindowController.addConsoleMsg("Disonnected: Client ID " + connection.getID());
                this.playerDisconnect(connection);
            } else if (received instanceof MessageCollision) {
                MessageCollision msg = (MessageCollision) received;
                Log.info("Nouvelle collision (" + msg.getShotId() + ")");
                this.processCollision(msg);
            } else if (received instanceof MessagePutObstacle) {
                setChanged();
                notifyObservers(new Pair<>(EnumController.NETWORK, received));
            } else if (received instanceof MessagePlayerUpdatePosition) {
                MessagePlayerUpdatePosition msg = (MessagePlayerUpdatePosition) received;
                System.out.println("Update: " + msg.getX() + " / " + msg.getY());

                MessageData task = new MessageData(connection, received);
                setChanged();
                notifyObservers(new Pair<>(EnumController.NETWORK, task));
            } else if (received instanceof MessageMove) {
                MessageMove msg = (MessageMove) received;
                System.out.println("direction recue: " + msg.getDirection() + " // move : " + (msg.getMove() ? "true" : "false"));
                setChanged();
                notifyObservers(new Pair<>(EnumController.NETWORK, received));
                //server.sendToAllTCP(msg);
            } else if (received instanceof MessageChangeTeam) {
                MessageChangeTeam msg = (MessageChangeTeam) received;
                System.out.println(msg.getPseudo() + " change de team.");
                setChanged();
                notifyObservers(new Pair<>(EnumController.NETWORK, received));
            } else if (received instanceof MessageSpell) {
                MessageSpell msg = (MessageSpell) received;
                System.out.println("sort de " + msg.getPseudo() + ": " + msg.getX() + ", " + msg.getY());
                setChanged();
                notifyObservers(new Pair<>(EnumController.NETWORK, received));
            } else if (received instanceof MessageNeedMap) {
                MessageNeedMap msg = (MessageNeedMap) received;
                System.out.println("Il a besoin de la map");
            } else if (received instanceof MessageShoot) {
                MessageShoot msg = ((MessageShoot) received);
                this.playerShoot(msg);
            }
        }
    }

    public void onGameStart() {
        WindowController.addConsoleMsg("Game started");
        playable = true;
    }

    public void onGameStop() {
        WindowController.addConsoleMsg("Game stopped");
    }

    // TODO : à refaire
    public boolean kick(String pseudo) {
        int kicked = 0;
        for (Map.Entry<String, Player> entry : this.targets.getPlayers().entrySet()) {
            if (entry.getValue().getPseudo().equals(pseudo)) {
                this.targets.deletePlayer(entry.getValue().getId());
                entry.getValue().getConnection().close();
                kicked++;
            }
        }
        return kicked > 0;
    }

    public void playerConnect(MessagePlayerNew msg, Connection connection) {
        msg.setPosX(50f);
        msg.setPosY(50f);
        WindowController.addConsoleMsg("new Player: " + msg.getId());
        this.targets.addPlayer(msg.getId(), new Player(msg.getId(), msg.getPseudo(), this.tankConfigData.getTank(msg.getEnumTanks()), connection));
        WindowController.addConsoleMsg("nombre de joueurs: " + this.targets.getPlayers().size());
        for (Map.Entry<String, Player> entry : this.targets.getPlayers().entrySet()) {
            MessagePlayerNew message = new MessagePlayerNew(msg);
            message.setEnumTanks(entry.getValue().getTank().getTankState().getTankType());
            message.setId(entry.getValue().getId());
            message.setPseudo(entry.getValue().getPseudo());
            message.setPosX(-50f);
            message.setPosY(-50f);
            this.setChanged();
            this.notifyObservers(new Pair<>(EnumController.NETWORK, new MessageData(connection, message)));
        }
    }

    public void playerDisconnect(Connection connection) {
        for (Map.Entry<String, Player> entry : this.targets.getPlayers().entrySet()) {
            if (entry.getValue().getConnection().getID() == connection.getID()) {

                //TODO : pourquoi est ce qu'il y a un MessageDelete ? idem qu'un MessageDisconnect ?
                this.setChanged();
                this.notifyObservers(new Pair<>(EnumController.NETWORK, new MessageDelete(entry.getKey(), entry.getValue().getPseudo())));
                break;
            }
        }
    }

    public void playerDelete(MessageDelete msg) {
        this.targets.deletePlayer(msg.getId());
        this.setChanged();
        this.notifyObservers(new Pair<>(EnumController.NETWORK, msg));
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
            this.notifyObservers(new Pair<>(EnumController.NETWORK, msg));

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
        if (!added) {
            this.addCollisionTimer(it1, it2, it3);
        }
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
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                MessageModel msg = new MessagePlayerRevive(values.getPseudo(), values.getId(), 100, 100);
                targets.getPlayer(values.getId()).revive();
                setChanged();
                notifyObservers(new Pair<>(EnumController.NETWORK, msg));
            }
        }, 3000);
    }

    private void gestCollision(String shotId, String targetId) {

        WindowController.addConsoleMsg("Shot : " + shotId + ", target : " + targetId);
        MessageModel msg = targets.doCollision(shotId, targetId);

        if (msg != null) {
            //WindowController.addConsoleMsg("nb connect :" + this.server.getServer().getConnections().length);
            if (((MessagePlayerUpdateState) msg).getCurrentLife() <= 0) {
                Player tmp = this.targets.getPlayer(targetId);
                if (tmp != null) {
                    tmp.addDeath();
                    this.targets.getPlayer(this.targets.getShot(shotId).getPlayerId()).addKill();
                    this.addReviveTimer(msg);
                }
            }
            this.setChanged();
            this.notifyObservers(new Pair<>(EnumController.NETWORK, msg));
        } else {
            WindowController.addConsoleMsg("msg = null");
        }
    }

    // GETTERS
    public List<String> getPlayerNames() {
        return this.targets.getPlayersName();
    }

    public HashMap<String, Player> getPlayers() {
        return this.targets.getPlayers();
    }

    public MapController getMapController() {
        return this.mapController;
    }

}
