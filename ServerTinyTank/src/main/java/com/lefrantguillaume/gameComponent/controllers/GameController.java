package com.lefrantguillaume.gameComponent.controllers;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.minlog.Log;
import com.lefrantguillaume.WindowController;
import com.lefrantguillaume.gameComponent.EnumCollision;
import com.lefrantguillaume.gameComponent.EnumGameObject;
import com.lefrantguillaume.gameComponent.gameMode.GameModeController;
import com.lefrantguillaume.gameComponent.gameMode.Team;
import com.lefrantguillaume.gameComponent.gameobjects.obstacles.Obstacle;
import com.lefrantguillaume.gameComponent.gameobjects.obstacles.ObstacleConfigData;
import com.lefrantguillaume.gameComponent.gameobjects.player.Player;
import com.lefrantguillaume.gameComponent.gameobjects.shots.Shot;
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
import com.lefrantguillaume.utils.StringTools;
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

    private ObstacleConfigData obstacleConfigData;
    private TankConfigData tankConfigData;

    private Targets targets;

    private boolean onNewRound;
    private HashMap<String, HashMap<String, List<List<String>>>> collisions;
    /////////////// idShot /////// idTarget //////// idPlayer

    public GameController(JSONObject configFile) throws JSONException {
        this.tankConfigData = new TankConfigData();
        this.tankConfigData.initTanks(configFile);
        this.mapController = new MapController();
        this.targets = new Targets();
        this.obstacleConfigData = new ObstacleConfigData(new JSONObject(StringTools.readFile("obstacles.json")));
        this.gameModeController = new GameModeController(this.obstacleConfigData);
        this.collisions = new HashMap<>();
        this.onNewRound = false;
        // à mettre lorsqu'on charge une game via l'interface
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
            } else if (received instanceof MessagePlayerObserverNew) {
                this.doMessagePlayerObserver((MessagePlayerObserverNew) received, connection, true);
            } else if (received instanceof MessagePlayerDelete) {
                this.doMessagePlayerDelete((MessagePlayerDelete) received);
            } else if (received instanceof MessagePlayerObserverDelete) {
                this.doMessagePlayerObserverDelete((MessagePlayerObserverDelete) received);
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
            } else if (received instanceof MessageChat) {
                this.doMessageChat((MessageChat) received);
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
            e.printStackTrace();
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
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void doMessagePlayerNew(MessagePlayerNew received, Connection connection) {
        WindowController.addConsoleMsg("Nouveau joueur: " + received.getId() + " with :" + received.getEnumTanks().getValue());

        String teamId = this.gameModeController.getCurrentMode().attributeATeam();
        if (teamId != null) {
            this.sendAllTargetsToSomeone(connection, true, true, false);
            this.targets.addPlayer(new Player(received.getId(), received.getPseudo(), teamId,
                    this.tankConfigData.getTank(received.getEnumTanks()), connection));
            Player player = this.targets.getPlayer(received.getId());
            this.gameModeController.getCurrentMode().changePlayerInTeam(player.getTeamId(), 1);
            Pair<Float, Float> newPositions = this.mapController.getCurrentMap().calcRespawnPoint(this.gameModeController.getCurrentGameMode(),
                    this.gameModeController.getCurrentMode().getIndexTeam(player.getTeamId()), player.getTank().getTankState().getCollisionObject(), false);
            WindowController.addConsoleMsg("joueur team: " + this.gameModeController.getCurrentMode().getIndexTeam(player.getTeamId()) + " at position: " + newPositions);

            if (newPositions != null) {
                received.setPosX(newPositions.getKey());
                received.setPosY(newPositions.getValue());
                received.setTeamId(player.getTeamId());
                this.setChanged();
                this.notifyObservers(new Pair<>(EnumTargetTask.NETWORK, RequestFactory.createRequest(received)));
                this.setChanged();
                this.notifyObservers(new Pair<>(EnumTargetTask.MASTER_SERVER, received));
            }
            this.sendAllTargetsToSomeone(connection, false, false, true);
        } else {
            received.setTeamId(this.gameModeController.getCurrentMode().getTeam(0));
            this.doMessagePlayerObserver(received, connection);
        }
    }

    public void doMessagePlayerObserver(MessagePlayerObserverNew received, Connection connection, boolean createObserver) {
        WindowController.addConsoleMsg("ADD OBSERVER");

        if (createObserver == true) {
            this.targets.addObserverPeople(new Pair<>(received, connection));
        }
        this.setChanged();
        this.notifyObservers(new Pair<>(EnumTargetTask.NETWORK, RequestFactory.createRequest(received)));
        this.sendAllTargetsToSomeone(connection, true, true, true);
    }

    public void doMessagePlayerObserver(MessagePlayerNew received, Connection connection) {
        this.targets.addWaitingPeople(new Pair<>(received, connection));
        this.doMessagePlayerObserver(new MessagePlayerObserverNew(received.getPseudo(), received.getId(), this.gameModeController.getCurrentMode().getTeam(0)), connection, false);
    }

    public void doMessagePlayerObserverDelete(MessagePlayerObserverDelete received){

        if (this.targets.deleteObserverPlayer(received.getId()) != null){
            this.targets.deleteWaitingPlayer(received.getId());
        }
        this.setChanged();
        this.notifyObservers(new Pair<>(EnumTargetTask.NETWORK, RequestFactory.createRequest(received)));
    }

    public void connectAWaitingPlayer(){
        Pair<MessagePlayerNew, Connection> message = this.targets.getFirstWaitingPlayer();

        if (message != null){
            this.doMessagePlayerNew(message.getKey(), message.getValue());
        }
    }

    public void doMessagePlayerDelete(MessagePlayerDelete received) {
        System.out.println(received.getPseudo() + " a envoyé un message DELETE");
        Player player = this.targets.getPlayer(received.getId());
        if (player != null) {
            this.gameModeController.getCurrentMode().changePlayerInTeam(player.getTeamId(), -1);
            if (player.isTransportObjective()) {
                MessagePutObstacle message = this.targets.addObstacle(player.getTransportObjective());
                this.setChanged();
                this.notifyObservers(new Pair<>(EnumTargetTask.NETWORK, RequestFactory.createRequest(message)));
                player.setTransportObjective(null);
            }
            this.targets.deletePlayer(received.getId());
            this.setChanged();
            this.notifyObservers(new Pair<>(EnumTargetTask.MASTER_SERVER, received));
            this.connectAWaitingPlayer();
        }
        this.setChanged();
        this.notifyObservers(new Pair<>(EnumTargetTask.NETWORK, RequestFactory.createRequest(received)));
    }

    public void doMessageDisconnect(Connection connection) {
        WindowController.addConsoleMsg("Disonnected: Client ID " + connection.getID());
        for (java.util.Map.Entry<String, Player> entry : this.targets.getPlayers().entrySet()) {
            if (entry.getValue().getConnection().getID() == connection.getID()) {
                this.doMessagePlayerDelete(new MessagePlayerDelete(entry.getKey(), entry.getValue().getPseudo()));
            }
        }
    }

    public void doMessageCollision(final MessageCollision mc) {
        boolean added = false;

        String it1 = mc.getHitterId();
        String it2 = mc.getTargetId();
        EnumCollision type = mc.getType();

        int it3 = -1;

        if (this.collisions.containsKey(mc.getHitterId())) {
            HashMap<String, List<List<String>>> shootVal = collisions.get(mc.getHitterId());
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
            this.collisions.put(mc.getHitterId(), shootVal);
            it3 = 0;
        }
        if (!added) {
            this.addCollisionTimer(it1, it2, it3, type);
        }
    }

    public void doMessagePlayerUpdatePosition(MessagePlayerUpdatePosition received) {
        if (!this.gameModeController.isPlayable() || this.targets == null || this.targets.getPlayers() == null)
            return;
        System.out.println("Update: " + received.getX() + " / " + received.getY());
        this.sendToAllExceptMe(received);
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
        received.setSpellId(UUID.randomUUID().toString());
        if (received.getType() == EnumGameObject.SHIELD) {
            Obstacle obstacle = this.obstacleConfigData.getObstacle(received.getType());
            obstacle.createObstacle(received.getId(), received.getPseudo(), received.getSpellId(), received.getAngle(), received.getPosX(), received.getPosY());
            WindowController.addConsoleMsg("create SpellObstacle " + received.getType() + "with playerId:" + received.getId() + " idBox: " + received.getSpellId());
            this.targets.addObstacle(obstacle);
        }
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
            this.targets.addShot(player.getTank().getTankWeapon().generateShot(message.getShotId(), player.getId()));
            player.addShoot();
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

    public void doMessagePutObstacle(MessagePutObstacle received) {
        if (!this.gameModeController.isPlayable())
            return;
        received.setObstacleId(UUID.randomUUID().toString());
        Obstacle obstacle = this.obstacleConfigData.getObstacle(received.getType());
        obstacle.createObstacle(received.getId(), received.getPseudo(), received.getObstacleId(), received.getAngle(), received.getPosX(), received.getPosY());
        WindowController.addConsoleMsg("create Box " + received.getType() + "with playerId:" + received.getId() + " idBox: " + received.getObstacleId());
        this.targets.addObstacle(obstacle);
        setChanged();
        notifyObservers(new Pair<>(EnumTargetTask.NETWORK, RequestFactory.createRequest(received)));
    }

    public void doMessageChat(MessageChat received) {
        setChanged();
        notifyObservers(new Pair<>(EnumTargetTask.NETWORK, RequestFactory.createRequest(received)));
    }

    // FUNCTIONS
    public void startGame() {
        WindowController.addConsoleMsg("Game started");
        this.targets.initGame(this.mapController, this.gameModeController.getCurrentMode().getObstacles());
        WindowController.addConsoleMsg("nombre d'objet: \n\tobstacle: " + this.targets.getObstacles().size() + "\n\tplayer:" + this.targets.getPlayers().size() + "\n\tshots: " + this.targets.getShots().size());
    }

    public void stopGame() {
        WindowController.addConsoleMsg("Game stopped");
        this.kickAll();
        this.targets.clearAll();
    }

    public void kickAll() {
        for (java.util.Map.Entry<String, Player> entry : this.targets.getPlayers().entrySet()) {
            System.out.println("Plyer to be kicked: " + entry.getValue().getPseudo());
            MessagePlayerDelete message = this.targets.deletePlayer(entry.getKey());
            if (message != null) {
                this.sendToAllExceptMe(message);
            }
        }
    }

    public void clearTargets() {
        HashMap<String, Shot> tmpShots = new HashMap<>();
        tmpShots.putAll(this.targets.getShots());
        HashMap<String, Obstacle> tmpObstacles = new HashMap<>();
        tmpObstacles.putAll(this.targets.getObstacles());

        for (java.util.Map.Entry entry : tmpShots.entrySet()) {
            Player player = this.targets.getPlayer(((Shot) entry.getValue()).getPlayerId());
            if (player != null) {
                MessageModel message = new MessageShotUpdateState(player.getPseudo(), player.getId(), (String) entry.getKey(), 0);
                if (message != null) {
                    this.setChanged();
                    this.notifyObservers(new Pair<>(EnumTargetTask.NETWORK, RequestFactory.createRequest(message)));
                }
            }
        }
        for (java.util.Map.Entry entry : tmpObstacles.entrySet()) {
            Obstacle obstacle = (Obstacle) entry.getValue();
            MessageModel message = new MessageObstacleUpdateState(obstacle.getPlayerPseudo(), obstacle.getPlayerId(), obstacle.getId(), 0);
            if (message != null) {
                this.setChanged();
                this.notifyObservers(new Pair<>(EnumTargetTask.NETWORK, RequestFactory.createRequest(message)));
            }
        }
        this.targets.initGame(this.mapController, this.gameModeController.getCurrentMode().getObstacles());
    }

    public void newRound() {
        MessageRoundState msg = new MessageRoundState("admin", "admin", false);
        this.gameModeController.getCurrentMode().stop();
        WindowController.addConsoleMsg("NEW ROUND");
        this.setChanged();
        this.notifyObservers(new Pair<>(EnumTargetTask.MASTER_SERVER, this.getPlayers()));
        this.mapController.getCurrentMap().resetCurrentObject();
        this.clearTargets();

        this.setChanged();
        this.notifyObservers(new Pair<>(EnumTargetTask.NETWORK, RequestFactory.createRequest(msg)));

        this.targets.initGame(this.mapController, this.gameModeController.getCurrentMode().getObstacles());
        for (java.util.Map.Entry<String, Player> entry : this.targets.getPlayers().entrySet()) {
            MessageModel message;
            Pair<Float, Float> newPositions = this.mapController.getCurrentMap().calcRespawnPoint(this.gameModeController.getCurrentGameMode(),
                    this.gameModeController.getCurrentMode().getIndexTeam(entry.getValue().getTeamId().toString()), entry.getValue().getTank().getTankState().getCollisionObject(), true);
            if (newPositions != null) {
                MessagePlayerUpdatePosition tmp = new MessagePlayerUpdatePosition();
                tmp.setPseudo(entry.getValue().getPseudo());
                tmp.setId(entry.getValue().getId());
                tmp.setX(newPositions.getKey());
                tmp.setY(newPositions.getValue());
                tmp.setResetMove(true);
                message = new MessagePlayerUpdatePosition(tmp);
            } else {
                message = new MessagePlayerDelete(entry.getValue().getPseudo(), entry.getValue().getId());
            }
            setChanged();
            notifyObservers(new Pair<>(EnumTargetTask.NETWORK, RequestFactory.createRequest(message)));
            this.sendAllTargetsToSomeone(entry.getValue().getConnection(), false, true, true);
        }
        this.mapController.getCurrentMap().resetCurrentObject();
        this.addNewRoundTimer();
    }

    public void sendAllTargetsToSomeone(Connection connection, boolean players, boolean obstacles, boolean teams) {
        if (players == true) {
            for (java.util.Map.Entry<String, Player> entry : this.getPlayers().entrySet()) {
                if (!entry.getValue().getConnection().equals(connection)) {
                    MessagePlayerNew tmpMessage = new MessagePlayerNew();
                    tmpMessage.setEnumTanks(entry.getValue().getTank().getTankState().getType());
                    tmpMessage.setId(entry.getValue().getId());
                    tmpMessage.setPseudo(entry.getValue().getPseudo());
                    tmpMessage.setPosX(0);
                    tmpMessage.setPosY(0);
                    this.setChanged();
                    this.notifyObservers(new Pair<>(EnumTargetTask.NETWORK, RequestFactory.createRequest(connection, tmpMessage)));
                }
            }
        }

        if (obstacles == true) {
            for (java.util.Map.Entry<String, Obstacle> entry : this.targets.getObstacles().entrySet()) {
                Obstacle tmp = entry.getValue();
                MessagePutObstacle tmpMessage = new MessagePutObstacle(tmp.getPlayerId(), tmp.getPlayerPseudo(), tmp.getId(), tmp.getType(), tmp.getX(), tmp.getY(), tmp.getAngle());
                WindowController.addConsoleMsg("send Box: " + entry.getKey());
                this.setChanged();
                this.notifyObservers(new Pair<>(EnumTargetTask.NETWORK, RequestFactory.createRequest(connection, tmpMessage)));
            }
        }

        if (teams == true) {
            for (Team team : this.gameModeController.getCurrentMode().getTeams()) {
                MessageRoundScore tmpMessage = new MessageRoundScore(team.getName(), team.getId(), team.getId(), EnumGameObject.NULL, team.getCurrentScore());
                this.setChanged();
                this.notifyObservers(new Pair<>(EnumTargetTask.NETWORK, RequestFactory.createRequest(connection, tmpMessage)));
            }
            for (java.util.Map.Entry<String, Player> entry : this.getPlayers().entrySet()) {
                MessageRoundScore tmpMessage = new MessageRoundScore(entry.getValue().getPseudo(), entry.getValue().getId(), entry.getValue().getTeamId(), EnumGameObject.NULL, entry.getValue().getCurrentScore());
                this.setChanged();
                this.notifyObservers(new Pair<>(EnumTargetTask.NETWORK, RequestFactory.createRequest(connection, tmpMessage)));
            }
        }

    }

    public void sendToAllExceptMe(MessageModel message) {
        for (java.util.Map.Entry entry : this.targets.getPlayers().entrySet()) {
            String id = (String) entry.getKey();
            Player player = (Player) entry.getValue();

            if (!id.equals(message.getId())) {
                setChanged();
                notifyObservers(new Pair<>(EnumTargetTask.NETWORK, RequestFactory.createRequest(player.getConnection(), message)));
            }
        }

        for (Pair<MessagePlayerNew, Connection> player : this.targets.getWaitingPeople()) {
            if (!player.getKey().getId().equals(message.getId())) {
                setChanged();
                notifyObservers(new Pair<>(EnumTargetTask.NETWORK, RequestFactory.createRequest(player.getValue(), message)));
            }
        }

        for (Pair<MessagePlayerObserverNew, Connection> player : this.targets.getObserverPeople()) {
            if (!player.getKey().getId().equals(message.getId())) {
                setChanged();
                notifyObservers(new Pair<>(EnumTargetTask.NETWORK, RequestFactory.createRequest(player.getValue(), message)));
            }
        }

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

    private void resultForCollision(String hitterId, String targetId, EnumCollision type) {
        WindowController.addConsoleMsg("Hitter : " + hitterId + ", target : " + targetId);

        if (this.gameModeController.getCurrentMode().isPlayable()) {
            List<MessageModel> allMessage = targets.doCollision(hitterId, targetId, type, this.gameModeController);
            WindowController.addConsoleMsg("MessageSize: " + allMessage.size());
            for (int i = 0; i < allMessage.size(); ++i) {
                MessageModel message = allMessage.get(i);
                WindowController.addConsoleMsg("messageType: " + message);
                if (message != null) {
                    this.setChanged();
                    this.notifyObservers(new Pair<>(EnumTargetTask.NETWORK, RequestFactory.createRequest(message)));
                    if (message instanceof MessagePlayerUpdateState) {
                        if (((MessagePlayerUpdateState) message).getCurrentLife() <= 0) {
                            if (this.gameModeController.isWinnerTeam() != null) {
                                MessageModel reviveTask = new MessagePlayerRevive(message.getPseudo(), message.getId(), -100, -100);
                                targets.getPlayer(message.getId()).revive();
                                setChanged();
                                notifyObservers(new Pair<>(EnumTargetTask.NETWORK, RequestFactory.createRequest(reviveTask)));
                            } else {
                                this.addReviveTimer(message);
                            }
                        }
                    }
                }
            }
            if (this.gameModeController.isPlayable() && this.gameModeController.isWinnerTeam() != null) {
                this.newRound();
            }
        }
    }


    //TIMERS
    private void addCollisionTimer(final String it1, final String it2, final int it3, final EnumCollision type) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                List<String> targeted = collisions.get(it1).get(it2).get(it3);
                int playerCount = targets.getPlayers().size();
                if (targeted.size() >= playerCount / 2) {
                    resultForCollision(it1, it2, type);
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
                Pair<Float, Float> newPositions = mapController.getCurrentMap().calcRespawnPoint(gameModeController.getCurrentGameMode(),
                        gameModeController.getCurrentMode().getIndexTeam(player.getTeamId()), player.getTank().getTankState().getCollisionObject(), false);
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
                setChanged();
                notifyObservers(new Pair<>(EnumTargetTask.NETWORK, RequestFactory.createRequest(new MessageRoundState("admin", "admin", true))));
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

    public ObstacleConfigData getObstacleConfigData() {
        return this.obstacleConfigData;
    }
}
