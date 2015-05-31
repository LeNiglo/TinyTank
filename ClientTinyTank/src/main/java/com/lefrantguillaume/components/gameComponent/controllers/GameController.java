package com.lefrantguillaume.components.gameComponent.controllers;

import com.lefrantguillaume.Utils.configs.CurrentUser;
import com.lefrantguillaume.Utils.configs.MasterConfig;
import com.lefrantguillaume.Utils.stockage.Pair;
import com.lefrantguillaume.Utils.stockage.Tuple;
import com.lefrantguillaume.Utils.tools.Block;
import com.lefrantguillaume.Utils.tools.Debug;
import com.lefrantguillaume.components.collisionComponent.CollisionController;
import com.lefrantguillaume.components.collisionComponent.CollisionObject;
import com.lefrantguillaume.components.gameComponent.RoundData.RoundController;
import com.lefrantguillaume.components.gameComponent.RoundData.Team;
import com.lefrantguillaume.components.gameComponent.animations.AnimatorGameData;
import com.lefrantguillaume.components.gameComponent.gameObject.EnumType;
import com.lefrantguillaume.components.gameComponent.gameObject.obstacles.Obstacle;
import com.lefrantguillaume.components.gameComponent.gameObject.obstacles.ObstacleConfigData;
import com.lefrantguillaume.components.gameComponent.gameObject.projectiles.Shot;
import com.lefrantguillaume.components.gameComponent.gameObject.tanks.tools.TankConfigData;
import com.lefrantguillaume.components.gameComponent.playerData.action.PlayerAction;
import com.lefrantguillaume.components.gameComponent.playerData.data.Player;
import com.lefrantguillaume.components.gameComponent.playerData.data.User;
import com.lefrantguillaume.components.networkComponent.networkGame.messages.MessageModel;
import com.lefrantguillaume.components.networkComponent.networkGame.messages.msg.*;
import com.lefrantguillaume.components.taskComponent.EnumTargetTask;
import com.lefrantguillaume.components.taskComponent.TaskFactory;
import com.lefrantguillaume.networkComponent.networkGame.messages.msg.MessageGestRound;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by andres_k on 13/03/2015.
 */
public class GameController extends Observable implements Observer {
    private List<Player> players;
    private List<Shot> shots;
    private List<Team> teams;
    private CollisionController collisionController;
    private MapController mapController;
    private RoundController roundController;
    private AnimatorGameData animatorGameData;
    private TankConfigData tankConfigData;
    private ObstacleConfigData obstaclesConfigData;
    public final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public GameController() throws SlickException {
        this.players = new ArrayList<>();
        this.shots = new ArrayList<>();
        this.teams = new ArrayList<>();
        this.animatorGameData = null;
        this.roundController = new RoundController(this.players, this.teams);
        this.mapController = new MapController(this.collisionController, MasterConfig.getMapConfigFile());
        this.collisionController = new CollisionController();
        this.tankConfigData = new TankConfigData();
        this.obstaclesConfigData = new ObstacleConfigData();
    }

    public void clearData() {
        this.players.clear();
        this.shots.clear();
        this.teams.clear();
        this.scheduler.shutdown();
        this.collisionController.clearCollisionObjects();
        this.mapController.clearObstacles();
    }

    public void initGame() {
        this.collisionController.createWorld(this.mapController);
        this.scheduleToSendCurrentPlayerPosition();
    }

