package com.lefrantguillaume.gameComponent.controllers;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.minlog.Log;
import com.lefrantguillaume.WindowController;
import com.lefrantguillaume.gameComponent.gameMode.EnumAction;
import com.lefrantguillaume.gameComponent.gameMode.GameModeController;
import com.lefrantguillaume.gameComponent.gameobjects.player.Player;
import com.lefrantguillaume.gameComponent.gameobjects.tanks.tools.TankConfigData;
import com.lefrantguillaume.gameComponent.maps.Map;
import com.lefrantguillaume.gameComponent.maps.MapController;
import com.lefrantguillaume.gameComponent.target.Targets;
import com.lefrantguillaume.master.EnumTargetTask;
import com.lefrantguillaume.networkComponent.gameServerComponent.Request;
import com.lefrantguillaume.networkComponent.gameServerComponent.RequestFactory;
import com.lefrantguillaume.networkComponent.gameServerComponent.SendFile;
import com.lefrantguillaume.networkComponent.gameServerComponent.clientmsgs.*;
import com.lefrantguillaume.utils.MD5;
import com.lefrantguillaume.utils.ServerConfig;
import javafx.util.Pair;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.util.*;

/**
 * Created by Styve on 10/03/2015.
 */

public class GameController extends Observable {
    private MapController mapController;
    private GameModeController gameModeController;
    private Targets targets = null;
    private TankConfigData tankConfigData = null;
    private HashMap<String, HashMap<String, List<List<String>>>> collisions = new HashMap<>();
    /////////////// idShot /////// idTarget //////// idPlayer

