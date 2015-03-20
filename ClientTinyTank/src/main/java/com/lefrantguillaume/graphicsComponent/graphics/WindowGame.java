package com.lefrantguillaume.graphicsComponent.graphics;

import com.lefrantguillaume.Utils.stockage.Pair;
import com.lefrantguillaume.Utils.tools.Debug;
import com.lefrantguillaume.Utils.configs.CurrentUser;
import com.lefrantguillaume.Utils.tools.MathTools;
import com.lefrantguillaume.collisionComponent.CollisionObject;
import com.lefrantguillaume.gameComponent.controllers.GameController;
import com.lefrantguillaume.gameComponent.controllers.MapController;
import com.lefrantguillaume.gameComponent.gameObject.obstacles.Obstacle;
import com.lefrantguillaume.gameComponent.playerData.data.Player;
import com.lefrantguillaume.gameComponent.gameObject.projectiles.Shot;
import com.lefrantguillaume.gameComponent.animations.AnimatorData;
import com.lefrantguillaume.gameComponent.gameObject.EnumType;
import com.lefrantguillaume.gameComponent.gameObject.tanks.TankFactory;
import com.lefrantguillaume.gameComponent.playerData.data.User;
import com.lefrantguillaume.graphicsComponent.input.*;
import org.newdawn.slick.*;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.util.List;
import java.util.Observer;
import java.util.UUID;


/**
 * Created by andres_k on 10/03/2015.
 */

public class WindowGame extends BasicGameState {
    private AnimatorData animatorData;
    private GameController gameController;
    private GameContainer container;
    private StateBasedGame stateGame;
    private InputCheck input;
    private int id;
    public String tmp = "no input";

    public WindowGame(int id, List<Observer> observers, Object gameController) {
        this.id = id;
        this.gameController = (GameController) gameController;
        this.animatorData = new AnimatorData();
        this.input = new InputCheck();
        for (int i = 0; i < observers.size(); ++i) {
            this.input.addObserver(observers.get(i));
        }
    }