    @Override
    public void update(Observable o, Object arg) {
        Tuple<EnumTargetTask, EnumTargetTask, Object> received = (Tuple<EnumTargetTask, EnumTargetTask, Object>) arg;

        if (received.getV2().equals(EnumTargetTask.GAME)) {
            Debug.debug("gameTask " + received);
            if (received.getV3() instanceof MessageModel) {
                MessageModel message = (MessageModel) received.getV3();
                if (message.getPlayerAction() == true) {
                    Player current = this.getPlayer(message.getId());
                    if (current != null) {
                        current.doAction(new PlayerAction(message), this.collisionController);
                    }
                } else {
                    if (message instanceof MessagePlayerNew) {
                        MessagePlayerNew task = (MessagePlayerNew) message;
                        if (this.animatorGameData != null && this.tankConfigData.isValid()) {
                            Debug.debug("NEW PLAYER");
                            this.addPlayer(new Player(new User(task.getPseudo(), task.getId()), null, this.tankConfigData.getTank(task.getEnumTanks()), this.getShots(), task.getPosX(), task.getPosY()));
                            if (task.getId().equals(CurrentUser.getId())) {
                                CurrentUser.setInGame(true);
                                this.initGame();
                            }
                            if (CurrentUser.isInGame() == true) {
                                MessageModel request = new MessagePlayerUpdatePosition(CurrentUser.getPseudo(), CurrentUser.getId(),
                                        this.getPlayer(CurrentUser.getId()).getTank().getTankState().getX(),
                                        this.getPlayer(CurrentUser.getId()).getTank().getTankState().getY());
                                this.setChanged();
                                this.notifyObservers(TaskFactory.createTask(EnumTargetTask.GAME, EnumTargetTask.MESSAGE_SERVER, request));
                            }
                        }
                    }
                    if (message instanceof MessagePlayerDelete) {
                        this.deletePlayer(message.getId());
                    }
                    if (message instanceof MessagePlayerUpdateState) {
                        Debug.debug("UPDATE STATE PLAYER");
                        this.changeStatePlayer((MessagePlayerUpdateState) message);
                    }
                    if (message instanceof MessagePlayerUpdatePosition) {
                        Debug.debug("UPDATE POS PLAYER");
                        this.changePositionPlayer((MessagePlayerUpdatePosition) message);
                    }
                    if (message instanceof MessagePlayerRevive) {
                        Debug.debug("REVIVE PLAYER");
                        this.revivePlayer((MessagePlayerRevive) message);
                    }
                    if (message instanceof MessagePutObstacle) {
                        Debug.debug("PUT OBJECT");
                        this.putObject((MessagePutObstacle) message);
                    }
                    if (message instanceof MessageGestRound) {
                        Debug.debug("GEST ROUND");
                        this.gestRound((MessageGestRound) message);
                    }
                }
            }
        }
    }

    private void gestRound(MessageGestRound received) {
        if (received.getAction() == 0) {
            Debug.debug("PREPARE GAME");
        } else if (received.getAction() == 1) {
            Debug.debug("START GAME");
        } else if (received.getAction() == 2) {
            Debug.debug("WIN ROUND");
        } else if (received.getAction() == 3) {
            Debug.debug("LOOSE ROUND");
        } else if (received.getAction() == 4) {
            Debug.debug("TIE ROUND");
        } else {
            Debug.debug("THIS MESSAGE IS WRONG !");
            throw new RuntimeException("Invalid Message ...");
        }
    }

    // CHANGE FUNCTIONS
    public void addPlayer(Player player) {
        Debug.debug("add player: [" + player.getTank().getTankState().getX() + "," + player.getTank().getTankState().getY() + "]");

        for (int i = 0; i < player.getTank().getTankState().getCollisionObject().size(); ++i) {
            Block current = player.getTank().getTankState().getCollisionObject().get(i);
            CollisionObject obj = new CollisionObject(true, player.getTank().getTankState().getPositions(), current.getSizes(),
                    current.getShiftOrigin(), player.getUser().getIdUser(),
                    player.getUser().getId(), EnumType.TANK,
                    player.getTank().getTankState().getDirection().getAngle());
            obj.addObserver(player);
            player.addObserver(obj);
            this.collisionController.addCollisionObject(obj);
        }
        this.players.add(player);
    }


    public void changeStatePlayer(MessagePlayerUpdateState task) {
        for (Player player : this.players) {
            if (player.getUser().getIdUser().equals(task.getId())) {
                player.getTank().getTankState().setCurrentLife(task.getCurrentLife());
                player.getTank().getTankState().setBoostEffect(task.getBoostEffect());
                player.getTank().getTankState().setShieldEffect(task.getShieldEffect());
                player.getTank().getTankState().setSlowEffect(task.getSlowEffect());
                player.getTank().getTankState().setCurrentArmor(task.getArmor());
                player.kill();
                break;
            }
        }
    }

