package com.lefrantguillaume.components.graphicsComponent.graphics.windowGame;

import com.lefrantguillaume.components.collisionComponent.CollisionObject;
import com.lefrantguillaume.components.gameComponent.animations.AnimatorGameData;
import com.lefrantguillaume.components.gameComponent.animations.AnimatorOverlayData;
import com.lefrantguillaume.components.gameComponent.controllers.GameController;
import com.lefrantguillaume.components.gameComponent.gameObject.tanks.equipment.TankState;
import com.lefrantguillaume.components.gameComponent.playerData.action.EnumDirection;
import com.lefrantguillaume.components.graphicsComponent.graphics.EnumWindow;
import com.lefrantguillaume.components.graphicsComponent.graphics.WindowBasedGame;
import com.lefrantguillaume.components.graphicsComponent.input.EnumInput;
import com.lefrantguillaume.components.graphicsComponent.input.InputData;
import com.lefrantguillaume.components.graphicsComponent.input.InputGame;
import com.lefrantguillaume.components.graphicsComponent.userInterface.overlay.windowOverlay.GameOverlay;
import com.lefrantguillaume.components.graphicsComponent.userInterface.overlay.Overlay;
import com.lefrantguillaume.components.taskComponent.GenericSendTask;
import com.lefrantguillaume.utils.configs.CurrentUser;
import com.lefrantguillaume.utils.configs.GlobalVariable;
import com.lefrantguillaume.utils.stockage.Pair;
import com.lefrantguillaume.utils.tools.MathTools;
import com.lefrantguillaume.utils.tools.StringTools;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.state.StateBasedGame;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by andres_k on 10/03/2015.
 */

public class WindowGame extends WindowBasedGame implements ScreenController {
    private AnimatorGameData animatorGameData;
    private AnimatorOverlayData animatorOverlayData;
    private GameController gameController;
    private Overlay gameOverlay;
    private InputGame input;

    private GameContainer container;
    private StateBasedGame stateWindow;
    private Nifty nifty;
    private int id;
    private boolean debug = false;

    private int frameRate = 60;
    private long runningTime = 0l;

    List<Pair<Integer, Integer>> mousePos = new ArrayList<>();

    public WindowGame(int id, Nifty nifty, GenericSendTask inputTask, GenericSendTask gameTask) throws JSONException, SlickException {
        this.id = id;
        this.nifty = nifty;

        InputData inputData = new InputData("configInput.json");
        this.input = new InputGame(inputData);
        this.input.addObserver(inputTask);

        this.gameController = new GameController();
        this.animatorGameData = new AnimatorGameData();
        this.animatorOverlayData = new AnimatorOverlayData();
        this.gameOverlay = new GameOverlay(inputData);


        gameTask.addObserver(this.gameController);
        this.gameController.addObserver(gameTask);
        gameTask.addObserver(this.gameOverlay);
        this.gameOverlay.addObserver(gameTask);
    }

    @Override
    public int getID() {
        return this.id;
    }

    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        this.container = gameContainer;
        this.stateWindow = stateBasedGame;
        this.container.setForceExit(false);

        this.animatorGameData.initMap(this.gameController.getMapController().getConfigMapFile());
        this.animatorGameData.init();

        this.gameController.getMapController().setMapAnimator(this.animatorGameData.getMapAnimator());
        this.gameController.setAnimatorGameData(this.animatorGameData);
        this.gameController.setStateWindow(this.stateWindow);

        this.animatorOverlayData.init();
        this.gameOverlay.initElementsComponent(this.animatorOverlayData);