    public GameController(JSONObject configFile) throws JSONException {
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
                this.doMessageConnect((MessageConnect) received, connection);
            } else if (received instanceof MessageDownload) {
                this.doMessageDownload(connection);
            } else if (received instanceof MessagePlayerNew) {
                this.doMessagePlayerNew((MessagePlayerNew) received, connection);
            } else if (received instanceof MessagePlayerDelete) {
                this.doMessagePlayerDelete((MessagePlayerDelete) received);
            } else if (received instanceof MessageDisconnect) {
                this.doMessageDisconnect(connection);
            } else if (received instanceof MessageCollision) {
                this.doMessageCollision((MessageCollision) received);
            } else if (received instanceof MessagePutObstacle) {
                this.doMessagePutObstacle((MessagePutObstacle) received);
            } else if (received instanceof MessagePlayerUpdatePosition) {
                this.doMessagePlayerUpdatePosition((MessagePlayerUpdatePosition) received);
            } else if (received instanceof MessageMove) {
                this.doMessageMove((MessageMove) received);
            } else if (received instanceof MessageChangeTeam) {
                this.doMessageChangeTeam((MessageChangeTeam) received);
            } else if (received instanceof MessageSpell) {
                this.doMessageSpell((MessageSpell) received);
            } else if (received instanceof MessageNeedMap) {
                System.out.println("Il a besoin de la map");
            } else if (received instanceof MessageShoot) {
                this.doMessageShoot((MessageShoot) received);
            }
        }
    }

    // DO MESSAGE
    public void doMessageConnect(MessageConnect received, Connection connection) {
        WindowController.addConsoleMsg("Nouvelle connection: " + received.getPseudo() + " avec l'id " + received.getId());
        try {
            String encodedMap = MD5.getMD5Checksum(this.mapController.getCurrentMap().getImgPath());
            String encodedJson = MD5.getMD5Checksum(this.mapController.getCurrentMap().getFilePath());
            MessageConnect response = new MessageConnect(this.mapController.getCurrentMap().getName(), this.mapController.getCurrentMap().getFileNameNoExt(), encodedMap, encodedJson, new ArrayList<String>());

            setChanged();
            notifyObservers(new Pair<>(EnumTargetTask.NETWORK, RequestFactory.createRequest(connection, response)));
        } catch (Exception e) {
            Log.error("MD5: " + e.getMessage());
        }
    }

    public void doMessageDownload(Connection connection) {
        MessageDownload response = new MessageDownload(this.mapController.getCurrentMap().getFileName(), this.mapController.getCurrentMap().getFileLength());
        setChanged();
        notifyObservers(new Pair<>(EnumTargetTask.NETWORK, RequestFactory.createRequest(connection, response)));
        new Thread("upload") {
            public void run() {
                try {
                    new SendFile(mapController.getCurrentMap().getFilePath());
                    MessageDownload response = new MessageDownload(mapController.getCurrentMap().getImgName(), mapController.getCurrentMap().getImgLength());

                    setChanged();
                    notifyObservers(new Pair<>(EnumTargetTask.NETWORK, RequestFactory.createRequest(connection, response)));
                    new SendFile(mapController.getCurrentMap().getImgPath());
                } catch (Exception e) {
                    System.out.println("Cannot send file: " + e.getMessage());
                }
            }
        }.start();
    }

    public void doMessagePlayerNew(MessagePlayerNew received, Connection connection) {
        System.out.println("Nouveau joueur: " + received.getPseudo() + " with :" + received.getEnumTanks().getValue());
        for (java.util.Map.Entry<String, Player> entry : this.targets.getPlayers().entrySet()) {
            MessagePlayerNew tmpMessage = new MessagePlayerNew();
            tmpMessage.setEnumTanks(entry.getValue().getTank().getTankState().getTankType());
            tmpMessage.setId(entry.getValue().getId());
            tmpMessage.setPseudo(entry.getValue().getPseudo());
            tmpMessage.setPosX(0);
            tmpMessage.setPosY(0);
            this.setChanged();
            this.notifyObservers(new Pair<>(EnumTargetTask.NETWORK, RequestFactory.createRequest(connection, tmpMessage)));
        }

        this.targets.addPlayer(received.getId(), new Player(received.getId(), received.getPseudo(), this.gameModeController.getCurrentMode().attributeATeam(), this.tankConfigData.getTank(received.getEnumTanks()), connection));
        Player player = this.targets.getPlayer(received.getId());
        this.gameModeController.getCurrentMode().changePlayerInTeam(player.getTeamId(), 1);
        Pair<Float, Float> newPositions = this.mapController.getCurrentMap().calcRespawnPoint(this.gameModeController.getCurrentGameMode(), this.gameModeController.getCurrentMode().getIndexTeam(player.getTeamId().toString()), player.getTank().getTankState().getCollisionObject());
        WindowController.addConsoleMsg("joueur team: " + this.gameModeController.getCurrentMode().getIndexTeam(player.getTeamId()));

        if (newPositions != null) {
            received.setPosX(newPositions.getKey());
            received.setPosY(newPositions.getValue());
            this.setChanged();
            this.notifyObservers(new Pair<>(EnumTargetTask.NETWORK, RequestFactory.createRequest(received)));
            this.setChanged();
            this.notifyObservers(new Pair<>(EnumTargetTask.MASTER_SERVER, received));
        }
    }

    public void doMessagePlayerDelete(MessagePlayerDelete received) {
        System.out.println(received.getPseudo() + " a envoyé un message DELETE");
        Player player = this.targets.getPlayer(received.getId());
        if (player != null) {
            this.gameModeController.getCurrentMode().changePlayerInTeam(player.getTeamId(), -1);
            this.targets.deletePlayer(received.getId());
        }
        this.setChanged();
        this.notifyObservers(new Pair<>(EnumTargetTask.NETWORK, RequestFactory.createRequest(received)));
    }

    public void doMessageDisconnect(Connection connection) {
        WindowController.addConsoleMsg("Disonnected: Client ID " + connection.getID());
        for (java.util.Map.Entry<String, Player> entry : this.targets.getPlayers().entrySet()) {
            if (entry.getValue().getConnection().getID() == connection.getID()) {
                this.setChanged();
                this.notifyObservers(new Pair<>(EnumTargetTask.NETWORK, RequestFactory.createRequest(new MessagePlayerDelete(entry.getKey(), entry.getValue().getPseudo()))));
                break;
            }
        }
    }

    public void doMessageCollision(final MessageCollision mc) {
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

    public void doMessagePutObstacle(MessagePutObstacle received) {
        if (!this.gameModeController.isPlayable())
            return;
        setChanged();
        notifyObservers(new Pair<>(EnumTargetTask.NETWORK, RequestFactory.createRequest(received)));
    }

    public void doMessagePlayerUpdatePosition(MessagePlayerUpdatePosition received) {
        if (!this.gameModeController.isPlayable())
            return;
        System.out.println("Update: " + received.getX() + " / " + received.getY());
        setChanged();
        notifyObservers(new Pair<>(EnumTargetTask.NETWORK, RequestFactory.createRequest(received)));
    }

    public void doMessageMove(MessageMove received) {
        if (!this.gameModeController.isPlayable())
            return;
        setChanged();
        notifyObservers(new Pair<>(EnumTargetTask.NETWORK, RequestFactory.createRequest(received)));
    }

    public void doMessageChangeTeam(MessageChangeTeam received) {
        setChanged();
        notifyObservers(new Pair<>(EnumTargetTask.NETWORK, RequestFactory.createRequest(received)));
    }

    public void doMessageSpell(MessageSpell received) {
        if (!this.gameModeController.isPlayable())
            return;
        setChanged();
        notifyObservers(new Pair<>(EnumTargetTask.NETWORK, RequestFactory.createRequest(received)));
    }

    public void doMessageShoot(MessageShoot message) {
        if (!this.gameModeController.isPlayable())
            return;
        WindowController.addConsoleMsg("nbShooter: " + this.targets.getPlayers().size());
        final Player player = this.targets.getPlayer(message.getId());
        if (player != null && player.isCanShoot()) {
            player.setCanShoot(false);
            message.setShootId(UUID.randomUUID().toString());
            WindowController.addConsoleMsg("new Shoot : " + message.getShotId());
            this.targets.addShot(message.getShotId(), player.getTank().getTankWeapon().generateShot(message.getShotId(), player.getId()));

            this.setChanged();
            this.notifyObservers(new Pair<>(EnumTargetTask.NETWORK, RequestFactory.createRequest(message)));

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

    // FUNCTIONS
    public void startGame() {
        WindowController.addConsoleMsg("Game started");
    }

    public void stopGame() {
        WindowController.addConsoleMsg("Game stopped");
    }

    public void newRound() {
        this.gameModeController.getCurrentMode().stop();
        this.mapController.getCurrentMap().resetCurrentObject();
        this.addNewRoundTimer();
        for (java.util.Map.Entry<String, Player> entry : this.targets.getPlayers().entrySet()) {
            MessageModel message;
            Pair<Float, Float> newPositions = this.mapController.getCurrentMap().calcRespawnPoint(this.gameModeController.getCurrentGameMode(), this.gameModeController.getCurrentMode().getIndexTeam(entry.getValue().getTeamId().toString()), entry.getValue().getTank().getTankState().getCollisionObject());
            if (newPositions != null) {
                MessagePlayerUpdatePosition tmp = new MessagePlayerUpdatePosition();
                tmp.setPseudo(entry.getValue().getPseudo());
                tmp.setId(entry.getValue().getId());
                tmp.setX(newPositions.getKey());
                tmp.setY(newPositions.getValue());
                message = new MessagePlayerUpdatePosition(tmp);
            } else {
                message = new MessagePlayerDelete(entry.getValue().getPseudo(), entry.getValue().getId());
            }
            setChanged();
            notifyObservers(new Pair<>(EnumTargetTask.NETWORK, RequestFactory.createRequest(message)));
        }
        this.mapController.getCurrentMap().resetCurrentObject();
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

    public void addMap(Map map) {
        this.mapController.addMap(map);
    }

    private void resultForCollision(String shotId, String targetId) {
        WindowController.addConsoleMsg("Shot : " + shotId + ", target : " + targetId);
        Player target = this.targets.getPlayer(targetId);
        Player killer = this.targets.getPlayer(this.targets.getShot(shotId).getPlayerId());
        if (target != null && killer != null) {
            if (!(ServerConfig.friendlyFire == false && target.getTeamId().equals(killer.getTeamId()))) {
                MessageModel message = targets.doCollision(shotId, targetId);
                if (message != null) {
                    this.setChanged();
                    this.notifyObservers(new Pair<>(EnumTargetTask.NETWORK, RequestFactory.createRequest(message)));
                    if (message instanceof MessagePlayerUpdateState) {
                        if (((MessagePlayerUpdateState) message).getCurrentLife() <= 0) {
                            target.addDeath();
                            killer.addKill();
                            this.gameModeController.doTask(new Pair<>(EnumAction.KILL, killer.getTeamId()));
                            if (this.gameModeController.isWinnerTeam() != null) {
                                MessageModel reviveTask = new MessagePlayerRevive(message.getPseudo(), message.getId(), 0, 0);
                                targets.getPlayer(message.getId()).revive();
                                setChanged();
                                notifyObservers(new Pair<>(EnumTargetTask.NETWORK, RequestFactory.createRequest(reviveTask)));
                                this.newRound();
                            } else {
                                this.addReviveTimer(message);
                            }
                        }
                    }
                }
            } else {
                WindowController.addConsoleMsg("message = null");
            }
        }
    }

    //TIMERS
    private void addCollisionTimer(final String it1, final String it2, final int it3) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                List<String> targeted = collisions.get(it1).get(it2).get(it3);
                int playerCount = targets.getPlayers().size();
                if (targeted.size() >= playerCount / 2) {
                    resultForCollision(it1, it2);
                }
            }
        }, 150);
    }

    private void addReviveTimer(final MessageModel values) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Player player = targets.getPlayer(values.getId());
                Pair<Float, Float> newPositions = mapController.getCurrentMap().calcRespawnPoint(gameModeController.getCurrentGameMode(), gameModeController.getCurrentMode().getIndexTeam(player.getTeamId().toString()), player.getTank().getTankState().getCollisionObject());
                if (newPositions != null) {
                    MessageModel message = new MessagePlayerRevive(player.getPseudo(), player.getId(), newPositions.getKey(), newPositions.getValue());
                    player.revive();
                    setChanged();
                    notifyObservers(new Pair<>(EnumTargetTask.NETWORK, RequestFactory.createRequest(message)));
                }
            }
        }, 3000);
    }

    private void addNewRoundTimer() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                gameModeController.getCurrentMode().start();
            }
        }, 7000);
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

    public Targets getTargets() {
        return this.targets;
    }
}