    public void changePositionPlayer(MessagePlayerUpdatePosition task) {
        Debug.debug("new pos [" + task.getX() + "," + task.getY() + "] : id=" + task.getId());
        for (Player player : this.players) {
            if (player.getUser().getIdUser().equals(task.getId())) {
                player.getTank().getTankState().setX(task.getX());
                player.getTank().getTankState().setY(task.getY());
                List<CollisionObject> objects = this.collisionController.getCollisionObject(player.getUser().getId());
                for (CollisionObject object : objects) {
                    object.setX(task.getX());
                    object.setY(task.getY());
                    object.setSaveX(task.getX());
                    object.setSaveY(task.getY());
                }
                break;
            }
        }
    }

    public void revivePlayer(MessagePlayerRevive task) {
        for (int i = 0; i < this.players.size(); ++i) {
            if (this.players.get(i).getUser().getIdUser().equals(task.getId())) {
                Debug.debug("Revive ok");
                this.players.get(i).revive(new Pair<Float, Float>(task.getPosX(), task.getPosY()));
                break;
            }
        }
    }

    public void putObject(MessagePutObstacle task) {

        Obstacle obstacle = this.obstaclesConfigData.getNewObstacle(task.getType().getIndex());
        obstacle.createObstacle(task.getId(), task.getObstacleId(), task.getAngle(), task.getPosX(), task.getPosY());
        this.mapController.addObstacle(obstacle);
    }

    // FUNCTIONS
    public void initConfigData(JSONObject config) throws JSONException {
        this.initTankConfigData(config);
        this.initObstacleConfigData(config);
    }

    public void initTankConfigData(JSONObject config) throws JSONException {
        if (this.animatorGameData == null)
            throw new JSONException("tankConfigData failed");
        this.tankConfigData.initTanks(config, this.animatorGameData);
    }

    public void initObstacleConfigData(JSONObject config) throws JSONException {
        if (this.animatorGameData == null)
            throw new JSONException("obstacleConfigData failed");
        this.obstaclesConfigData.initObstacles(config, this.animatorGameData);
    }

    public void deletePlayer(String id) {
        for (int i = 0; i < this.players.size(); ++i) {
            if (this.players.get(i).getUser().getIdUser().equals(id)) {
                this.getCollisionController().deleteCollisionObject(this.players.get(i).getUser().getId());
                this.players.remove(i);
                break;
            }
        }
    }

    // UPDATE GAME FUNCTIONS
    public void updateGame(float delta) {
        Pair<Boolean, Pair<String, String>> impactIds;

        if (this.collisionController != null) {
            this.collisionController.cleanCollision();
        }
        for (int i = 0; i < this.players.size(); ++i) {
            if (this.players.get(i).getTank().getTankState().isMove() && this.players.get(i).isAlive()) {
                if ((impactIds = this.collisionController.checkCollision(this.players.get(i).coordPredict(delta), this.players.get(i).getUser().getId())) != null) {
                    if (impactIds.getV2().getV1().equals("null")) {
                        this.players.get(i).move(delta);
                    }
                }
            }
        }
        for (int i = 0; i < this.shots.size(); ++i) {
            if (!this.shots.get(i).getExplode()) {
                if ((impactIds = this.collisionController.checkCollision(this.shots.get(i).coordPredict(delta), this.shots.get(i).getId())) != null) {
                    if (impactIds.getV2().getV1().equals("null")) {
                        this.shots.get(i).move(delta);
                    } else if (impactIds.getV1() == true && !impactIds.getV2().getV1().equals("null")) {
                        Debug.debug("Collision to Server");
                        MessageModel request = new MessageCollision(CurrentUser.getPseudo(), CurrentUser.getId(), impactIds.getV2().getV1(), impactIds.getV2().getV2());
                        this.setChanged();
                        this.notifyObservers(TaskFactory.createTask(EnumTargetTask.GAME, EnumTargetTask.MESSAGE_SERVER, request));
                    }
                }
            }
        }
    }

    public void scheduleToSendCurrentPlayerPosition() {
        /*
        this.scheduler.scheduleAtFixedRate(new Runnable() {
            public void run() {
                Player currentPlayer = getPlayer(CurrentUser.getId());
                if (currentPlayer != null && currentPlayer.isAlive()) {
                    MessageModel request = new MessagePlayerUpdatePosition(CurrentUser.getPseudo(), CurrentUser.getId(), currentPlayer.getTank().getTankState().getX(), currentPlayer.getTank().getTankState().getY());
                    setChanged();
                    notifyObservers(request);
                }
            }
        }, 300, 300, TimeUnit.MILLISECONDS);
*/
    }

