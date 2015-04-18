package com.lefrantguillaume.gameComponent.controllers;

import com.lefrantguillaume.Utils.configs.CurrentUser;
import com.lefrantguillaume.Utils.configs.MasterConfig;
import com.lefrantguillaume.Utils.stockage.Pair;
import com.lefrantguillaume.Utils.tools.Debug;
import com.lefrantguillaume.Utils.tools.Rectangle;
import com.lefrantguillaume.collisionComponent.CollisionController;
import com.lefrantguillaume.collisionComponent.CollisionObject;
import com.lefrantguillaume.gameComponent.RoundData.RoundController;
import com.lefrantguillaume.gameComponent.animations.AnimatorGameData;
import com.lefrantguillaume.gameComponent.gameObject.obstacles.Obstacle;
import com.lefrantguillaume.gameComponent.gameObject.tanks.tools.TankConfigData;
import com.lefrantguillaume.gameComponent.playerData.data.Player;
import com.lefrantguillaume.gameComponent.playerData.action.PlayerAction;
import com.lefrantguillaume.gameComponent.gameObject.projectiles.Shot;
import com.lefrantguillaume.gameComponent.gameObject.EnumType;
import com.lefrantguillaume.gameComponent.RoundData.Team;
import com.lefrantguillaume.gameComponent.playerData.data.User;
import com.lefrantguillaume.networkComponent.messages.MessageModel;
import com.lefrantguillaume.networkComponent.messages.msg.*;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

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

    public GameController() throws SlickException {
        this.players = new ArrayList<Player>();
        this.shots = new ArrayList<Shot>();
        this.teams = new ArrayList<Team>();
        this.animatorGameData = null;
        this.roundController = new RoundController(this.players, this.teams);
        this.mapController = new MapController(this.collisionController, MasterConfig.getMapConfigFile());
        this.collisionController = new CollisionController(this.mapController);
        this.tankConfigData = new TankConfigData();
    }

    public void clearData() {
        this.players.clear();
        this.shots.clear();
        this.teams.clear();
        this.collisionController.clearCollisionObjects();
        this.mapController.clearObstacles();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof MessageModel) {
            MessageModel received = (MessageModel) arg;
            if (received.getPlayerAction() == true) {
                Player current = this.getPlayer(received.getId());
                if (current != null) {
                    current.doAction(new PlayerAction(received), this.collisionController);
                }
            } else {
                if (received instanceof MessagePlayerNew) {
                    MessagePlayerNew task = (MessagePlayerNew) received;
                    if (this.animatorGameData != null && this.tankConfigData.isValid()) {
                        Debug.debug("NEW PLAYER");
                        this.addPlayer(new Player(new User(task.getPseudo(), task.getId()), null, this.tankConfigData.getTank(task.getEnumTanks()), this.getShots(), 50, 50));
                        if (task.getId().equals(CurrentUser.getId())) {
                            CurrentUser.setInGame(true);
                            this.collisionController.createWorld(this.mapController);
                        } else {
                            Debug.debug("My position send -> [" + this.getPlayer(CurrentUser.getId()).getTank().getTankState().getX() + "," +
                                    this.getPlayer(CurrentUser.getId()).getTank().getTankState().getY() + "]");
                            MessageModel request = new MessagePlayerUpdatePosition(CurrentUser.getPseudo(), CurrentUser.getId(),
                                    this.getPlayer(CurrentUser.getId()).getTank().getTankState().getX(),
                                    this.getPlayer(CurrentUser.getId()).getTank().getTankState().getY());
                            this.setChanged();
                            this.notifyObservers(request);
                        }
                    }
                }
                if (received instanceof MessagePlayerDelete) {
                    Debug.debug("DELETE PLAYER");
                    MessagePlayerDelete task = (MessagePlayerDelete) received;
                    this.deletePlayer(task.getId());
                }
                if (received instanceof MessagePlayerUpdateState) {
                    Debug.debug("UPDATE PLAYER");
                    MessagePlayerUpdateState task = (MessagePlayerUpdateState) received;
                    this.changeStatePlayer(task);
                }
                if (received instanceof MessagePlayerUpdatePosition) {
                    Debug.debug("UPDATE POS PLAYER");
                    MessagePlayerUpdatePosition task = (MessagePlayerUpdatePosition) received;
                    this.changePositionPlayer(task);
                }
            }
        }
    }

    // CHANGE FUNCTIONS
    public void addPlayer(Player player) {
        Debug.debug("add player: [" + player.getTank().getTankState().getX() + "," + player.getTank().getTankState().getY() + "] ?= [" +
                player.getTank().getTankState().getGraphicalX() + "," + player.getTank().getTankState().getGraphicalY() + "]");

        for (int i = 0; i < player.getTank().getTankState().getCollisionObject().size(); ++i) {
            Rectangle current = player.getTank().getTankState().getCollisionObject().get(i);

            Debug.debug("originCol:" + current.getShiftOrigin().toString());
            Debug.debug("pos:" + player.getTank().getTankState().getPositions().toString());
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
        for (int i = 0; i < this.players.size(); ++i) {
            if (this.players.get(i).getUser().getIdUser().equals(task.getId())) {
                this.players.remove(i);
                this.players.get(i).getTank().getTankState().setCurrentLife(task.getCurrentLife());
                this.players.get(i).getTank().getTankState().setBoostEffect(task.getBoostEffect());
                this.players.get(i).getTank().getTankState().setShieldEffect(task.getShieldEffect());
                this.players.get(i).getTank().getTankState().setSlowEffect(task.getSlowEffect());
                this.players.get(i).getTank().getTankState().setArmor(task.getArmor());
                this.players.get(i).setShots(this.shots);
                break;
            }
        }
    }

    public void changePositionPlayer(MessagePlayerUpdatePosition task) {
        Debug.debug("new pos [" + task.getX() + "," + task.getY() + "] : id=" + task.getId());
        for (int i = 0; i < this.players.size(); ++i) {
            if (this.players.get(i).getUser().getIdUser().equals(task.getId())) {
                this.players.get(i).getTank().getTankState().setX(task.getX());
                this.players.get(i).getTank().getTankState().setY(task.getY());
                List<CollisionObject> objects = this.collisionController.getCollisionObject(this.players.get(i).getUser().getId());
                for (int i2 = 0; i2 < objects.size(); ++i2) {
                    objects.get(i2).setX(task.getX());
                    objects.get(i2).setY(task.getY());
                }
                break;
            }
        }
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

    public void initTankConfigData(JSONObject config) throws JSONException {
        if (this.animatorGameData == null)
            throw new JSONException("tankConfigData failed");
        this.tankConfigData.initTanks(config, this.animatorGameData);
    }

    // UPDATE GAME FUNCTIONS
    public void updateGame(int delta) {
        Pair<String, String> impactIds;

        delta = 1;
        if (this.collisionController != null) {
            this.collisionController.cleanCollision();
        }
        for (int i = 0; i < this.players.size(); ++i) {
            if (this.players.get(i).getTank().getTankState().isMove()) {
                if (this.collisionController.checkCollision(this.players.get(i).movePredict(delta), this.players.get(i).getUser().getId()) == null) {
                    this.players.get(i).move(delta);
                }
            }
        }
        for (int i = 0; i < this.shots.size(); ++i) {
            if (!this.shots.get(i).getExplode()) {
                if ((impactIds = this.collisionController.checkCollision(this.shots.get(i).movePredict(delta), this.shots.get(i).getId())) == null) {
                    this.shots.get(i).move(delta);
                } else {
                    MessageModel request = new MessageCollision(CurrentUser.getPseudo(), CurrentUser.getId(), impactIds.getV1(), impactIds.getV2());
                    this.setChanged();
                    this.notifyObservers(request);
                }
            }
        }
    }

    // DRAW FUNCTIONS
    public void drawGamePlayers(Graphics g) {
        for (int i = 0; i < this.players.size(); ++i) {
            Player current = this.players.get(i);
            current.getTank().getTankAnimator().currentAnimation().getCurrentFrame().setCenterOfRotation(current.getTank().getTankState().getShiftOrigin().getV1() * -1,
                    current.getTank().getTankState().getShiftOrigin().getV2() * -1);
            current.getTank().getTankAnimator().currentAnimation().getCurrentFrame().setRotation(current.getTank().getTankState().getDirection().getAngle());
            g.drawAnimation(current.getTank().getTankAnimator().currentAnimation(), current.getTank().getTankState().getGraphicalX(), current.getTank().getTankState().getGraphicalY());

            current.getTank().getTopAnimator().currentAnimation().getCurrentFrame().setCenterOfRotation(current.getTank().getTankWeapon().getShiftWeaponOrigin().getV1() * -1, -1 * current.getTank().getTankWeapon().getShiftWeaponOrigin().getV2());
            current.getTank().getTopAnimator().currentAnimation().getCurrentFrame().setRotation(current.getTank().getTankState().getGunAngle());
            g.drawAnimation(current.getTank().getTopAnimator().currentAnimation(), current.getTank().getTankWeapon().getGraphicalX(current.getTank().getTankState().getPositions().getV1()),
                    current.getTank().getTankWeapon().getGraphicalY(current.getTank().getTankState().getPositions().getV2()));
        }
    }

    public void drawGameShots(Graphics g) {

        for (int i = 0; i < this.shots.size(); ++i) {
            Shot current = this.shots.get(i);
            if (current.getAnimator().currentAnimation().isStopped()) {
                this.collisionController.deleteCollisionObject(this.shots.get(i).getId());
                this.shots.remove(i);
            } else {
                current.getAnimator().currentAnimation().getCurrentFrame().setCenterOfRotation(current.getShiftOrigin().getV1() * -1, -1 * current.getShiftOrigin().getV2());
                current.getAnimator().currentAnimation().getCurrentFrame().setRotation(current.getAngle());
                g.drawAnimation(current.getAnimator().currentAnimation(), current.getGraphicalX(), current.getGraphicalY());

                // debug
                g.setColor(Color.black);
                g.drawRect(current.getX(), current.getY(), 1, 1);
                g.setColor(Color.red);
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
