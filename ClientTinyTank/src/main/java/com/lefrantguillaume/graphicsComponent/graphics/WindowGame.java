package com.lefrantguillaume.graphicsComponent.graphics;

import com.lefrantguillaume.Utils.configs.CurrentUser;
import com.lefrantguillaume.Utils.tools.MathTools;
import com.lefrantguillaume.Utils.tools.StringTools;
import com.lefrantguillaume.collisionComponent.CollisionObject;
import com.lefrantguillaume.gameComponent.animations.AnimatorGameData;
import com.lefrantguillaume.gameComponent.controllers.GameController;
import com.lefrantguillaume.graphicsComponent.input.EnumInput;
import com.lefrantguillaume.graphicsComponent.input.InputCheck;
import de.lessvoid.nifty.Nifty;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.newdawn.slick.*;
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

    private int frameRate = 60;
    private float saveAngle = 0f;
    private long runningTime = 0l;

    public WindowGame(int id, Nifty nifty, List<Observer> observers, Object gameController) throws JSONException {
        this.id = id;
        this.gameController = (GameController) gameController;
        this.animatorGameData = new AnimatorGameData();
        this.input = new InputCheck(StringTools.readFile("configInput.json"));
        for (int i = 0; i < observers.size(); ++i) {
            this.input.addObserver(observers.get(i));
            this.gameController.addObserver(observers.get(i));
        }
    }

    @Override
    public int getID() {
        return this.id;
    }

    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        this.container = gameContainer;
        this.stateGame = stateBasedGame;
        this.container.setForceExit(false);
        this.animatorGameData.initMap(this.gameController.getMapController().getConfigMapFile());
        this.animatorGameData.initGame();
        this.gameController.getMapController().setMapAnimator(this.animatorGameData.getMapAnimator());
        this.gameController.setAnimatorGameData(this.animatorGameData);
        try {
            JSONObject jsonConfig = new JSONObject(StringTools.readFile("tanks.json"));
            this.gameController.initConfigData(jsonConfig);
        } catch (JSONException e) {
            throw new SlickException(e.getMessage());
        }
    }

    @Override
    public void enter(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        this.container.setTargetFrameRate(this.frameRate);
        this.container.setShowFPS(true);
        this.container.setAlwaysRender(true);
        this.container.setVSync(false);
    }

    @Override
    public void leave(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        this.gameController.clearData();
    }

    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics g) throws SlickException {
        if (this.gameController != null) {
            this.gameController.drawGameMap(g);
            this.gameController.drawGamePlayers(g);
            this.gameController.drawGameShots(g);

            // debug
            if (this.gameController.getCollisionController() != null) {
                for (int i = 0; i < this.gameController.getCollisionController().getCollisionObjects().size(); ++i) {
                    CollisionObject current = this.gameController.getCollisionController().getCollisionObjects().get(i);
                    if (current.isAlive()) {
                        Rectangle r = new Rectangle(current.getOriginX(), current.getOriginY(), current.getSizeX(), current.getSizeY());
                        Shape nr = r.transform(Transform.createRotateTransform(current.getRadian(), current.getX(), current.getY()));
                        g.draw(nr);
                    }
                }
            }
            //debug
            if (CurrentUser.isInGame()) {
                g.setColor(Color.red);
                g.drawRect(this.gameController.getPlayer(CurrentUser.getId()).getTank().getTankState().getGraphicalX() - 2, this.gameController.getPlayer(CurrentUser.getId()).getTank().getTankState().getGraphicalY() - 2, 5, 5);
                g.drawRect(this.gameController.getPlayer(CurrentUser.getId()).getTank().getTankState().getGraphicalX(), this.gameController.getPlayer(CurrentUser.getId()).getTank().getTankState().getGraphicalY(), 1, 1);
                g.drawRect(this.gameController.getPlayer(CurrentUser.getId()).getTank().getTankState().getX() - 2, this.gameController.getPlayer(CurrentUser.getId()).getTank().getTankState().getY() - 2, 5, 5);
                g.drawRect(this.gameController.getPlayer(CurrentUser.getId()).getTank().getTankState().getX(), this.gameController.getPlayer(CurrentUser.getId()).getTank().getTankState().getY(), 1, 1);
            }
        }
    }

    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int delta) throws SlickException {
        this.runningTime += delta;
//        Debug.debug("Delta=" + delta + "   frameRate:" + this.frameRate);
        if (runningTime > 30) {
            Input input = gameContainer.getInput();
            int xpos = input.getMouseX();
            int ypos = input.getMouseY();


            if (this.gameController != null) {
                this.myMouseMoved(xpos, ypos);
                this.gameController.updateGame(1);//(((float) delta / 15) < 1 ? 1 : ((float) delta / 15)));
            }
            this.runningTime = 0;
        }
    }

    @Override
    public void mouseWheelMoved(int change) {
    }

    @Override
    public void keyPressed(int key, char c) {
        if (key == Input.KEY_ADD) {
            this.frameRate += 5;
            this.container.setTargetFrameRate(this.frameRate);
        } else if (key == Input.KEY_SUBTRACT) {
            if (this.frameRate > 5) {
                this.frameRate -= 5;
                this.container.setTargetFrameRate(this.frameRate);
            }
        } else {
            input.checkInput(this.gameController, key, EnumInput.PRESSED, this.container.getInput().getMouseX(), this.container.getInput().getMouseY());
        }
    }

    @Override
    public void keyReleased(int key, char c) {
        if (input != null && this.gameController != null) {
            if (input.checkInput(this.gameController, key, EnumInput.RELEASED, this.container.getInput().getMouseX(), this.container.getInput().getMouseY()) == -1) {
                this.stateGame.enterState(EnumWindow.INTERFACE.getValue());
            }
        }
    }

    @Override
    public void mousePressed(int button, int x, int y) {
        if (input != null && button == 0) {
            input.checkInput(this.gameController, -2, EnumInput.PRESSED, x, y);
        } else if (input != null && button == 1) {
            input.checkInput(this.gameController, -3, EnumInput.PRESSED, x, y);
        }
    }

    @Override
    public void mouseReleased(int button, int x, int y) {
        if (input != null && button == 0) {
            input.checkInput(this.gameController, -2, EnumInput.RELEASED, x, y);
        } else if (input != null && button == 1) {
            input.checkInput(this.gameController, -3, EnumInput.RELEASED, x, y);
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
            //TODO Change 90 with a tank variable.

            minAngle = (minAngle < 0 ? 360 + minAngle : minAngle);
            maxAngle = (maxAngle > 360 ? maxAngle - 360 : maxAngle);

            if (maxAngle < minAngle && ((newAngle >= 0 && newAngle <= maxAngle) || (newAngle >= minAngle && newAngle <= 360))) {
                this.saveAngle = newAngle;
                this.gameController.getPlayer(CurrentUser.getId()).getTank().getTankState().setGunAngle(angle);
            } else if (newAngle >= minAngle && newAngle <= maxAngle) {
                this.saveAngle = newAngle;
                this.gameController.getPlayer(CurrentUser.getId()).getTank().getTankState().setGunAngle(angle);
            } else {
                this.gameController.getPlayer(CurrentUser.getId()).getTank().getTankState().setGunAngle(this.saveAngle);
            }
        }
    }
}
