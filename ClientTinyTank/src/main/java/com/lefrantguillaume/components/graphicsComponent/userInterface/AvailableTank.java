package com.lefrantguillaume.components.graphicsComponent.userInterface;

import com.lefrantguillaume.components.gameComponent.gameObject.EnumGameObject;
import com.lefrantguillaume.components.gameComponent.animations.Animator;
import com.lefrantguillaume.components.gameComponent.animations.EnumInterfaceComponent;
import org.newdawn.slick.Graphics;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andres_k on 20/03/2015.
 */
public class AvailableTank {
    private List<Animator> tankStatAnimator;
    private List<Animator> tankPreviewAnimator;
    private List<Boolean> available;
    private EnumInterfaceComponent currentTankStat;
    private final float xStat;
    private final float yStat;
    private final float xPreview;
    private final float yPreview;

    public AvailableTank() {
//TODO faire en sorte que les tanks disponible soit passé en paramètre (List<> available)
        this.xStat = 450;
        this.yStat = 450;
        this.xPreview = 500;
        this.yPreview = 300;
        this.available = new ArrayList<>();
        this.available.add(true);
        this.available.add(true);
        this.available.add(true);
        this.tankStatAnimator = new ArrayList<>();
        this.tankPreviewAnimator = new ArrayList<>();
        this.currentTankStat = EnumInterfaceComponent.SNIPER;
    }

    public void drawCurrentTankStat(Graphics g) {
        float sizex = tankPreviewAnimator.get(currentTankStat.getIndex()).currentSizeAnimation().getV1();
        float sizey = tankPreviewAnimator.get(currentTankStat.getIndex()).currentSizeAnimation().getV2();
        g.drawAnimation(tankStatAnimator.get(EnumInterfaceComponent.RANK.getIndex()).currentAnimation(), this.xStat, this.yStat);
        g.drawAnimation(tankStatAnimator.get(currentTankStat.getIndex()).currentAnimation(), this.xStat, this.yStat);
        tankPreviewAnimator.get(currentTankStat.getIndex()).currentAnimation().draw(this.xPreview, this.yPreview, 2 * sizex, 2 * sizey);
    }

    public void addTankStatAnimator(Animator tankStatAnimator) {
        this.tankStatAnimator.add(tankStatAnimator);
    }

    public void addTankPreviewAnimator(Animator tankPreviewAnimator) {
        this.tankPreviewAnimator.add(tankPreviewAnimator);
    }

    public void nextTankStat() {
        EnumInterfaceComponent[] values = {EnumInterfaceComponent.TIGER, EnumInterfaceComponent.SNIPER, EnumInterfaceComponent.RUSHER};
        int index = this.currentTankStat.getIndex();

        if (index == 2) {
            index = 0;
        } else {
            index += 1;
        }
        this.currentTankStat = values[index];
    }

    public void prevTankStat() {
        EnumInterfaceComponent[] values = {EnumInterfaceComponent.TIGER, EnumInterfaceComponent.SNIPER, EnumInterfaceComponent.RUSHER};
        int index = this.currentTankStat.getIndex();

        if (index == 0) {
            index = 2;
        } else {
            index -= 1;
        }
        this.currentTankStat = values[index];
    }

    public EnumGameObject getCurrentTank() {
        if (this.currentTankStat.getIndex() < this.available.size() && this.available.get(this.currentTankStat.getIndex()).equals(true)) {
            return EnumGameObject.getEnumByIndex(this.currentTankStat.getIndex());
        }
        return EnumGameObject.NULL;
    }
}
