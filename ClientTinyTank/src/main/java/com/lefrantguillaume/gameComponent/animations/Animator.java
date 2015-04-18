package com.lefrantguillaume.gameComponent.animations;

import com.lefrantguillaume.Utils.stockage.Pair;
import org.newdawn.slick.Animation;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by andres_k on 13/03/2015.
 */
public class Animator implements Observer {
    private List<Animation> animations;
    private int index;

    public Animator() {
        this.animations = new ArrayList<Animation>();
        this.index = 0;
    }
    public Animator(Animator animator){
        this.animations = new ArrayList<Animation>();
        for (int i = 0; i < animator.animations.size(); ++i) {
            this.animations.add(animator.animations.get(i).copy());
        }
        this.index = animator.index;
    }
    public Animation getAnimation(int index) {
        return animations.get(index);
    }

    public void addAnimation(Animation animation){
        this.animations.add(animation);
    }

    public Animation currentAnimation(){
       return this.animations.get(this.index);
    }

    public Pair<Float, Float> currentSizeAnimation(){
        return new Pair<Float, Float>((float)this.animations.get(this.index).getWidth(), (float)this.animations.get(this.index).getHeight());
    }
    public void setIndex(int index){
        this.index = index;
    }
    @Override
    public void update(Observable o, Object arg) {
        this.index = (Integer)arg;
    }
}
