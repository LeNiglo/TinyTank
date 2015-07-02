package com.lefrantguillaume.components.graphicsComponent.userInterface.tools.elements;

import com.lefrantguillaume.components.gameComponent.animations.Animator;
import com.lefrantguillaume.components.graphicsComponent.userInterface.elements.EnumOverlayElement;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.items.BodyRect;
import org.newdawn.slick.Graphics;

/**
 * Created by andres_k on 27/06/2015.
 */
public class ImageElement extends Element {
    private Animator animator;

    public ImageElement(BodyRect body, Animator animator, PositionInBody position) {
        this.init(body, "", position, EnumOverlayElement.IMAGE);
        this.animator = animator;
    }

    public ImageElement(BodyRect body, Animator animator, String id, PositionInBody position) {
        this.init(body, id, position, EnumOverlayElement.IMAGE);
        this.animator = animator;
    }

    public ImageElement(Animator animator, String id, PositionInBody position) {
        this.init(null, id, position, EnumOverlayElement.IMAGE);
        this.animator = animator;
    }


    @Override
    public void draw(Graphics g) {
        if (this.body != null && this.body.getMinX() != -1) {
            float x = this.body.getMinX();
            float y = this.body.getMinY();

            if (this.position == PositionInBody.MIDDLE_MID) {
                x += (this.body.getSizeX() / 2) - (this.animator.currentAnimation().getWidth() / 2);
                y += (this.body.getSizeY() / 2) - (this.animator.currentAnimation().getHeight() / 2);
            } else if (this.position == PositionInBody.RIGHT_MID) {
                x += (this.body.getSizeX() - this.animator.currentAnimation().getWidth());
                y += (this.body.getSizeY() / 2) - (this.animator.currentAnimation().getHeight() / 2);
            } else if (this.position == PositionInBody.MIDDLE_DOWN) {
                x += (this.body.getSizeX() / 2) - (this.animator.currentAnimation().getWidth() / 2);
                y += (this.body.getSizeY() - this.animator.currentAnimation().getHeight());
            } else if (this.position == PositionInBody.RIGHT_DOWN) {
                x += (this.body.getSizeX() - this.animator.currentAnimation().getWidth());
                y += (this.body.getSizeY() - this.animator.currentAnimation().getHeight());
            } else if (this.position == PositionInBody.LEFT_DOWN) {
                y += (this.body.getSizeY() - this.animator.currentAnimation().getHeight());
            } else if (this.position == PositionInBody.MIDDLE_UP) {
                x += (this.body.getSizeX() / 2) - (this.animator.currentAnimation().getWidth() / 2);
            } else if (this.position == PositionInBody.RIGHT_UP) {
                x += (this.body.getSizeX() - this.animator.currentAnimation().getWidth());
            }

            this.body.draw(g);
            g.drawAnimation(this.animator.currentAnimation(), x, y);
        }
    }

    @Override
    public void draw(Graphics g, BodyRect body) {
        if (body.getMinX() != -1) {
            float x = body.getMinX();
            float y = body.getMinY();

            if (this.position == PositionInBody.MIDDLE_MID) {
                x += (body.getSizeX() / 2) - (this.animator.currentAnimation().getWidth() / 2);
                y += (body.getSizeY() / 2) - (this.animator.currentAnimation().getHeight() / 2);
            } else if (this.position == PositionInBody.RIGHT_MID) {
                x += (body.getSizeX() - this.animator.currentAnimation().getWidth());
                y += (body.getSizeY() / 2) - (this.animator.currentAnimation().getHeight() / 2);
            } else if (this.position == PositionInBody.MIDDLE_DOWN) {
                x += (body.getSizeX() / 2) - (this.animator.currentAnimation().getWidth() / 2);
                y += (body.getSizeY() - this.animator.currentAnimation().getHeight());
            } else if (this.position == PositionInBody.RIGHT_DOWN) {
                x += (body.getSizeX() - this.animator.currentAnimation().getWidth());
                y += (body.getSizeY() - this.animator.currentAnimation().getHeight());
            } else if (this.position == PositionInBody.LEFT_DOWN) {
                y += (body.getSizeY() - this.animator.currentAnimation().getHeight());
            } else if (this.position == PositionInBody.MIDDLE_UP) {
                x += (body.getSizeX() / 2) - (this.animator.currentAnimation().getWidth() / 2);
            } else if (this.position == PositionInBody.RIGHT_UP) {
                x += (body.getSizeX() - this.animator.currentAnimation().getWidth());
            }

            body.draw(g);
            g.drawAnimation(this.animator.currentAnimation(), x, y);
        }
    }

    @Override
    public void update() {
    }

    @Override
    public boolean replace(Element element) {
        if (element.getType() == EnumOverlayElement.IMAGE){
            this.animator = new Animator(((ImageElement)element).animator);
            return true;
        }
        return false;
    }

    @Override
    public Object doTask(Object task) {
        if (task instanceof String){
            String value = (String)task;
            if (value.equals("start")){
                this.start();
            }
        }
        return null;
    }

    @Override
    public boolean isActivated() {
        return true;
    }

    @Override
    public boolean isEmpty() {
        if (this.animator == null || this.animator.isDeleted()) {
            return true;
        }
        return false;
    }

    @Override
    public float getAbsoluteWidth() {
        return this.animator.currentAnimation().getWidth();
    }

    @Override
    public float getAbsoluteHeight() {
        return this.animator.currentAnimation().getHeight();
    }

    @Override
    public String toString() {
        return "imageType: " + this.animator.getCurrent();
    }

    private void start(){
        this.animator.restart();
    }
}
