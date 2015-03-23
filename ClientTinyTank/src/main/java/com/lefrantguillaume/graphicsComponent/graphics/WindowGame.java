package com.lefrantguillaume.graphicsComponent.graphics;

import com.lefrantguillaume.Utils.tools.Debug;
import com.lefrantguillaume.Utils.configs.CurrentUser;
import com.lefrantguillaume.Utils.tools.MathTools;
import com.lefrantguillaume.collisionComponent.CollisionObject;
import com.lefrantguillaume.gameComponent.controllers.GameController;
import com.lefrantguillaume.gameComponent.gameObject.obstacles.Obstacle;
import com.lefrantguillaume.gameComponent.playerData.data.Player;
import com.lefrantguillaume.gameComponent.gameObject.projectiles.Shot;
import com.lefrantguillaume.gameComponent.animations.AnimatorGameData;
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


/**
 * Created by andres_k on 10/03/2015.
 */

public class WindowGame extends BasicGameState {
    private AnimatorGameData animatorGameData;
    private GameController gameController;
    private GameContainer container;
    private StateBasedGame stateGame;
    private InputCheck input;
    private int id;
    public String tmp = "no input";

    public WindowGame(int id, List<Observer> observers, Object gameController) {
        this.id = id;
        this.gameController = (GameController) gameController;
        this.animatorGameData = new AnimatorGameData();
        this.input = new InputCheck();
        for (int i = 0; i < observers.size(); ++i) {
            this.input.addObserver(observers.get(i));
            this.gameController.addObserver(observers.get(i));
        }
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
        gameContainer.setAlwaysRender(true);
        this.container.setForceExit(false);
        this.animatorGameData.initMap(this.gameController.getMapController().getConfigMapFile());
        this.animatorGameData.initGame();
        this.gameController.getMapController().setMapAnimator(this.animatorGameData.getMapAnimator());
        this.gameController.setAnimatorGameData(this.animatorGameData);
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
                g.drawAnimation(current.getAnimator().currentAnimation(), current.getGraphicalX(), current.getGraphicalY());

                g.setColor(Color.black);
                g.drawRect(current.getX(), current.getY(), 1, 1);
                g.setColor(Color.red);
                g.drawRect(current.getGraphicalX(), current.getGraphicalY(), 1, 1);
                g.setColor(Color.red);
            }
        }
        for (int i = 0; i < this.gameController.getPlayers().size(); ++i) {
            Player current = this.gameController.getPlayers().get(i);
            current.getTank().getTankAnimator().currentAnimation().getCurrentFrame().setRotation(current.getPlayerState().getDirection().getAngle());
            g.drawAnimation(current.getTank().getTankAnimator().currentAnimation(), current.getPlayerState().getGraphicalX(), current.getPlayerState().getGraphicalY());
            current.getTank().getGunAnimator().currentAnimation().getCurrentFrame().setRotation(current.getPlayerState().getGunAngle());
            g.drawAnimation(current.getTank().getGunAnimator().currentAnimation(), current.getPlayerState().getGraphicalX(), current.getPlayerState().getGraphicalY());
        }
        for (int i = 0; i < this.gameController.getCollisionController().getCollisionObjects().size(); ++i){
            CollisionObject current = this.gameController.getCollisionController().getCollisionObjects().get(i);
            Rectangle r = new Rectangle(current.getX(), current.getY(), current.getSizeX(), current.getSizeY());
            Shape nr = r.transform(Transform.createRotateTransform(current.getRadian(), current.getCenterX(), current.getCenterY()));
            g.draw(nr);
        }
        //debug
        if (CurrentUser.isInGame()) {
            g.drawString(tmp, 100, 50);
            g.drawRect(this.gameController.getPlayer(CurrentUser.getId()).getPlayerState().getGraphicalX() - 2, this.gameController.getPlayer(CurrentUser.getId()).getPlayerState().getGraphicalY() - 2, 5, 5);
            g.drawRect(this.gameController.getPlayer(CurrentUser.getId()).getPlayerState().getGraphicalX(), this.gameController.getPlayer(CurrentUser.getId()).getPlayerState().getGraphicalY(), 1, 1);
            g.drawRect(this.gameController.getPlayer(CurrentUser.getId()).getPlayerState().getX() - 2, this.gameController.getPlayer(CurrentUser.getId()).getPlayerState().getY() - 2, 5, 5);
            g.drawRect(this.gameController.getPlayer(CurrentUser.getId()).getPlayerState().getX(), this.gameController.getPlayer(CurrentUser.getId()).getPlayerState().getY(), 1, 1);
            g.setColor(Color.red);
        }
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
                if (!this.gameController.getCollisionController().checkCollision(this.gameController.getPlayers().get(i).movePredict(delta, true), this.gameController.getPlayers().get(i).getPlayerState().getUser().getId()))
                    this.gameController.getPlayers().get(i).move(delta);
            }
        }
        for (int i = 0; i < this.gameController.getShots().size(); ++i) {
            if (!this.gameController.getShots().get(i).getExplode())
                if (!this.gameController.getCollisionController().checkCollision(this.gameController.getShots().get(i).movePredict(delta, true), this.gameController.getShots().get(i).getId()))
                    this.gameController.getShots().get(i).move(delta);
        }
    }

    @Override
    public void mouseWheelMoved(int change) {
        System.out.println(change);
    }

    @Override
    public void keyPressed(int key, char c) {
        input.keyCheck(key, EnumInput.PRESSED);
    }

    @Override
    public void keyReleased(int key, char c) {
        if (input.keyCheck(key, EnumInput.RELEASED) == -1) {
            this.gameController.clearData();
            this.stateGame.enterState(EnumWindow.INTERFACE.getValue());
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
        if (CurrentUser.isInGame()) {
            double x = this.gameController.getPlayer(CurrentUser.getId()).getPlayerState().getX();
            double y = this.gameController.getPlayer(CurrentUser.getId()).getPlayerState().getY();

            float angle = MathTools.getAngle(x, y, newX, newY);
            this.gameController.getPlayer(CurrentUser.getId()).getPlayerState().setGunAngle(angle);
        }
    }
}
