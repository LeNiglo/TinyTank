package com.lefrantguillaume.components.gameComponent.controllers;

import com.lefrantguillaume.utils.configs.CurrentUser;
import com.lefrantguillaume.utils.configs.GlobalVariable;
import com.lefrantguillaume.utils.configs.MasterConfig;
import com.lefrantguillaume.utils.stockage.Pair;
import com.lefrantguillaume.utils.stockage.Tuple;
import com.lefrantguillaume.utils.tools.Block;
import com.lefrantguillaume.utils.tools.ConsoleWriter;
import com.lefrantguillaume.components.collisionComponent.CollisionController;
import com.lefrantguillaume.components.collisionComponent.CollisionObject;
import com.lefrantguillaume.components.collisionComponent.EnumCollision;
import com.lefrantguillaume.components.gameComponent.RoundData.RoundController;
import com.lefrantguillaume.components.gameComponent.RoundData.Team;
import com.lefrantguillaume.components.gameComponent.animations.Animator;
import com.lefrantguillaume.components.gameComponent.animations.AnimatorGameData;
import com.lefrantguillaume.components.gameComponent.gameObject.EnumGameObject;
import com.lefrantguillaume.components.gameComponent.gameObject.obstacles.Obstacle;
import com.lefrantguillaume.components.gameComponent.gameObject.obstacles.ObstacleConfigData;
import com.lefrantguillaume.components.gameComponent.gameObject.projectiles.Shot;
import com.lefrantguillaume.components.gameComponent.gameObject.tanks.tools.TankConfigData;
import com.lefrantguillaume.components.gameComponent.playerData.action.PlayerAction;
import com.lefrantguillaume.components.gameComponent.playerData.data.Player;
import com.lefrantguillaume.components.gameComponent.playerData.data.User;
import com.lefrantguillaume.components.graphicsComponent.graphics.EnumWindow;
import com.lefrantguillaume.components.graphicsComponent.sounds.EnumSound;
import com.lefrantguillaume.components.graphicsComponent.sounds.SoundController;
import com.lefrantguillaume.components.graphicsComponent.userInterface.overlay.EnumOverlayElement;
import com.lefrantguillaume.components.networkComponent.networkGame.messages.MessageModel;
import com.lefrantguillaume.components.networkComponent.networkGame.messages.msg.*;
import com.lefrantguillaume.components.taskComponent.EnumTargetTask;
import com.lefrantguillaume.components.taskComponent.TaskFactory;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

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

    private StateBasedGame stateWindow;

    public GameController() throws SlickException {
        this.players = new ArrayList<>();
        this.shots = new ArrayList<>();
        this.teams = new ArrayList<>();

        this.stateWindow = null;

        this.roundController = new RoundController(this.players, this.teams);
        this.collisionController = new CollisionController();
        this.mapController = new MapController(this.collisionController, MasterConfig.getMapConfigFile());

        this.animatorGameData = null;
        this.tankConfigData = new TankConfigData();
        this.obstacleConfigData = new ObstacleConfigData();
    }

    public void clearData() {
        this.players.clear();
        this.shots.clear();
        this.teams.clear();
        this.collisionController.clearCollisionObjects();
        this.mapController.clearObstacles();
    }

    public void initGame() {
        for (Player other : this.players) {
            if (CurrentUser.getIdTeam().equals(other.getIdTeam()) == false) {
                other.getTank().getTankState().setCurrentTeam(EnumGameObject.getEnemyEnum(other.getTank().getTankState().getType()));
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof Tuple) {
            Tuple<EnumTargetTask, EnumTargetTask, Object> received = (Tuple<EnumTargetTask, EnumTargetTask, Object>) arg;

            if (received.getV1().equals(EnumTargetTask.WINDOWS) && received.getV2().equals(EnumTargetTask.GAME)) {
                if (received.getV3() instanceof EnumWindow && this.stateWindow != null) {
                    Player player = this.getPlayer(CurrentUser.getId());
                    MessageModel request;
                    if (player != null) {
                        request = new MessagePlayerDelete(CurrentUser.getPseudo(), CurrentUser.getId());
                    } else {
                        request = new MessagePlayerObserverDelete(CurrentUser.getPseudo(), CurrentUser.getId());
                    }
                    CurrentUser.setInGame(false);
                    this.setChanged();
                    this.notifyObservers(TaskFactory.createTask(EnumTargetTask.INPUT, EnumTargetTask.MESSAGE_SERVER, request));

                    this.stateWindow.enterState(((EnumWindow) received.getV3()).getValue());
                } else if (received.getV3() instanceof MessageModel) {
                    MessageModel message = (MessageModel) received.getV3();
                    if (message.getPlayerAction() == true) {
                        Player current = this.getPlayer(message.getId());
                        if (current != null) {
                            List<Object> result = current.doAction(new PlayerAction(message), this.collisionController);
                            if (result != null) {
                                for (Object item : result) {
                                    ConsoleWriter.debug("\nGAME_CONTROLER: resultPlayerAction ->" + item);
                                    if (item instanceof Obstacle) {
                                        this.mapController.addObstacle((Obstacle) item);
                                    } else if (item instanceof Pair) {
                                        if (((Pair) item).getV1() instanceof EnumOverlayElement) {
                                            this.setChanged();
                                            this.notifyObservers(TaskFactory.createTask(EnumTargetTask.GAME, EnumTargetTask.GAME_OVERLAY, item));
                                        }
                                    }
                                }

                            }
                        }
                    } else {
                        if (message instanceof MessagePlayerNew) {
                            ConsoleWriter.debug("NEW PLAYER");
                            this.doPlayerNew((MessagePlayerNew) message);
                        } else if (message instanceof MessagePlayerObserverNew) {
                            ConsoleWriter.debug("NEW OBSERVER");
                            this.doPlayerObserver((MessagePlayerObserverNew) message);
                        } else if (message instanceof MessagePlayerDelete) {
                            ConsoleWriter.debug("DELETE PLAYER");
                            this.doPlayerDelete(message.getId());
                        } else if (message instanceof MessagePlayerUpdateState) {
                            ConsoleWriter.debug("UPDATE STATE PLAYER");
                            this.doPlayerUpdateState((MessagePlayerUpdateState) message);
                        } else if (message instanceof MessagePlayerUpdatePosition) {
                            ConsoleWriter.debug("UPDATE POS PLAYER");
                            this.doPlayerUpdatePosition((MessagePlayerUpdatePosition) message);
                        } else if (message instanceof MessagePlayerRevive) {
                            ConsoleWriter.debug("REVIVE PLAYER");
                            this.doRevivePlayer((MessagePlayerRevive) message);
                        } else if (message instanceof MessagePutObstacle) {
                            ConsoleWriter.debug("PUT OBJECT");
                            this.doPutObstacle((MessagePutObstacle) message);
                        } else if (message instanceof MessageObstacleUpdateState) {
                            ConsoleWriter.debug("UPDATE STATE OBSTACLE");
                            this.doObstacleUpdateState((MessageObstacleUpdateState) message);
                        } else if (message instanceof MessageShotUpdateState) {
                            ConsoleWriter.debug("UPDATE STATE SHOT");
                            this.doShotUpdateState((MessageShotUpdateState) message);
                        }
                    }
                }
            }
        }
    }

    // CHANGE FUNCTIONS

    public void doPlayerNew(MessagePlayerNew task) {
        if (this.animatorGameData != null && this.tankConfigData.isValid()) {
            if (this.getPlayer(task.getId()) == null) {
                ConsoleWriter.debug("\n NEW PLAYER with id: " + task.getId() + " idTeam:" + task.getTeamId());
                this.addPlayer(new Player(new User(task.getPseudo(), task.getId()), task.getTeamId(), this.tankConfigData.getTank(task.getEnumGameObject()),
                        this.getShots(), task.getPosX(), task.getPosY()));
                if (task.getId().equals(CurrentUser.getId())) {
                    CurrentUser.setInGame(true);
                    CurrentUser.setIdTeam(task.getTeamId());
                    this.initGame();
                    this.setChanged();
                    this.notifyObservers(TaskFactory.createTask(EnumTargetTask.GAME, EnumTargetTask.GAME_OVERLAY, new MessageChat("Admin", "admin", true, "Welcome!")));
                    this.setChanged();
                    this.notifyObservers(TaskFactory.createTask(EnumTargetTask.GAME, EnumTargetTask.GAME_OVERLAY, this.getPlayer(CurrentUser.getId())));
                }
                if (CurrentUser.isInGame() == true) {
                    Player other = this.getPlayer(task.getId());
                    if (CurrentUser.getIdTeam().equals(other.getIdTeam()) == false) {
                        other.getTank().getTankState().setCurrentTeam(EnumGameObject.getEnemyEnum(other.getTank().getTankState().getType()));
                    }
                    MessageModel request = new MessagePlayerUpdatePosition(CurrentUser.getPseudo(), CurrentUser.getId(),
                            this.getPlayer(CurrentUser.getId()).getTank().getTankState().getX(),
                            this.getPlayer(CurrentUser.getId()).getTank().getTankState().getY());
                    this.setChanged();
                    this.notifyObservers(TaskFactory.createTask(EnumTargetTask.GAME, EnumTargetTask.MESSAGE_SERVER, request));
                }
            }
        }
    }

    public void doPlayerObserver(MessagePlayerObserverNew task) {
        if (task.getId().equals(CurrentUser.getId())) {
            CurrentUser.setIdTeam(task.getTeamId());
        }
        if (CurrentUser.isInGame() == true) {
            Player current = this.getPlayer(CurrentUser.getId());
            MessageModel request = new MessagePlayerUpdatePosition(CurrentUser.getPseudo(), CurrentUser.getId(),
                    current.getTank().getTankState().getX(),
                    current.getTank().getTankState().getY());
            this.setChanged();
            this.notifyObservers(TaskFactory.createTask(EnumTargetTask.GAME, EnumTargetTask.MESSAGE_SERVER, request));
        }
    }

    public void doPlayerDelete(String id) {
        for (int i = 0; i < this.players.size(); ++i) {
            if (this.players.get(i).getUser().getIdUser().equals(id)) {
                this.players.get(i).getTank().explode();
                this.getCollisionController().deleteCollisionObject(this.players.get(i).getUser().getId());
                this.players.remove(i);
                break;
            }
        }
    }

    public void doPlayerUpdateState(MessagePlayerUpdateState task) {
        Player player = this.getPlayer(task.getId());
        if (player != null) {
            player.getTank().getTankState().setCurrentLife(task.getCurrentLife());
            player.getTank().getTankState().setBoostEffect(task.getBoostEffect());
            player.getTank().getTankState().setShieldEffect(task.getShieldEffect());
            player.getTank().getTankState().setSlowEffect(task.getSlowEffect());
            player.getTank().getTankState().setCurrentArmor(task.getArmor());
            player.kill();
            if (player.getTank().getTankState().getCurrentLife() == 0) {
                SoundController.play(EnumSound.EXPLOSION_TANK);
            }
            ConsoleWriter.debug("isCurrent ? " + CurrentUser.getId() + " =? " + player.getUser().getIdUser());
            if (CurrentUser.getId().equals(player.getUser().getIdUser())) {
                Pair order = new Pair<>(EnumOverlayElement.USER_LIFE, new Pair<>("cutBody", player.getTank().getTankState().getPercentageLife()));
                this.setChanged();
                this.notifyObservers(TaskFactory.createTask(EnumTargetTask.GAME, EnumTargetTask.GAME_OVERLAY, new Pair<>(EnumOverlayElement.GENERIC_USER_STAT, order)));
            }
        }
    }

    public void doPlayerUpdatePosition(MessagePlayerUpdatePosition task) {
        ConsoleWriter.debug("new pos [" + task.getX() + "," + task.getY() + "] : id=" + task.getId());
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
            if (task.isResetMove() == true) {
                player.getTank().getTankState().setMove(false);
            }
        }
    }

    public void doRevivePlayer(MessagePlayerRevive task) {
        Player player = this.getPlayer(task.getId());
        if (player != null) {
            player.revive(new Pair<>(task.getPosX(), task.getPosY()));
            if (CurrentUser.getId().equals(player.getUser().getIdUser())) {
                Pair order = new Pair<>(EnumOverlayElement.USER_LIFE, new Pair<>("cutBody", player.getTank().getTankState().getPercentageLife()));
                this.setChanged();
                this.notifyObservers(TaskFactory.createTask(EnumTargetTask.GAME, EnumTargetTask.GAME_OVERLAY, new Pair<>(EnumOverlayElement.GENERIC_USER_STAT, order)));
            }
        }
    }

    public void doPutObstacle(MessagePutObstacle task) {
        ConsoleWriter.debug("player for obstacle = " + task.getPseudo() + "  obstacle: " + task.getObstacleId() + "(" + task.getType() + ")");

        if (this.getObstacle(task.getObstacleId()) == null) {
            Obstacle obstacle;
            if (task.getType() == EnumGameObject.SHIELD) {
                return;
            }
            if (task.getType() == EnumGameObject.UNBREAKABLE) {
                obstacle = this.obstacleConfigData.getWorldWall(task.getObstacleId());
            } else {
                obstacle = this.obstacleConfigData.getObstacle(task.getType());
            }
            if (obstacle != null) {
                obstacle.createObstacle(task.getId(), task.getPseudo(), task.getObstacleId(), task.getAngle(), task.getPosX(), task.getPosY());
                this.mapController.addObstacle(obstacle);
            }
            if (task.getId().equals(CurrentUser.getId())) {
                Player player = getPlayer(CurrentUser.getId());
                if (player != null) {
                    this.setChanged();
                    this.notifyObservers(TaskFactory.createTask(EnumTargetTask.GAME, EnumTargetTask.GAME_OVERLAY, new Pair<>(EnumOverlayElement.TABLE_ICON, new Pair<>(EnumOverlayElement.BOX, player.getTank().getTankBox().getCooldownPut()))));
                }
            }
        } else {
            ConsoleWriter.debug("Obstacle already exist");
        }
    }

    public void doObstacleUpdateState(MessageObstacleUpdateState task) {
        Obstacle obstacle = this.getObstacle(task.getObstacleId());
        if (obstacle != null) {
            obstacle.setCurrentLife(task.getCurrentLife());

            if (obstacle.getCurrentLife() == 0 && obstacle.getType() == EnumGameObject.MINE) {
                SoundController.play(EnumSound.EXPLOSION_MINE);
            }
            if (obstacle.getType() == EnumGameObject.SHIELD && CurrentUser.getId().equals(obstacle.getPlayerId())) {
                Pair order = new Pair<>(EnumOverlayElement.USER_SHIELD, new Pair<>("cutBody", obstacle.getPercentageLife()));
                this.setChanged();
                this.notifyObservers(TaskFactory.createTask(EnumTargetTask.GAME, EnumTargetTask.GAME_OVERLAY, new Pair<>(EnumOverlayElement.GENERIC_USER_STAT, order)));
            }
        }
    }

    public void doShotUpdateState(MessageShotUpdateState task) {
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
        this.tankConfigData.initTanks(config, this.animatorGameData, this.obstacleConfigData);
    }

    public void initObstacleConfigData(JSONObject config) throws JSONException {
        if (this.animatorGameData == null)
            throw new JSONException("obstacleConfigData failed");
        this.obstacleConfigData.initObstacle(config, this.animatorGameData);
    }

    public void addPlayer(Player player) {
        ConsoleWriter.debug("add player: [" + player.getTank().getTankState().getX() + "," + player.getTank().getTankState().getY() + "]");

        for (int i = 0; i < player.getTank().getTankState().getCollisionObject().size(); ++i) {
            Block current = player.getTank().getTankState().getCollisionObject().get(i);
            CollisionObject obj = new CollisionObject(player.getIgnoredObjectList(), player.getTank().getTankState().getPositions(), current.getSizes(),
                    current.getShiftOrigin(), player.getUser().getIdUser(),
                    player.getUser().getId(), player.getTank().getTankState().getType(),
                    player.getTank().getTankState().getDirection().getAngle());
            obj.addObserver(player.getTank().getTankState());
            player.getTank().getTankState().addObserver(obj);
            this.collisionController.addCollisionObject(obj);
        }
        this.players.add(player);
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
                    if (impactIds.getV2() == true) {
                        this.players.get(i).move(delta);
                    }
                    if (impactIds.getV1() != EnumCollision.NOTHING) {
                        ConsoleWriter.debug("Collision: " + impactIds);
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
                    if (impactIds.getV2() == true) {
                        this.shots.get(i).move(delta);
                    }

                    if (impactIds.getV1() != EnumCollision.NOTHING) {
                        ConsoleWriter.debug("Collision: " + impactIds);
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
                Animator bodyAnimator = current.getTank().getBodyAnimator();
                Animator topAnimator = current.getTank().getTopAnimator();
                Animator spellAnimator = current.getTank().getSpellAnimator();

                if (bodyAnimator != null) {
                    if (bodyAnimator.isDeleted()) {
                        current.die();
                    } else if (bodyAnimator.isPrintable()) {
                        bodyAnimator.currentAnimation().getCurrentFrame().setCenterOfRotation(current.getTank().getTankState().getShiftOrigin().getV1() * -1,
                                current.getTank().getTankState().getShiftOrigin().getV2() * -1);
                        bodyAnimator.currentAnimation().getCurrentFrame().setRotation(current.getTank().getTankState().getDirection().getAngle());
                        g.drawAnimation(bodyAnimator.currentAnimation(), current.getTank().getTankState().getGraphicalX(), current.getTank().getTankState().getGraphicalY(),
                                bodyAnimator.getFilter());
                    }
                }
                if (topAnimator != null && topAnimator.isPrintable()) {
                    topAnimator.currentAnimation().getCurrentFrame().setCenterOfRotation(current.getTank().getTankWeapon().getShiftWeaponOrigin().getV1() * -1, -1 * current.getTank().getTankWeapon().getShiftWeaponOrigin().getV2());
                    topAnimator.currentAnimation().getCurrentFrame().setRotation(current.getTank().getTankState().getGunAngle());
                    g.drawAnimation(topAnimator.currentAnimation(), current.getTank().getTankWeapon().getGraphicalX(current.getTank().getTankState().getPositions().getV1()),
                            current.getTank().getTankWeapon().getGraphicalY(current.getTank().getTankState().getPositions().getV2()),
                            topAnimator.getFilter());
                }
                if (spellAnimator != null && spellAnimator.isPrintable() && current.getTank().getTankSpell().isActivate() && spellAnimator.currentAnimation().isStopped() == false) {
                    spellAnimator.currentAnimation().getCurrentFrame().setCenterOfRotation(current.getTank().getTankState().getShiftOrigin().getV1() * -1,
                            current.getTank().getTankState().getShiftOrigin().getV2() * -1);
                    spellAnimator.currentAnimation().getCurrentFrame().setRotation(current.getTank().getTankState().getDirection().getAngle());
                    g.drawAnimation(spellAnimator.currentAnimation(), current.getTank().getTankState().getGraphicalX(), current.getTank().getTankState().getGraphicalY(),
                            spellAnimator.getFilter());
                }
            }
        }
    }

    public void drawGameShots(Graphics g) {

        for (int i = 0; i < this.shots.size(); ++i) {
            Shot current = this.shots.get(i);
            if (current.getAnimator().isDeleted()) {
                this.collisionController.deleteCollisionObject(this.shots.get(i).getId());
                this.shots.remove(i);
            } else if (current.isInMaxRange()) {
                current.setCurrentLife(0);
                this.setChanged();
                this.notifyObservers(TaskFactory.createTask(EnumTargetTask.GAME, EnumTargetTask.MESSAGE_SERVER, new MessageDeleteObject(CurrentUser.getPseudo(), CurrentUser.getId(), current.getId())));
            } else {
                current.draw(g);

                if (GlobalVariable.debug == true) {
                    g.setColor(Color.black);
                    g.drawRect(current.getX(), current.getY(), 1, 1);
                    g.setColor(Color.blue);
                    g.drawRect(current.getGraphicalX(), current.getGraphicalY(), 1, 1);
                    g.setColor(Color.red);
                }
            }
        }
    }


    public void drawGameMap(Graphics g) {
        if (this.mapController != null) {
            g.drawAnimation(this.mapController.getMapAnimator().currentAnimation(), 0, 0);
            for (int i = 0; i < this.mapController.getObstacles().size(); ++i) {
                Obstacle current = this.mapController.getObstacles().get(i);

                if (current.getAnimator() != null) {
                    if (current.getAnimator().isDeleted()) {
                        if (current.getType() == EnumGameObject.SHIELD && CurrentUser.getId().equals(current.getPlayerId())) {
                            Pair order = new Pair<>(EnumOverlayElement.USER_SHIELD, new Pair<>("cutBody", current.getPercentageLife()));
                            this.setChanged();
                            this.notifyObservers(TaskFactory.createTask(EnumTargetTask.GAME, EnumTargetTask.GAME_OVERLAY, new Pair<>(EnumOverlayElement.GENERIC_USER_STAT, order)));
                        }
                        this.mapController.deleteObstacle(current.getId());
                    } else {
                        current.getAnimator().currentAnimation().getCurrentFrame().setCenterOfRotation(current.getShiftOrigin().getV1() * -1, current.getShiftOrigin().getV2() * -1);
                        current.getAnimator().currentAnimation().getCurrentFrame().setRotation(current.getAngle());
                        g.drawAnimation(current.getAnimator().currentAnimation(), current.getGraphicalX(), current.getGraphicalY());
                    }
                } else if (current.getCurrentLife() == 0) {
                    this.mapController.deleteObstacle(current.getId());
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

    // SETTERS
    public void setStateWindow(StateBasedGame stateWindow) {
        this.stateWindow = stateWindow;
    }
}

