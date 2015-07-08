package com.lefrantguillaume.components.graphicsComponent.userInterface.tools.elements;

import com.lefrantguillaume.utils.stockage.Pair;
import com.lefrantguillaume.components.gameComponent.animations.Animator;
import com.lefrantguillaume.components.graphicsComponent.userInterface.overlay.EnumOverlayElement;
import com.lefrantguillaume.components.graphicsComponent.userInterface.tools.items.BodyRect;
import org.newdawn.slick.Graphics;

/**
 * Created by andres_k on 27/06/2015.
 */
public class ImageElement extends Element {
    private Animator animator;
    private float sizeXMAX;

    public ImageElement(BodyRect body, Animator animator, PositionInBody position) {
        this.init(body, "", position, EnumOverlayElement.IMAGE);
        this.animator = animator;
        this.sizeXMAX = body.getSizeX();
    }

    public ImageElement(BodyRect body, Animator animator, String id, PositionInBody position) {
        this.init(body, id, position, EnumOverlayElement.IMAGE);
        this.animator = animator;
        this.sizeXMAX = body.getSizeX();
    }

    public ImageElement(BodyRect body, String id, PositionInBody position) {
        this.init(body, id, position, EnumOverlayElement.IMAGE);
        this.animator = null;
        this.sizeXMAX = body.getSizeX();
    }

    public ImageElement(Animator animator, String id, PositionInBody position) {
        this.init(null, id, position, EnumOverlayElement.IMAGE);
        this.animator = animator;
        this.sizeXMAX = 0;
    }


    @Override
    public void leave() {
    }

    public void draw(Graphics g) {
        if (this.body != null && this.body.getMinX() != -1) {
            this.body.draw(g);

            if (this.animator != null && this.animator.isPrintable()) {
                Pair<Float, Float> position = this.getChoicePosition(this.body);
                g.drawAnimation(this.animator.currentAnimation(), position.getV1(), position.getV2());
            }
        }
    }

    @Override
    public void draw(Graphics g, BodyRect body) {
        if (body.getMinX() != -1) {
            if (this.body != null && body.getColor() == null) {
                body.setColor(this.body.getColor());
            }
            body.draw(g);
            if (this.animator != null && this.animator.isPrintable()) {
                Pair<Float, Float> position = this.getChoicePosition(body);
                g.drawAnimation(this.animator.currentAnimation(), position.getV1(), position.getV2());
            }
        }
    }

    private Pair<Float, Float> getChoicePosition(BodyRect body) {
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
        if (this.animator != null) {
            if (this.animator.needUpdate() && this.animator.isActivated()) {
                this.animator.updateAnimator(true, true);
            }
        }
    }

    @Override
    public boolean replace(Element element) {
        if (element.getType() == EnumOverlayElement.IMAGE) {
            this.animator = new Animator(((ImageElement) element).animator);
            return true;
        }
        return false;
    }

    @Override
    public Object doTask(Object task) {
        if (task instanceof String) {
            String value = (String) task;
            if (value.equals("start")) {
                this.start();
            }
        } else if (task instanceof Long) {
            this.animator.updateAnimator(false, false);
            this.animator.startTimer((Long) task);
        } else if (task instanceof Pair) {
            if (((Pair) task).getV1() instanceof String) {
                if (((Pair) task).getV1().equals("newCurrentIndex") && ((Pair) task).getV2() instanceof Integer && this.animator != null) {
                    this.animator.setIndex((Integer) ((Pair) task).getV2());
                } else if (((Pair) task).getV1().equals("cutBody") && ((Pair) task).getV2() instanceof Float) {
                    float percent = (Float) ((Pair) task).getV2();
                    if (percent >= 1){
                        this.body.setPrintable(true);
                        this.body.setSizes(this.sizeXMAX, this.body.getSizeY());
                    }
                    else if (percent > 0){
                        if (!this.id.contains(EnumOverlayElement.BORDER.getValue())){
                            this.body.setPrintable(true);
                            this.body.setSizes(this.sizeXMAX * percent, this.body.getSizeY());
                        }
                    } else {
                        this.body.setPrintable(false);
                        this.body.setSizes(0, this.body.getSizeY());
                    }
                }
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "imageType: " + (this.animator != null ? this.animator.getCurrentAnimation() : "");
    }

    private void start(){
        this.animator.restart();
    }

    //GETTERS
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

    // SETTERS
    public void setBody(BodyRect body) {
        if (this.body != null) {
            if (body.getColor() == null) {
                body.setColor(this.body.getColor());
            }
        }
        this.body = body;
        this.sizeXMAX = this.body.getSizeX();
    }
}
