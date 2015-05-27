package com.lefrantguillaume.gameComponent.controler;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.minlog.Log;
import com.lefrantguillaume.WindowController;
import com.lefrantguillaume.gameComponent.gameMode.GameModeController;
import com.lefrantguillaume.gameComponent.gameobjects.player.Player;
import com.lefrantguillaume.gameComponent.gameobjects.tanks.tools.TankConfigData;
import com.lefrantguillaume.gameComponent.maps.Map;
import com.lefrantguillaume.gameComponent.maps.MapController;
import com.lefrantguillaume.gameComponent.target.Targets;
import com.lefrantguillaume.master.EnumController;
import com.lefrantguillaume.networkComponent.gameServer.Request;
import com.lefrantguillaume.networkComponent.gameServer.RequestFactory;
import com.lefrantguillaume.networkComponent.gameServer.SendFile;
import com.lefrantguillaume.networkComponent.gameServer.clientmsgs.*;
import com.lefrantguillaume.utils.MD5;
import javafx.util.Pair;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.util.*;

/**
 * Created by Styve on 10/03/2015.
 */

public class GameController extends Observable {
    private boolean playable;
    private MapController mapController;
    private GameModeController gameModeController;
    private GameTaskExecute gameTaskExecute;
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
    }

    // FUNCTIONS
    public void doTask(Observable o, Object arg) {
        if (arg instanceof Request) {
            MessageModel received = ((Request) arg).getRequest();
            final Connection connection = ((Request) arg).getConnection();
            if (received instanceof MessageConnect) {
                MessageConnect message = (MessageConnect) received;
                WindowController.addConsoleMsg("Nouvelle connection: " + message.getPseudo() + " avec l'id " + message.getId());
                try {
                    String encodedMap = MD5.getMD5Checksum(this.mapController.getCurrentMapIndex().getImgPath());
                    String encodedJson = MD5.getMD5Checksum(this.mapController.getCurrentMapIndex().getFilePath());
                    MessageConnect response = new MessageConnect(this.mapController.getCurrentMapIndex().getName(), this.mapController.getCurrentMapIndex().getFileNameNoExt(), encodedMap, encodedJson, new ArrayList<String>());

                    setChanged();
                    notifyObservers(new Pair<>(EnumController.NETWORK, RequestFactory.createRequest(connection, response)));
                } catch (Exception e) {
                    Log.error("MD5: " + e.getMessage());
                }
            } else if (received instanceof MessageDownload) {
                MessageDownload response = new MessageDownload(this.mapController.getCurrentMapIndex().getFileName(), this.mapController.getCurrentMapIndex().getFileLength());
                setChanged();
                notifyObservers(new Pair<>(EnumController.NETWORK, RequestFactory.createRequest(connection, response)));
                new Thread("upload") {
                    public void run() {
                        try {
                            new SendFile(mapController.getCurrentMapIndex().getFilePath());
                            MessageDownload response = new MessageDownload(mapController.getCurrentMapIndex().getImgName(), mapController.getCurrentMapIndex().getImgLength());

                            setChanged();
                            notifyObservers(new Pair<>(EnumController.NETWORK, RequestFactory.createRequest(connection, response)));
                            new SendFile(mapController.getCurrentMapIndex().getImgPath());
                        } catch (Exception e) {
                            System.out.println("Cannot send file: " + e.getMessage());
                        }
                    }
                }.start();
            } else if (received instanceof MessagePlayerNew) {
                MessagePlayerNew message = (MessagePlayerNew) received;
                System.out.println("Nouveau joueur: " + message.getPseudo() + " with :" + message.getEnumTanks().getValue());
                this.playerConnect(message, connection);

                this.setChanged();
                this.notifyObservers(new Pair<>(EnumController.MASTER_SERVER, received));
            } else if (received instanceof MessagePlayerDelete) {
                MessagePlayerDelete message = (MessagePlayerDelete) received;
                System.out.println(message.getPseudo() + " a envoyé un message DELETE");
                this.playerDelete(message);
            } else if (received instanceof MessageDisconnect) {
                WindowController.addConsoleMsg("Disonnected: Client ID " + connection.getID());
                this.playerDisconnect(connection);
            } else if (received instanceof MessageCollision) {
                MessageCollision message = (MessageCollision) received;
                Log.info("Nouvelle collision (" + message.getShotId() + ")");
                this.processCollision(message);
            } else if (received instanceof MessagePutObstacle) {
                setChanged();
                notifyObservers(new Pair<>(EnumController.NETWORK, RequestFactory.createRequest(received)));
            } else if (received instanceof MessagePlayerUpdatePosition) {
                MessagePlayerUpdatePosition message = (MessagePlayerUpdatePosition) received;
                System.out.println("Update: " + message.getX() + " / " + message.getY());
                setChanged();
                notifyObservers(new Pair<>(EnumController.NETWORK, RequestFactory.createRequest(received)));
            } else if (received instanceof MessageMove) {
                MessageMove message = (MessageMove) received;
                System.out.println("direction recue: " + message.getDirection() + " // move : " + (message.getMove() ? "true" : "false"));
                setChanged();
                notifyObservers(new Pair<>(EnumController.NETWORK, RequestFactory.createRequest(received)));
                //server.sendToAllTCP(message);
            } else if (received instanceof MessageChangeTeam) {
                MessageChangeTeam message = (MessageChangeTeam) received;
                System.out.println(message.getPseudo() + " change de team.");
                setChanged();
                notifyObservers(new Pair<>(EnumController.NETWORK, RequestFactory.createRequest(received)));
            } else if (received instanceof MessageSpell) {
                MessageSpell message = (MessageSpell) received;
                System.out.println("sort de " + message.getPseudo() + ": " + message.getX() + ", " + message.getY());
                setChanged();
                notifyObservers(new Pair<>(EnumController.NETWORK, RequestFactory.createRequest(received)));
            } else if (received instanceof MessageNeedMap) {
                MessageNeedMap message = (MessageNeedMap) received;
                System.out.println("Il a besoin de la map");
            } else if (received instanceof MessageShoot) {
                MessageShoot message = ((MessageShoot) received);
                this.playerShoot(message);
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
        for (java.util.Map.Entry<String, Player> entry : this.targets.getPlayers().entrySet()) {
            if (entry.getValue().getPseudo().equals(pseudo)) {
                this.targets.deletePlayer(entry.getValue().getId());
                entry.getValue().getConnection().close();
                kicked++;
            }
        }
        return kicked > 0;
    }

    public void playerConnect(MessagePlayerNew message, Connection connection) {
        WindowController.addConsoleMsg("nombre de joueurs: " + this.targets.getPlayers().size());
        for (java.util.Map.Entry<String, Player> entry : this.targets.getPlayers().entrySet()) {
            MessagePlayerNew tmpMessage = new MessagePlayerNew();
            tmpMessage.setEnumTanks(entry.getValue().getTank().getTankState().getTankType());
            tmpMessage.setId(entry.getValue().getId());
            tmpMessage.setPseudo(entry.getValue().getPseudo());
            tmpMessage.setPosX(50);
            tmpMessage.setPosY(50);
            this.setChanged();
            this.notifyObservers(new Pair<>(EnumController.NETWORK, RequestFactory.createRequest(connection, tmpMessage)));
        }
        WindowController.addConsoleMsg("new Player: " + message.getId());
        message.setPosX(50f);
        message.setPosY(50f);
        this.targets.addPlayer(message.getId(), new Player(message.getId(), message.getPseudo(), this.tankConfigData.getTank(message.getEnumTanks()), connection));
        this.setChanged();
        this.notifyObservers(new Pair<>(EnumController.NETWORK, RequestFactory.createRequest(message)));
    }

    public void playerDisconnect(Connection connection) {
        for (java.util.Map.Entry<String, Player> entry : this.targets.getPlayers().entrySet()) {
            if (entry.getValue().getConnection().getID() == connection.getID()) {

                //TODO : pourquoi est ce qu'il y a un MessageDelete ? idem qu'un MessageDisconnect ?
                this.setChanged();
                this.notifyObservers(new Pair<>(EnumController.NETWORK, RequestFactory.createRequest(new MessagePlayerDelete(entry.getKey(), entry.getValue().getPseudo()))));
                break;
            }
        }
    }

    public void playerDelete(MessagePlayerDelete message) {
        this.targets.deletePlayer(message.getId());
        this.setChanged();
        this.notifyObservers(new Pair<>(EnumController.NETWORK, RequestFactory.createRequest(message)));
    }

    public void playerShoot(MessageShoot message) {
        if (!this.playable) return;
        WindowController.addConsoleMsg("nbShooter: " + this.targets.getPlayers().size());
        final Player player = this.targets.getPlayer(message.getId());
        if (player != null && player.isCanShoot()) {
            player.setCanShoot(false);
            //System.out.println("tir de " + message.getPseudo() + " / angle: " + message.getAngle());
            message.setShootId(UUID.randomUUID().toString());
            WindowController.addConsoleMsg("new Shoot : " + message.getShotId());
            this.targets.addShot(message.getShotId(), player.getTank().getTankWeapon().generateShot(message.getShotId(), player.getId()));

            this.setChanged();
            this.notifyObservers(new Pair<>(EnumController.NETWORK, RequestFactory.createRequest(message)));

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
                MessageModel message = new MessagePlayerRevive(values.getPseudo(), values.getId(), 100, 100);
                targets.getPlayer(values.getId()).revive();
                setChanged();
                notifyObservers(new Pair<>(EnumController.NETWORK, RequestFactory.createRequest(message)));
            }
        }, 3000);
    }

    public void addMap(Map map) {
        this.mapController.addMap(map);
    }

    private void gestCollision(String shotId, String targetId) {
        WindowController.addConsoleMsg("Shot : " + shotId + ", target : " + targetId);
        MessageModel message = targets.doCollision(shotId, targetId);

        if (message != null) {
            if (((MessagePlayerUpdateState) message).getCurrentLife() <= 0) {
                Player tmp = this.targets.getPlayer(targetId);
                if (tmp != null) {
                    tmp.addDeath();
                    this.targets.getPlayer(this.targets.getShot(shotId).getPlayerId()).addKill();
                    this.addReviveTimer(message);
                }
            }
            this.setChanged();
            this.notifyObservers(new Pair<>(EnumController.NETWORK, RequestFactory.createRequest(message)));
        } else {
            WindowController.addConsoleMsg("message = null");
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
