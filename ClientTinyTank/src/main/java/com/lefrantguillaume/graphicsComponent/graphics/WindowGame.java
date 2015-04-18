package com.lefrantguillaume.graphicsComponent.graphics;

import com.lefrantguillaume.Utils.Debug;
import com.lefrantguillaume.Utils.configs.WindowConfig;
import com.lefrantguillaume.collisionComponent.CollisionController;
import com.lefrantguillaume.collisionComponent.CollisionObject;
import com.lefrantguillaume.gameComponent.GameController;
import com.lefrantguillaume.gameComponent.Player;
import com.lefrantguillaume.gameComponent.actions.Shot;
import com.lefrantguillaume.gameComponent.animations.AnimatorData;
import com.lefrantguillaume.gameComponent.tanks.EnumTanks;
import com.lefrantguillaume.gameComponent.tanks.EnumType;
import com.lefrantguillaume.gameComponent.tanks.TankFactory;
import com.lefrantguillaume.graphicsComponent.input.*;
import org.newdawn.slick.*;
import org.newdawn.slick.tiled.TiledMap;

import java.util.List;
import java.util.Observer;


/**
 * Created by andres_k on 10/03/2015.
 */

public class WindowGame extends BasicGame {
    private AnimatorData animatorData;
    private GameController gameController;
    private GameContainer container;
    private TiledMap map;
    private InputCheck input;

    public WindowGame(List<Observer> observers, AnimatorData animatorData, Object gameController) {
        super("Game");
        this.gameController = (GameController) gameController;
        this.animatorData = animatorData;
        this.input = new InputCheck();
        for (int i = 0; i < observers.size(); ++i) {
            this.input.addObserver(observers.get(i));
        }
    }

    @Override
    public void init(GameContainer container) throws SlickException {
        this.container = container;
        this.container.setForceExit(false);
        this.map = new TiledMap("map/background.tmx");
        this.animatorData.initGame();
        this.gameController.addPlayer(new Player(null, TankFactory.createTank("panzer", this.animatorData), this.gameController.getShots(), 15, 15));
        this.gameController.getCollisionController().addCollisionObject(new CollisionObject(0, -5, WindowConfig.getSizeX(), 5, -11, -11, EnumType.OBSTACLE));
        this.gameController.getCollisionController().addCollisionObject(new CollisionObject(0, WindowConfig.getSizeY() + 5, WindowConfig.getSizeX(), -5, -12, -12, EnumType.OBSTACLE));
        this.gameController.getCollisionController().addCollisionObject(new CollisionObject(-5, 0, 5, WindowConfig.getSizeY(), -13, -13, EnumType.OBSTACLE));
        this.gameController.getCollisionController().addCollisionObject(new CollisionObject(WindowConfig.getSizeX() + 5, 0, -5, WindowConfig.getSizeY(), -14, -14, EnumType.OBSTACLE));
    }

    @Override
    public void keyPressed(int key, char c) {
        input.keyCheck(key, 1);
    }

    @Override
    public void keyReleased(int key, char c) {
        if (input.keyCheck(key, -1) == -1)
            container.exit();
    }

    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
        this.map.render(0, 0);
        for (int i = 0; i < this.gameController.getShots().size(); ++i) {
            Shot current = this.gameController.getShots().get(i);
            if (current.getAnimator().currentAnimation().isStopped()) {
                Debug.debug("removed");
                this.gameController.getShots().remove(i);
            } else {
                current.getAnimator().currentAnimation().getCurrentFrame().setRotation(current.getAngle());
                g.drawAnimation(current.getAnimator().currentAnimation(), current.getX(), current.getY());
            }
        }
        for (int i = 0; i < this.gameController.getPlayers().size(); ++i) {
            Player current = this.gameController.getPlayers().get(i);
            current.getTank().getTankAnimator().currentAnimation().getCurrentFrame().setRotation(current.getPlayerState().getDirection().getAngle());
            g.drawAnimation(current.getTank().getTankAnimator().currentAnimation(), current.getPlayerState().getX(), current.getPlayerState().getY());
        }
        g.fillRect(250, 250, 80, 80);
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
        for (int i = 0; i < this.gameController.getPlayers().size(); ++i) {
            if (this.gameController.getPlayers().get(i).getPlayerState().isMove()) {
                if (!this.gameController.getCollisionController().checkCollision(this.gameController.getPlayers().get(i).movePredict(delta), this.gameController.getPlayers().get(i).getPlayerState().getUserId()))
                    this.gameController.getPlayers().get(i).move(delta);
            }
        }
        for (int i = 0; i < this.gameController.getShots().size(); ++i) {
            if (!this.gameController.getShots().get(i).getExplode())
                if (!this.gameController.getCollisionController().checkCollision(this.gameController.getShots().get(i).movePredict(delta), this.gameController.getShots().get(i).getShotId()))
                    this.gameController.getShots().get(i).move(delta);
        }
    }
}