    // DRAW FUNCTIONS
    public void drawGamePlayers(Graphics g) {
        for (int i = 0; i < this.players.size(); ++i) {
            Player current = this.players.get(i);
            if (current.isAlive()) {
                if (current.getTank().getBodyAnimator().currentAnimation().isStopped()) {
                    current.die();
                } else if (current.getTank().getBodyAnimator().isPrintable()) {
                    current.getTank().getBodyAnimator().currentAnimation().getCurrentFrame().setCenterOfRotation(current.getTank().getTankState().getShiftOrigin().getV1() * -1,
                            current.getTank().getTankState().getShiftOrigin().getV2() * -1);
                    current.getTank().getBodyAnimator().currentAnimation().getCurrentFrame().setRotation(current.getTank().getTankState().getDirection().getAngle());
                    g.drawAnimation(current.getTank().getBodyAnimator().currentAnimation(), current.getTank().getTankState().getGraphicalX(), current.getTank().getTankState().getGraphicalY());
                }
                if (current.getTank().getTopAnimator().isPrintable()) {
                    current.getTank().getTopAnimator().currentAnimation().getCurrentFrame().setCenterOfRotation(current.getTank().getTankWeapon().getShiftWeaponOrigin().getV1() * -1, -1 * current.getTank().getTankWeapon().getShiftWeaponOrigin().getV2());
                    current.getTank().getTopAnimator().currentAnimation().getCurrentFrame().setRotation(current.getTank().getTankState().getGunAngle());
                    g.drawAnimation(current.getTank().getTopAnimator().currentAnimation(), current.getTank().getTankWeapon().getGraphicalX(current.getTank().getTankState().getPositions().getV1()),
                            current.getTank().getTankWeapon().getGraphicalY(current.getTank().getTankState().getPositions().getV2()));
                }

            }
        }
    }

    public void drawGameShots(Graphics g) {

        for (int i = 0; i < this.shots.size(); ++i) {
            Shot current = this.shots.get(i);
            if (current.getAnimator().currentAnimation().isStopped()) {
                this.collisionController.deleteCollisionObject(this.shots.get(i).getId());
                this.shots.remove(i);
            } else {
                current.getAnimator().currentAnimation().getCurrentFrame().setCenterOfRotation(current.getShiftOrigin().getV1() * -1, current.getShiftOrigin().getV2() * -1);
                current.getAnimator().currentAnimation().getCurrentFrame().setRotation(current.getAngle());
                g.drawAnimation(current.getAnimator().currentAnimation(), current.getGraphicalX(), current.getGraphicalY());

                // debug
                g.setColor(Color.black);
                g.drawRect(current.getX(), current.getY(), 1, 1);
                g.setColor(Color.blue);
                g.drawRect(current.getGraphicalX(), current.getGraphicalY(), 1, 1);
                g.setColor(Color.red);
            }
        }
    }

    public void drawGameMap(Graphics g) {
        if (this.mapController != null) {
            g.drawAnimation(this.mapController.getMapAnimator().currentAnimation(), 0, 0);
            for (int i = 0; i < this.mapController.getObstacles().size(); ++i) {
                Obstacle current = this.mapController.getObstacles().get(i);
                g.drawAnimation(current.getAnimator().currentAnimation(), current.getX(), current.getY());
            }
        }
    }

    // GETTERS
    public List<Player> getPlayers() {
        return this.players;
    }

    public CollisionController getCollisionController() {
        return this.collisionController;
    }

    public MapController getMapController() {
        return this.mapController;
    }

    public RoundController getRoundController() {
        return roundController;
    }

    public List<Shot> getShots() {
        return shots;
    }

    public Player getPlayer(String id) {
        for (int i = 0; i < this.players.size(); ++i) {
            if (id.equals(this.players.get(i).getUser().getIdUser())) {
                return this.players.get(i);
            }
        }
        return null;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setAnimatorGameData(AnimatorGameData animatorGameData) {
        this.animatorGameData = animatorGameData;
    }
}