        try {
            JSONObject tanksConfig = new JSONObject(StringTools.readFile("tanks.json"));
            JSONObject obstaclesConfig = new JSONObject(StringTools.readFile("obstacles.json"));
            this.gameController.initConfigData(tanksConfig, obstaclesConfig);
        } catch (JSONException e) {
            e.printStackTrace();
            throw new SlickException(e.getMessage());
        }
    }

    @Override
    public void enter(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        this.container.setTargetFrameRate(this.frameRate);
        this.container.setShowFPS(true);
        this.container.setAlwaysRender(true);
        this.container.setVSync(false);


        this.nifty.gotoScreen("screen-game");
        // this.nifty.getNiftyMouse().enableMouseCursor("crosshair");
    }

    @Override
    public void leave(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        this.clean();
    }

    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics g) throws SlickException {
        if (this.gameController != null) {
            this.gameController.drawGameMap(g);
            this.gameController.drawGamePlayers(g);
            this.gameController.drawGameShots(g);

            if (GlobalVariable.debug == true) {
                // debug
                if (this.gameController.getCollisionController() != null) {
                    g.setColor(Color.red);
                    for (int i = 0; i < this.gameController.getCollisionController().getCollisionObjects().size(); ++i) {
                        CollisionObject current = this.gameController.getCollisionController().getCollisionObjects().get(i);
                        if (current.isAlive()) {
                            g.draw(current.getShape());
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
                    g.draw(new Circle(this.gameController.getPlayer(CurrentUser.getId()).getTank().getTankState().getX(), this.gameController.getPlayer(CurrentUser.getId()).getTank().getTankState().getY(), this.gameController.getPlayer(CurrentUser.getId()).getTank().getTankWeapon().getMaxRangeShot()));
                }
                g.setColor(Color.darkGray);
                for (Pair<Integer, Integer> pos : this.mousePos) {
                    g.drawRoundRect(pos.getV1() - 1, pos.getV2() - 1, 3, 3, 50);
                }
            }
        }
        this.gameOverlay.draw(g);
    }

    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int delta) throws SlickException {
        this.runningTime += delta;
        Input input = gameContainer.getInput();
        int xpos = input.getMouseX();
        int ypos = input.getMouseY();

        if (runningTime > 10) {
            if (this.mousePos.size() > 10) {
                this.mousePos.remove(0);
            }
            this.mousePos.add(new Pair<>(xpos, ypos));
        }
        if (runningTime > 30) {
            this.gameOverlay.updateOverlay();
            this.gameOverlay.sliderMove(xpos, ypos);
            this.myMouseMoved(xpos, ypos);
            this.gameController.updateGame(1);//(((float) delta / 15) < 1 ? 1 : ((float) delta / 15)));

            this.runningTime = 0;
        }
    }

    @Override
    public void mouseWheelMoved(int change) {
    }

    @Override
    public void keyPressed(int key, char c) {
        boolean absorbed = this.gameOverlay.event(key, c, EnumInput.PRESSED);
        if (absorbed == false) {
            if (this.input != null && this.gameController != null) {
                this.input.checkInput(this.gameController, key, EnumInput.PRESSED, this.container.getInput().getMouseX(), this.container.getInput().getMouseY());
            }
        }
    }

    @Override
    public void keyReleased(int key, char c) {
        boolean absorbed = this.gameOverlay.event(key, c, EnumInput.RELEASED);
        if (absorbed == false) {
            if (this.input != null && this.gameController != null) {
                int result = this.input.checkInput(this.gameController, key, EnumInput.RELEASED, this.container.getInput().getMouseX(), this.container.getInput().getMouseY());
                this.gameOverlay.doTask(result);
            }
        }
    }

    @Override
    public void mousePressed(int button, int x, int y) {
        if (this.input != null && this.gameController != null) {
            if (button == 0) {
                this.input.checkInput(this.gameController, -2, EnumInput.PRESSED, x, y);
            } else if (button == 1) {
                this.input.checkInput(this.gameController, -3, EnumInput.PRESSED, x, y);
            }
        }
    }

    @Override
    public void mouseReleased(int button, int x, int y) {
        if (!this.gameOverlay.isOnFocus(x, y)) {
            if (this.input != null && button == 0) {
                this.input.checkInput(this.gameController, -2, EnumInput.RELEASED, x, y);
            } else if (this.input != null && button == 1) {
                this.input.checkInput(this.gameController, -3, EnumInput.RELEASED, x, y);
            }
        }
    }


    public void myMouseMoved(double newX, double newY) throws SlickException {
        if (CurrentUser.isInGame() && this.gameController != null && this.gameController.getPlayer(CurrentUser.getId()).isCanDoAction()) {
            TankState ts = this.gameController.getPlayer(CurrentUser.getId()).getTank().getTankState();

            double x = ts.getX();
            double y = ts.getY();

            float angle = MathTools.getAngle(x, y, newX, newY);
            float tmpAngle;

            tmpAngle = ts.getDirection().getAngle() - 75;
            float minAngle = ((tmpAngle < -180 ? tmpAngle + 360 : tmpAngle) > 180 ? tmpAngle - 360 : tmpAngle);

            tmpAngle = ts.getDirection().getAngle() + 75;
            float maxAngle = ((tmpAngle < -180 ? tmpAngle + 360 : tmpAngle) > 180 ? tmpAngle - 360 : tmpAngle);
            //TODO Change 75 with a tank variable (getTankState().rotationAngle).

            if (ts.getDirection() == EnumDirection.DOWN) {

                if ((angle >= minAngle && angle <= maxAngle) || (angle >= minAngle && angle <= maxAngle)) {
                    ts.setGunAngle(angle);
                } else if (angle >= -90 && angle <= minAngle) {
                    ts.setGunAngle(minAngle);
                } else if (angle >= maxAngle || angle <= -90) {
                    ts.setGunAngle(maxAngle);
                } else {
                    throw new SlickException("WHAT IS THIS CASE FOR DOWN ? angle = " + angle);
                }

            } else if (ts.getDirection() == EnumDirection.RIGHT) {

                if (angle >= minAngle && angle <= maxAngle) {
                    ts.setGunAngle(angle);
                } else if (angle <= minAngle) {
                    ts.setGunAngle(minAngle);
                } else if (angle <= 180 && angle >= maxAngle) {
                    ts.setGunAngle(maxAngle);
                } else {
                    throw new SlickException("WHAT IS THIS CASE FOR RIGHT ? angle = " + angle);
                }

            } else if (ts.getDirection() == EnumDirection.UP) {

                if (angle >= minAngle && angle <= maxAngle) {
                    ts.setGunAngle(angle);
                } else if (angle >= maxAngle && angle <= 90) {
                    ts.setGunAngle(maxAngle);
                } else if ((angle >= 90 && angle <= 180) || (angle <= minAngle)) {
                    ts.setGunAngle(minAngle);
                } else {
                    throw new SlickException("WHAT IS THIS CASE FOR UP ? angle = " + angle);
                }

            } else if (ts.getDirection() == EnumDirection.LEFT) {

                if (angle >= minAngle || angle <= maxAngle) {
                    ts.setGunAngle(angle);
                } else if (angle >= maxAngle && angle <= 0) {
                    ts.setGunAngle(maxAngle);
                } else if (angle >= 0 && angle <= minAngle) {
                    ts.setGunAngle(minAngle);
                } else {
                    throw new SlickException("WHAT IS THIS CASE FOR LEFT ? angle = " + angle);
                }

            } else {
                throw new SlickException("Your direction isn't real");
            }

        }
    }

    // NIFTY
    @Override
    public void bind(Nifty nifty, Screen screen) {
    }

    @Override
    public void onStartScreen() {
    }

    @Override
    public void onEndScreen() {
    }

    @Override
    public void clean() {
        this.gameController.clearData();
        this.nifty.getNiftyMouse().resetMouseCursor();
        this.gameOverlay.leave();
    }

    @Override
    public void quit() {
        this.clean();
        this.stateWindow.enterState(EnumWindow.INTERFACE.getValue());
    }
}