    @Override
    public void mouseWheelMoved(int change) {
        System.out.println(change);
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        Debug.debug("init Game");
        this.container = gameContainer;
        this.stateGame = stateBasedGame;
        this.container.setForceExit(false);
        this.animatorData.initMap(this.gameController.getMapController().getConfigMapFile());
        this.animatorData.initGame();
        this.gameController.getMapController().setMapAnimator(this.animatorData.getMapAnimator());
        MapController map = this.gameController.getMapController();
        this.gameController.getCollisionController().addCollisionObject(new CollisionObject(true, 0, -5, new Pair<Float, Float>(map.getSizeX(), 5f), "admin", UUID.randomUUID(), EnumType.OBSTACLE, 0));
        this.gameController.getCollisionController().addCollisionObject(new CollisionObject(true, 0, map.getSizeY() + 5, new Pair<Float, Float>(map.getSizeX(), -5f), "admin", UUID.randomUUID(), EnumType.OBSTACLE, 0));
        this.gameController.getCollisionController().addCollisionObject(new CollisionObject(true, -5, 0, new Pair<Float, Float>(5f, map.getSizeY()), "admin", UUID.randomUUID(), EnumType.OBSTACLE, 0));
        this.gameController.getCollisionController().addCollisionObject(new CollisionObject(true, map.getSizeX() + 5, 0, new Pair<Float, Float>(-5f, map.getSizeY()), "admin", UUID.randomUUID(), EnumType.OBSTACLE, 0));
        //tmp
        this.gameController.addPlayer(new Player(new User(CurrentUser.getPseudo(), CurrentUser.getId()), null, TankFactory.createTank("panzer", this.animatorData), this.gameController.getShots(), 15, 15));

    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics g) throws SlickException {
        g.drawAnimation(this.gameController.getMapController().getMapAnimator().currentAnimation(), 0, 0);
        for (int i = 0; i < this.gameController.getMapController().getObstacles().size(); ++i) {
            Obstacle current = this.gameController.getMapController().getObstacles().get(i);
            g.drawAnimation(current.getAnimator().currentAnimation(), current.getX(), current.getY());
        }
        for (int i = 0; i < this.gameController.getShots().size(); ++i) {
            Shot current = this.gameController.getShots().get(i);
            if (current.getAnimator().currentAnimation().isStopped()) {
                this.gameController.getCollisionController().deleteCollisionObject(this.gameController.getShots().get(i).getId());
                this.gameController.getShots().remove(i);
            } else {
                current.getAnimator().currentAnimation().getCurrentFrame().setRotation(current.getAngle());
                g.drawAnimation(current.getAnimator().currentAnimation(), current.getX(), current.getY());
                Rectangle r = new Rectangle(this.gameController.getCollisionController().getCollisionObject(current.getId()).getX(),
                        this.gameController.getCollisionController().getCollisionObject(current.getId()).getY(),
                        this.gameController.getCollisionController().getCollisionObject(current.getId()).getSizeX(),
                        this.gameController.getCollisionController().getCollisionObject(current.getId()).getSizeY());
                Shape nr = r.transform(Transform.createRotateTransform(this.gameController.getCollisionController().getCollisionObject(current.getId()).getRadian(),
                        this.gameController.getCollisionController().getCollisionObject(current.getId()).getCenterX(),
                        this.gameController.getCollisionController().getCollisionObject(current.getId()).getCenterY()));
                g.draw(nr);
            }
        }
        for (int i = 0; i < this.gameController.getPlayers().size(); ++i) {
            Player current = this.gameController.getPlayers().get(i);
            current.getTank().getTankAnimator().currentAnimation().getCurrentFrame().setRotation(current.getPlayerState().getDirection().getAngle());
            g.drawAnimation(current.getTank().getTankAnimator().currentAnimation(), current.getPlayerState().getAbsoluteX(), current.getPlayerState().getAbsoluteY());
            current.getTank().getGunAnimator().currentAnimation().getCurrentFrame().setRotation(current.getPlayerState().getGunAngle());
            g.drawAnimation(current.getTank().getGunAnimator().currentAnimation(), current.getPlayerState().getAbsoluteX(), current.getPlayerState().getAbsoluteY());

            Rectangle r = new Rectangle(this.gameController.getCollisionController().getCollisionObject(current.getPlayerState().getUser().getId()).getX(),
                    this.gameController.getCollisionController().getCollisionObject(current.getPlayerState().getUser().getId()).getY(),
                    this.gameController.getCollisionController().getCollisionObject(current.getPlayerState().getUser().getId()).getSizeX(),
                    this.gameController.getCollisionController().getCollisionObject(current.getPlayerState().getUser().getId()).getSizeY());
            Shape nr = r.transform(Transform.createRotateTransform(this.gameController.getCollisionController().getCollisionObject(current.getPlayerState().getUser().getId()).getRadian(),
                    this.gameController.getCollisionController().getCollisionObject(current.getPlayerState().getUser().getId()).getCenterX(),
                    this.gameController.getCollisionController().getCollisionObject(current.getPlayerState().getUser().getId()).getCenterY()));
            g.draw(nr);
        }
        g.drawString(tmp, 100, 50);
        g.drawRect(this.gameController.getPlayer(CurrentUser.getId()).getPlayerState().getCenterX() - 2, this.gameController.getPlayer(CurrentUser.getId()).getPlayerState().getCenterY() - 2, 5, 5);
        g.drawRect(this.gameController.getPlayer(CurrentUser.getId()).getPlayerState().getCenterX(), this.gameController.getPlayer(CurrentUser.getId()).getPlayerState().getCenterY(), 1, 1);
        g.drawRect(this.gameController.getPlayer(CurrentUser.getId()).getPlayerState().getAbsoluteX() - 2, this.gameController.getPlayer(CurrentUser.getId()).getPlayerState().getAbsoluteY() - 2, 5, 5);
        g.drawRect(this.gameController.getPlayer(CurrentUser.getId()).getPlayerState().getAbsoluteX(), this.gameController.getPlayer(CurrentUser.getId()).getPlayerState().getAbsoluteY(), 1, 1);
        g.setColor(Color.red);
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int delta) throws SlickException {
        Input input = gameContainer.getInput();
        int xpos = input.getMouseX();
        int ypos = input.getMouseY();
        tmp = "MouseSlick position x:" + xpos + " y:" + ypos;
        this.myMouseMoved(xpos, ypos);
        for (int i = 0; i < this.gameController.getPlayers().size(); ++i) {
            if (this.gameController.getPlayers().get(i).getPlayerState().isMove()) {
                if (!this.gameController.getCollisionController().checkCollision(this.gameController.getPlayers().get(i).movePredict(delta), this.gameController.getPlayers().get(i).getPlayerState().getUser().getId()))
                    this.gameController.getPlayers().get(i).move(delta);
            }
        }
        for (int i = 0; i < this.gameController.getShots().size(); ++i) {
            if (!this.gameController.getShots().get(i).getExplode())
                if (!this.gameController.getCollisionController().checkCollision(this.gameController.getShots().get(i).movePredict(delta), this.gameController.getShots().get(i).getId()))
                    this.gameController.getShots().get(i).move(delta);
        }
    }

    @Override
    public void keyPressed(int key, char c) {
        input.keyCheck(key, EnumInput.PRESSED);
    }

    @Override
    public void keyReleased(int key, char c) {
        if (input.keyCheck(key, EnumInput.RELEASED) == -1) {
            this.stateGame.enterState(EnumWindow.HOME.getValue());
        }
    }

    @Override
    public void mousePressed(int button, int x, int y) {
        if (button == 0) {
            input.mouseClickCheck(this.gameController.getPlayer(CurrentUser.getId()).getPlayerState(), x, y, EnumInput.PRESSED);
        }
    }

    @Override
    public void mouseReleased(int button, int x, int y) {
        if (button == 0) {
            input.mouseClickCheck(this.gameController.getPlayer(CurrentUser.getId()).getPlayerState(), x, y, EnumInput.RELEASED);
        }
    }


    public void myMouseMoved(double newX, double newY) {
        double x = this.gameController.getPlayer(CurrentUser.getId()).getPlayerState().getCenterX();
        double y = this.gameController.getPlayer(CurrentUser.getId()).getPlayerState().getCenterY();

        float angle = MathTools.getAngle(x, y, newX, newY);
        this.gameController.getPlayer(CurrentUser.getId()).getPlayerState().setGunAngle(angle);
    }
}
