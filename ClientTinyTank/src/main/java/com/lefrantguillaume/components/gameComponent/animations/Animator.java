package com.lefrantguillaume.components.gameComponent.animations;

import com.lefrantguillaume.Utils.stockage.Pair;
import com.lefrantguillaume.Utils.tools.Debug;
import org.newdawn.slick.Animation;

import java.util.*;

/**
 * Created by andres_k on 13/03/2015.
 */
public class Animator implements Observer {
    private HashMap<EnumAnimation, List<Animation>> animations;
    private EnumAnimation current;
    private int index;
    private boolean printable;

    public Animator() {
        this.animations = new HashMap<>();
        this.current = EnumAnimation.BASIC;
        this.printable = true;
        this.index = 0;
    }

    public Animator(Animator animator) {
        this.animations = new HashMap<>();
        for (Map.Entry entry : animator.animations.entrySet()) {
            this.addElement((EnumAnimation) entry.getKey(), (List<Animation>) entry.getValue());
        }
        this.current = animator.current;
        this.index = animator.index;
        this.printable = animator.printable;
    }

    // FUNCTIONS
    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof EnumAnimation) {
            this.setCurrent((EnumAnimation) arg);
        }
    }

    public void addAnimation(EnumAnimation type, Animation animation) {
        if (this.animations.containsKey(type)) {
            this.animations.get(type).add(animation.copy());
        } else {
            List<Animation> values = new ArrayList<>();
            values.add(animation.copy());
            this.animations.put(type, values);
        }
    }

    public void addElement(EnumAnimation type, List<Animation> animation) {
        for (Animation anAnimation : animation) {
            this.addAnimation(type, anAnimation);
        }
    }

    public void nextCurrentIndex(){
        if ((this.index + 1) < this.animations.get(this.current).size()){
            this.index += 1;
        }
    }

    // GETTERS
    public Animation currentAnimation() {
        return this.animations.get(this.current).get(this.index);
    }

    public Pair<Float, Float> currentSizeAnimation() {
        return new Pair<>((float) this.animations.get(this.current).get(this.index).getWidth(), (float) this.animations.get(this.current).get(this.index).getHeight());
    }

    public boolean isPrintable() {
        return this.printable;
    }

    // SETTERS
    public boolean isStopped(){
        if (this.current == EnumAnimation.EXPLODE && this.currentAnimation().isStopped()){
            return true;
        }
        return false;
    }
    public void setPrintable(boolean printable) {
        this.printable = printable;
    }

    public void setCurrent(EnumAnimation current) {
        if (this.animations.containsKey(current)) {
            this.current = current;
            this.index = 0;
        }
    }

    public void setIndex(int index){
        if (index < this.animations.get(this.current).size()){
            this.index = index;
        }
    }
}
