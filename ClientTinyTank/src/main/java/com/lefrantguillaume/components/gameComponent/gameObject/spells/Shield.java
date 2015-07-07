package com.lefrantguillaume.components.gameComponent.gameObject.spells;

import com.lefrantguillaume.Utils.stockage.Pair;
import com.lefrantguillaume.components.gameComponent.gameObject.EnumGameObject;
import com.lefrantguillaume.components.gameComponent.gameObject.obstacles.Obstacle;
import com.lefrantguillaume.components.gameComponent.gameObject.obstacles.ObstacleConfigData;

import java.util.Timer;

/**
 * Created by andres_k on 18/03/2015.
 */
public class Shield extends Spell {
    Obstacle shield;
    ObstacleConfigData obstacleConfigData;

    public Shield(ObstacleConfigData obstacleConfigData) {
        this.type = EnumGameObject.SHIELD;
        this.obstacleConfigData = obstacleConfigData;
        this.animator = null;
        this.duration = 10000;
        this.isActive = false;
    }

    public Shield(Shield shield){
        this.type = EnumGameObject.SHIELD;
        this.obstacleConfigData = shield.obstacleConfigData;
        this.animator = null;
        this.duration = shield.duration;
        this.isActive = shield.isActive;
    }

    // FUNCTIONS

    @Override
    public Object activeSpell(){
        if (this.isActive == false) {
            this.isActive = true;
            this.shield = this.obstacleConfigData.getObstacle(EnumGameObject.SHIELD);
            this.timer = new Timer();
            this.timer.schedule(new myTask(), this.duration);
            return shield;
        }
        return null;
    }

    @Override
    protected int deleteSpell(){
        if (shield != null) {
            this.shield.setCurrentLife(0);
        }
        this.isActive = false;
        return 0;
    }

    @Override
    public void move(Pair<Float, Float> coords) {
        if (this.isActive == true && this.shield != null) {
            this.shield.move(coords);
        }
    }

}
