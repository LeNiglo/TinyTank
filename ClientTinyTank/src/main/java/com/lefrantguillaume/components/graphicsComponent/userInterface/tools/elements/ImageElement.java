package com.lefrantguillaume.components.graphicsComponent.userInterface.tools.elements;

import com.lefrantguillaume.Utils.stockage.Pair;
import com.lefrantguillaume.components.gameComponent.animations.Animator;
import com.lefrantguillaume.components.graphicsComponent.userInterface.overlay.EnumOverlayElement;
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
    public void leave(){
    }

    public void draw(Graphics g) {
        if (this.body != null && this.body.getMinX() != -1) {
            Pair<Float, Float> position = this.getChoicePosition(this.body);
            this.body.draw(g);
            g.drawAnimation(this.animator.currentAnimation(), position.getV1(), position.getV2());
        }
    }

    @Override
    public void draw(Graphics g, BodyRect body) {
        if (body.getMinX() != -1) {
            Pair<Float, Float> position = this.getChoicePosition(body);
            if (this.body != null && body.getColor() == null){
                body.setColor(this.body.getColor());
            }
            body.draw(g);
            g.drawAnimation(this.animator.currentAnimation(), position.getV1(), position.getV2());
        }
    }

    private Pair<Float, Float> getChoicePosition(BodyRect body){
        float x = body.getMinX();
        float y = body.getMinY();

        if (this.position == PositionInBody.MIDDLE_MID) {
            float sizeX = (body.getSizeX() / 2) - (this.animator.currentAnimation().getWidth() / 2);
            float sizeY = (body.getSizeY() / 2) - (this.animator.currentAnimation().getHeight() / 2);

            sizeX = (sizeX < 0 ? 0 : sizeX);
            sizeY = (sizeY < 0 ? 0 : sizeY);
            x += sizeX;
            y += sizeY;
        } else if (this.position == PositionInBody.RIGHT_MID) {
            float sizeX = (body.getSizeX() - this.animator.currentAnimation().getWidth());
            float sizeY = (body.getSizeY() / 2) - (this.animator.currentAnimation().getHeight() / 2);

            sizeX = (sizeX < 0 ? 0 : sizeX);
            sizeY = (sizeY < 0 ? 0 : sizeY);
            x += sizeX;
            y += sizeY;
        } else if (this.position == PositionInBody.MIDDLE_DOWN) {
            float sizeX = (body.getSizeX() / 2) - (this.animator.currentAnimation().getWidth() / 2);
            float sizeY = (body.getSizeY() - this.animator.currentAnimation().getHeight());

            sizeX = (sizeX < 0 ? 0 : sizeX);
            sizeY = (sizeY < 0 ? 0 : sizeY);
            x += sizeX;
            y += sizeY;
        } else if (this.position == PositionInBody.RIGHT_DOWN) {
            float sizeX = (body.getSizeX() - this.animator.currentAnimation().getWidth());
            float sizeY = (body.getSizeY() - this.animator.currentAnimation().getHeight());

            sizeX = (sizeX < 0 ? 0 : sizeX);
            sizeY = (sizeY < 0 ? 0 : sizeY);
            x += sizeX;
            y += sizeY;
        } else if (this.position == PositionInBody.LEFT_DOWN) {
            float sizeY = (body.getSizeY() - this.animator.currentAnimation().getHeight());

            sizeY = (sizeY < 0 ? 0 : sizeY);
            y += sizeY;
        } else if (this.position == PositionInBody.MIDDLE_UP) {
            float sizeX = (body.getSizeX() / 2) - (this.animator.currentAnimation().getWidth() / 2);

            sizeX = (sizeX < 0 ? 0 : sizeX);
            x += sizeX;
        } else if (this.position == PositionInBody.RIGHT_UP) {
            float sizeX = (body.getSizeX() - this.animator.currentAnimation().getWidth());

            sizeX = (sizeX < 0 ? 0 : sizeX);
            x += sizeX;
        }

        return new Pair<>(x, y);
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
