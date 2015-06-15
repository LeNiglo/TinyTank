package com.lefrantguillaume.components.gameComponent.controllers;

import com.lefrantguillaume.Utils.configs.CurrentUser;
import com.lefrantguillaume.Utils.configs.MasterConfig;
import com.lefrantguillaume.Utils.stockage.Pair;
import com.lefrantguillaume.Utils.stockage.Tuple;
import com.lefrantguillaume.Utils.tools.Debug;
import com.lefrantguillaume.components.collisionComponent.CollisionObject;
import com.lefrantguillaume.components.collisionComponent.EnumCollision;
import com.lefrantguillaume.components.gameComponent.animations.AnimatorGameData;
import com.lefrantguillaume.components.gameComponent.gameObject.EnumGameObject;
import com.lefrantguillaume.components.gameComponent.gameObject.obstacles.Obstacle;
import com.lefrantguillaume.components.gameComponent.gameObject.obstacles.ObstacleConfigData;
import com.lefrantguillaume.components.gameComponent.playerData.data.Player;
import com.lefrantguillaume.components.gameComponent.playerData.data.User;
import com.lefrantguillaume.components.gameComponent.RoundData.RoundController;
import com.lefrantguillaume.components.gameComponent.RoundData.Team;
import com.lefrantguillaume.components.gameComponent.gameObject.tanks.tools.TankConfigData;
import com.lefrantguillaume.components.gameComponent.playerData.action.PlayerAction;
import com.lefrantguillaume.Utils.tools.Block;
import com.lefrantguillaume.components.collisionComponent.CollisionController;
import com.lefrantguillaume.components.gameComponent.gameObject.projectiles.Shot;
import com.lefrantguillaume.components.networkComponent.networkGame.messages.msg.*;
import com.lefrantguillaume.components.taskComponent.EnumTargetTask;
import com.lefrantguillaume.components.taskComponent.TaskFactory;
import com.lefrantguillaume.components.networkComponent.networkGame.messages.MessageModel;
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
    private ObstacleConfigData obstacleConfigData;
    public final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public GameController() throws SlickException {
        this.players = new ArrayList<>();
        this.shots = new ArrayList<>();
        this.teams = new ArrayList<>();
        this.animatorGameData = null;
        this.roundController = new RoundController(this.players, this.teams);
        this.collisionController = new CollisionController();
        this.mapController = new MapController(this.collisionController, MasterConfig.getMapConfigFile());
        this.tankConfigData = new TankConfigData();
        this.obstacleConfigData = new ObstacleConfigData();
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
                            this.addPlayer(new Player(new User(task.getPseudo(), task.getId()), null, this.tankConfigData.getTank(task.getEnumGameObject()), this.getShots(), task.getPosX(), task.getPosY()));
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
                    if (message instanceof MessageObstacleUpdateState) {
                        Debug.debug("UPDATE STATE OBSTACLE");
                        this.changeStateObstacle((MessageObstacleUpdateState) message);
                    }
                    if (message instanceof MessageShotUpdateState) {
                        Debug.debug("UPDATE STATE SHOT");
                        this.changeStateShot((MessageShotUpdateState) message);
                    }
                }
            }
        }
    }

    // CHANGE FUNCTIONS
    public void addPlayer(Player player) {
        Debug.debug("add player: [" + player.getTank().getTankState().getX() + "," + player.getTank().getTankState().getY() + "]");

        for (int i = 0; i < player.getTank().getTankState().getCollisionObject().size(); ++i) {
            Block current = player.getTank().getTankState().getCollisionObject().get(i);
            CollisionObject obj = new CollisionObject(player.getIgnoredObjectList(), player.getTank().getTankState().getPositions(), current.getSizes(),
                    current.getShiftOrigin(), player.getUser().getIdUser(),
                    player.getUser().getId(), player.getTank().getTankState().getType(),
                    player.getTank().getTankState().getDirection().getAngle());
            obj.addObserver(player);
            player.addObserver(obj);
            this.collisionController.addCollisionObject(obj);
        }
        this.players.add(player);
    }


    public void changeStatePlayer(MessagePlayerUpdateState task) {
        Player player = this.getPlayer(task.getId());
        if (player != null) {
            player.getTank().getTankState().setCurrentLife(task.getCurrentLife());
            player.getTank().getTankState().setBoostEffect(task.getBoostEffect());
            player.getTank().getTankState().setShieldEffect(task.getShieldEffect());
            player.getTank().getTankState().setSlowEffect(task.getSlowEffect());
            player.getTank().getTankState().setCurrentArmor(task.getArmor());
            player.kill();
        }
    }

    public void changePositionPlayer(MessagePlayerUpdatePosition task) {
        Debug.debug("new pos [" + task.getX() + "," + task.getY() + "] : id=" + task.getId());
        Player player = this.getPlayer(task.getId());
        if (player != null) {
            player.getTank().getTankState().setX(task.getX());
            player.getTank().getTankState().setY(task.getY());
            List<CollisionObject> objects = this.collisionController.getCollisionObject(player.getUser().getId());
            for (CollisionObject object : objects) {
                object.setX(task.getX());
                object.setY(task.getY());
                object.setSaveX(task.getX());
                object.setSaveY(task.getY());
            }
            if (task.isResetMove() == true){
                player.getTank().getTankState().setMove(false);
            }
        }
    }

    public void revivePlayer(MessagePlayerRevive task) {
        Player player = this.getPlayer(task.getId());
        if (player != null) {
            player.revive(new Pair<>(task.getPosX(), task.getPosY()));
        }
    }

    public void putObject(MessagePutObstacle task) {
        Debug.debug("player for obstacle = " + task.getPseudo() + "  type: " + task.getType());
        Obstacle obstacle;

        if (task.getType() == EnumGameObject.UNBREAKABLE) {
            obstacle = this.obstacleConfigData.getWorldWall(task.getObstacleId());
        } else {
            obstacle = this.obstacleConfigData.getObstacle(task.getType());
        }
        if (obstacle != null) {
            obstacle.createObstacle(task.getId(), task.getPseudo(), task.getObstacleId(), task.getAngle(), task.getPosX(), task.getPosY());
            this.mapController.addObstacle(obstacle);
        }
    }

    public void changeStateObstacle(MessageObstacleUpdateState task) {
        Obstacle obstacle = this.getObstacle(task.getObstacleId());
        if (obstacle != null) {
            obstacle.setCurrentLife(task.getCurrentLife());
        }
    }

    public void changeStateShot(MessageShotUpdateState task) {
        Shot shot = this.getShot(task.getShotId());
        if (shot != null) {
            shot.setCurrentLife(task.getCurrentDamageShot());
            if (shot.getType() == EnumGameObject.LASER && shot.getExplode() == true) {
                Player player = this.getPlayer(shot.getUserId());
                if (player != null) {
                    player.setCanDoAction(true);
                }
            }
        }
    }

    // FUNCTIONS

    public void initConfigData(JSONObject tankConfig, JSONObject obstacleConfig) throws JSONException {
        this.initTankConfigData(tankConfig);
        this.initObstacleConfigData(obstacleConfig);
    }

    public void initTankConfigData(JSONObject config) throws JSONException {
        if (this.animatorGameData == null)
            throw new JSONException("tankConfigData failed");
        this.tankConfigData.initTanks(config, this.animatorGameData);
    }

    public void initObstacleConfigData(JSONObject config) throws JSONException {
        if (this.animatorGameData == null)
            throw new JSONException("obstacleConfigData failed");
        this.obstacleConfigData.initObstacle(config, this.animatorGameData);
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
        Tuple<EnumCollision, Boolean, Pair<CollisionObject, CollisionObject>> impactIds;

        if (this.collisionController != null) {
            this.collisionController.cleanCollision();
        }
        for (int i = 0; i < this.players.size(); ++i) {
            if (this.players.get(i).getTank().getTankState().isMove() && this.players.get(i).isAlive()) {
                if ((impactIds = this.collisionController.checkCollision(this.players.get(i).coordPredict(delta), this.players.get(i).getUser().getId())) != null) {
                    Debug.debug("Collision: " + impactIds);
                    if (impactIds.getV2() == true) {
                        this.players.get(i).move(delta);
                    }
                    if (impactIds.getV1() != EnumCollision.NOTHING) {
                        MessageModel request = new MessageCollision(CurrentUser.getPseudo(), CurrentUser.getId(), impactIds.getV3().getV1().getId(),
                                impactIds.getV3().getV2().getId(), impactIds.getV1());
                        this.setChanged();
                        this.notifyObservers(TaskFactory.createTask(EnumTargetTask.GAME, EnumTargetTask.MESSAGE_SERVER, request));
                    }
                }
            }
        }
        for (int i = 0; i < this.shots.size(); ++i) {
            if (!this.shots.get(i).getExplode()) {
                if ((impactIds = this.collisionController.checkCollision(this.shots.get(i).coordPredict(delta), this.shots.get(i).getId())) != null) {
                    Debug.debug("Collision: " + impactIds);
                    if (impactIds.getV2() == true) {
                        this.shots.get(i).move(delta);
                    }
                    if (impactIds.getV1() != EnumCollision.NOTHING) {
                        Debug.debug("Collision to Server");
                        MessageModel request = new MessageCollision(CurrentUser.getPseudo(), CurrentUser.getId(), impactIds.getV3().getV1().getId(),
                                impactIds.getV3().getV2().getId(), impactIds.getV1());
                        this.setChanged();
                        this.notifyObservers(TaskFactory.createTask(EnumTargetTask.GAME, EnumTargetTask.MESSAGE_SERVER, request));
                    }
                }
            }
        }
    }

    // DRAW FUNCTIONS
    public void drawGamePlayers(Graphics g) {
        for (int i = 0; i < this.players.size(); ++i) {
            Player current = this.players.get(i);
            if (current.isAlive()) {
                if (current.getTank().getBodyAnimator().isStopped()) {
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
            if (current.getAnimator().isStopped()) {
                this.collisionController.deleteCollisionObject(this.shots.get(i).getId());
                this.shots.remove(i);
            } else {
                current.draw(g);

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

                if (current.getAnimator() != null) {
                    if (current.getAnimator().isStopped()) {
                        this.mapController.deleteObstacle(current.getId());
                    } else {
                        current.getAnimator().currentAnimation().getCurrentFrame().setCenterOfRotation(current.getShiftOrigin().getV1() * -1, current.getShiftOrigin().getV2() * -1);
                        current.getAnimator().currentAnimation().getCurrentFrame().setRotation(current.getAngle());
                        g.drawAnimation(current.getAnimator().currentAnimation(), current.getGraphicalX(), current.getGraphicalY());
                    }
                }
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

    public ObstacleConfigData getObstacleConfigData() {
        return this.obstacleConfigData;
    }

    public RoundController getRoundController() {
        return roundController;
    }

    public List<Shot> getShots() {
        return shots;
    }

    public Player getPlayer(String id) {
        for (Player player : this.players) {
            if (id.equals(player.getUser().getIdUser())) {
                return player;
            }
        }
        return null;
    }

    public Obstacle getObstacle(String id) {
        return this.mapController.getObstacle(id);
    }

    public Shot getShot(String id) {
        for (Shot shot : this.shots) {
            if (id.equals(shot.getId())) {
                return shot;
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
