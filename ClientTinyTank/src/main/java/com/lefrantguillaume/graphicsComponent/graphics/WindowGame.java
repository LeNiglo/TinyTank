package com.lefrantguillaume.graphicsComponent.graphics;

import com.lefrantguillaume.Utils.tools.Debug;
import com.lefrantguillaume.Utils.configs.CurrentUser;
import com.lefrantguillaume.Utils.tools.MathTools;
import com.lefrantguillaume.Utils.tools.StringTools;
import com.lefrantguillaume.collisionComponent.CollisionObject;
import com.lefrantguillaume.gameComponent.controllers.GameController;
import com.lefrantguillaume.gameComponent.gameObject.obstacles.Obstacle;
import com.lefrantguillaume.gameComponent.playerData.data.Player;
import com.lefrantguillaume.gameComponent.gameObject.projectiles.Shot;
import com.lefrantguillaume.gameComponent.animations.AnimatorGameData;
import com.lefrantguillaume.graphicsComponent.input.*;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
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
    private InputCheck input;

    private GameContainer container;
    private StateBasedGame stateGame;
    private int id;

    private long runningTime = 0l;
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
        return this.id;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        this.container = gameContainer;
        this.stateGame = stateBasedGame;
        gameContainer.setAlwaysRender(true);
        this.container.setForceExit(false);
        this.animatorGameData.initMap(this.gameController.getMapController().getConfigMapFile());
        this.animatorGameData.initGame();
        this.gameController.getMapController().setMapAnimator(this.animatorGameData.getMapAnimator());
        this.gameController.setAnimatorGameData(this.animatorGameData);
        try {
            JSONObject jsonConfig = new JSONObject(StringTools.readFile("tanks.json"));
            this.gameController.initTankConfigData(jsonConfig);
        } catch (JSONException e) {
            throw new SlickException(e.getMessage());
        }
    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics g) throws SlickException {
        if (this.gameController != null) {
            this.gameController.drawGameMap(g);
            this.gameController.drawGamePlayers(g);
            this.gameController.drawGameShots(g);

            // debug
            if (this.gameController.getCollisionController() != null) {
                for (int i = 0; i < this.gameController.getCollisionController().getCollisionObjects().size(); ++i) {
                    CollisionObject current = this.gameController.getCollisionController().getCollisionObjects().get(i);
                    Rectangle r = new Rectangle(current.getOriginX(), current.getOriginY(), current.getSizeX(), current.getSizeY());
                    Shape nr = r.transform(Transform.createRotateTransform(current.getRadian(), current.getX(), current.getY()));
                    g.draw(nr);
                }
            }
            //debug
            if (CurrentUser.isInGame()) {
                g.drawString(tmp, 100, 50);
                g.setColor(Color.red);
                g.drawRect(this.gameController.getPlayer(CurrentUser.getId()).getTank().getTankState().getGraphicalX() - 2, this.gameController.getPlayer(CurrentUser.getId()).getTank().getTankState().getGraphicalY() - 2, 5, 5);
                g.drawRect(this.gameController.getPlayer(CurrentUser.getId()).getTank().getTankState().getGraphicalX(), this.gameController.getPlayer(CurrentUser.getId()).getTank().getTankState().getGraphicalY(), 1, 1);
                g.drawRect(this.gameController.getPlayer(CurrentUser.getId()).getTank().getTankState().getX() - 2, this.gameController.getPlayer(CurrentUser.getId()).getTank().getTankState().getY() - 2, 5, 5);
                g.drawRect(this.gameController.getPlayer(CurrentUser.getId()).getTank().getTankState().getX(), this.gameController.getPlayer(CurrentUser.getId()).getTank().getTankState().getY(), 1, 1);
            }
        }
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int delta) throws SlickException {
        this.runningTime += delta;
        if (runningTime > 30) {
        /*debug*/
            Input input = gameContainer.getInput();
            int xpos = input.getMouseX();
            int ypos = input.getMouseY();
            tmp = "MouseSlick position x:" + xpos + " y:" + ypos;
        /**/

            if (this.gameController != null) {
                this.myMouseMoved(xpos, ypos);
                this.gameController.updateGame();
            }
            this.runningTime = 0;
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
        if (input != null && input.keyCheck(key, EnumInput.RELEASED) == -1 && this.gameController != null) {
            this.gameController.clearData();
            this.stateGame.enterState(EnumWindow.INTERFACE.getValue());
        }
    }

    @Override
    public void mousePressed(int button, int x, int y) {
        if (input != null && button == 0) {
            input.mouseClickCheck(this.gameController.getPlayer(CurrentUser.getId()), x, y, EnumInput.PRESSED);
        }
    }

    @Override
    public void mouseReleased(int button, int x, int y) {
        if (input != null && button == 0) {
            input.mouseClickCheck(this.gameController.getPlayer(CurrentUser.getId()), x, y, EnumInput.RELEASED);
        }
    }


    public void myMouseMoved(double newX, double newY) {
        if (CurrentUser.isInGame() && this.gameController != null) {
            double x = this.gameController.getPlayer(CurrentUser.getId()).getTank().getTankState().getX();
            double y = this.gameController.getPlayer(CurrentUser.getId()).getTank().getTankState().getY();

            float angle = MathTools.getAngle(x, y, newX, newY);
            float newAngle = angle;
            if (angle < 0)
                newAngle = 180 + (180 - (angle * -1));
            float minAngle = this.gameController.getPlayer(CurrentUser.getId()).getTank().getTankState().getDirection().getAngle() - 90;
            float maxAngle = this.gameController.getPlayer(CurrentUser.getId()).getTank().getTankState().getDirection().getAngle() + 90;

            minAngle = (minAngle < 0 ? 360 + minAngle : minAngle);
            maxAngle = (maxAngle > 360 ? maxAngle - 360 : maxAngle);
/*
            Debug.debug("minAngle: " + minAngle);
            Debug.debug("maxAngle: " + maxAngle);
            Debug.debug("newAngle: " + newAngle);
            */
            if (maxAngle < minAngle && ((newAngle >= 0 && newAngle <= maxAngle) || (newAngle >= minAngle && newAngle <= 360))) {
                this.gameController.getPlayer(CurrentUser.getId()).getTank().getTankState().setGunAngle(angle);
            } else if (newAngle >= minAngle && newAngle <= maxAngle) {
                this.gameController.getPlayer(CurrentUser.getId()).getTank().getTankState().setGunAngle(angle);
            } else if (newAngle < minAngle) {
                this.gameController.getPlayer(CurrentUser.getId()).getTank().getTankState().setGunAngle(minAngle);
            } else {
                this.gameController.getPlayer(CurrentUser.getId()).getTank().getTankState().setGunAngle(maxAngle);
            }
        }
    }
}
