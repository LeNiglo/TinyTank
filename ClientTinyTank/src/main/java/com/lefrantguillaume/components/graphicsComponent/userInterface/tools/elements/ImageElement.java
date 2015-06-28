package com.lefrantguillaume.components.graphicsComponent.userInterface.tools.elements;

import com.lefrantguillaume.components.gameComponent.animations.Animator;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.items.BodyRect;
import org.newdawn.slick.Graphics;

/**
 * Created by andres_k on 27/06/2015.
 */
public class ImageElement extends Element {
    private Animator animator;

    public ImageElement(BodyRect body, Animator animator){
        this.body = body;
        this.animator = animator;
    }

    @Override
    public void draw(Graphics g) {
        this.body.draw(g);
        g.drawAnimation(animator.currentAnimation(), this.body.getX(), this.body.getY());
    }

    @Override
    public void draw(Graphics g, BodyRect body) {

    }

    @Override
    public void update() {
    }

    @Override
    public boolean isActivated() {
        return true;
    }

    @Override
    public boolean isEmpty() {
        if (this.animator == null || this.animator.isDeleted()){
            return true;
        }
        return false;
    }
}
