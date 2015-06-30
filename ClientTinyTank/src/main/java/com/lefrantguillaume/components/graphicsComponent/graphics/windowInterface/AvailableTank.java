package com.lefrantguillaume.components.graphicsComponent.graphics.windowInterface;

import com.lefrantguillaume.components.gameComponent.animations.Animator;
import com.lefrantguillaume.components.gameComponent.gameObject.EnumGameObject;
import org.newdawn.slick.Graphics;

import java.util.HashMap;

/**
 * Created by andres_k on 20/03/2015.
 */
public class AvailableTank {
    private HashMap<EnumInterfaceElement, Animator> tankStatAnimator;
    private HashMap<EnumInterfaceElement, Animator> tankPreviewAnimator;
    private HashMap<EnumInterfaceElement, Boolean> available;
    private EnumInterfaceElement currentTankStat;
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
        this.available = new HashMap<>();
        this.available.put(EnumInterfaceElement.TIGER, true);
        this.available.put(EnumInterfaceElement.SNIPER, true);
        this.available.put(EnumInterfaceElement.RUSHER, true);
        this.tankStatAnimator = new HashMap<>();
        this.tankPreviewAnimator = new HashMap<>();
        this.currentTankStat = EnumInterfaceElement.SNIPER;
    }

    public void drawCurrentTankStat(Graphics g) {
        float sizex = this.tankPreviewAnimator.get(this.currentTankStat).currentSizeAnimation().getV1();
        float sizey = this.tankPreviewAnimator.get(this.currentTankStat).currentSizeAnimation().getV2();
        g.drawAnimation(this.tankStatAnimator.get(EnumInterfaceElement.RANK).currentAnimation(), this.xStat, this.yStat);
        g.drawAnimation(this.tankStatAnimator.get(this.currentTankStat).currentAnimation(), this.xStat, this.yStat);
        this.tankPreviewAnimator.get(this.currentTankStat).currentAnimation().draw(this.xPreview, this.yPreview, 2 * sizex, 2 * sizey);
    }

    public void addTankStatAnimator(Animator tankStatAnimator, EnumInterfaceElement type) {
        this.tankStatAnimator.put(type, tankStatAnimator);
    }

    public void addTankPreviewAnimator(Animator tankPreviewAnimator, EnumInterfaceElement type) {
        this.tankPreviewAnimator.put(type, tankPreviewAnimator);
    }

    public void nextTankStat() {
        EnumInterfaceElement[] values = {EnumInterfaceElement.TIGER, EnumInterfaceElement.SNIPER, EnumInterfaceElement.RUSHER};
        int index = this.currentTankStat.getIndex();

        if (index == 2) {
            index = 0;
        } else {
            index += 1;
        }
        this.currentTankStat = values[index];
    }

    public void prevTankStat() {
        EnumInterfaceElement[] values = {EnumInterfaceElement.TIGER, EnumInterfaceElement.SNIPER, EnumInterfaceElement.RUSHER};
        int index = this.currentTankStat.getIndex();

        if (index == 0) {
            index = 2;
        } else {
            index -= 1;
        }
        this.currentTankStat = values[index];
    }

    public EnumGameObject getCurrentTank() {
        if (this.available.containsKey(this.currentTankStat) && this.available.get(this.currentTankStat) == true) {
            return EnumGameObject.getEnumByValue(this.currentTankStat.getValue());
        }
        return EnumGameObject.NULL;
    }
}
