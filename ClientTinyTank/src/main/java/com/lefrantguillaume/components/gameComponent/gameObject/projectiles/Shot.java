package com.lefrantguillaume.components.gameComponent.gameObject.projectiles;

import com.lefrantguillaume.Utils.stockage.Pair;
import com.lefrantguillaume.Utils.stockage.Tuple;
import com.lefrantguillaume.Utils.tools.Block;
import com.lefrantguillaume.Utils.tools.MathTools;
import com.lefrantguillaume.components.gameComponent.animations.Animator;
import com.lefrantguillaume.components.gameComponent.gameObject.EnumGameObject;
import org.newdawn.slick.Graphics;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by andres_k on 13/03/2015.
 */
public class Shot extends Observable implements Observer {
    private final String id;
    private final String userId;
    private final Animator animator;
    private final Pair<Float, Float> shiftHead;
    private final Pair<Float, Float> shiftToExplode;
    private final EnumGameObject type;
    private Pair<Float, Float> shiftOrigin;
    private Pair<Float, Float> positions;
    private float damage;
    private float speed;
    private float angle;
    private boolean explode;
    private List<Block> collisionObject;
    private List<Pair<Float, Float>> savePosShot;

    public Shot(String userId, String id, EnumGameObject type, float damage, float speed, Animator animator, Tuple<Float, Float, Float> positioning,
                Pair<Float, Float> shiftOrigin, Pair<Float, Float> shiftToExplode, Pair<Float, Float> shiftHead) {
        this.positions = new Pair<>(positioning.getV1(), positioning.getV2());
        this.angle = positioning.getV3();
        this.shiftOrigin = new Pair<>(shiftOrigin);
        this.shiftToExplode = new Pair<>(shiftToExplode);
        this.shiftHead = new Pair<>(shiftHead);
        this.savePosShot = new ArrayList<>();
        this.type = type;
        this.explode = false;
        this.userId = userId;
        this.id = id;
        this.damage = damage;
        this.speed = speed;
        this.animator = animator;
        this.collisionObject = new ArrayList<>();
    }

    @Override
    public void update(Observable o, Object arg) {
        Tuple<Float, Float, EnumGameObject> task = (Tuple<Float, Float, EnumGameObject>) arg;

        Pair<Float, Float> newPoint = new Pair<>(task.getV1() + this.shiftHead.getV1(), task.getV2() + this.shiftHead.getV2());
        MathTools.rotate(new Pair<>(task.getV1(), task.getV2()), newPoint, angle);
        this.positions.setV1(newPoint.getV1());
        this.positions.setV2(newPoint.getV2());

        Pair<Float, Float> newShift = new Pair<>(this.positions.getV1() + this.shiftOrigin.getV1(), this.positions.getV2() + this.shiftOrigin.getV2());
        MathTools.rotate(this.positions, newShift, angle);
        this.shiftOrigin.setV1(this.shiftToExplode.getV1());
        this.shiftOrigin.setV2(this.shiftToExplode.getV2());

        this.addNewPosition();
        this.animator.setIndex(EnumAnimationShot.EXPLODE.getValue());
        this.explode = true;
        Block nullBlock = null;
        this.setChanged();
        this.notifyObservers(nullBlock);
    }

    // FUNCTIONS

    public void draw(Graphics g) {
        this.animator.currentAnimation().getCurrentFrame().setCenterOfRotation(this.getShiftOrigin().getV1() * -1, this.getShiftOrigin().getV2() * -1);
        this.animator.currentAnimation().getCurrentFrame().setRotation(this.getAngle());
        for (int i = 0; i < this.savePosShot.size(); ++i) {
            g.drawAnimation(this.animator.currentAnimation(), this.savePosShot.get(i).getV1(), this.savePosShot.get(i).getV2());
        }
    }

    public void move(float delta) {
        if (this.explode == false) {
            Pair<Float, Float> coords = this.movePredict(delta);
            this.positions.setV1(this.getX() + coords.getV1());
            this.positions.setV2(this.getY() + coords.getV2());
            this.addNewPosition();
        }
    }

    private void addNewPosition() {
        if (this.type != EnumGameObject.LASER) {
            this.savePosShot.clear();
        }
        this.savePosShot.add(new Pair<>(this.getGraphicalX(), this.getGraphicalY()));
    }

    public Pair<Float, Float> movePredict(float delta) {
        return MathTools.movePredict(this.angle, this.speed, delta);
    }

    public Pair<Float, Float> coordPredict(float delta) {
        Pair<Float, Float> coords = this.movePredict(delta);
        coords.setV1(coords.getV1() + this.getX());
        coords.setV2(coords.getV2() + this.getY());
        return coords;
    }

    public void addCollisionObject(Block block) {
        this.collisionObject.add(block);
    }

    // GETTERS
    public float getGraphicalX() {
        return this.positions.getV1() + this.shiftOrigin.getV1();
    }

    public float getGraphicalY() {
        return this.positions.getV2() + this.shiftOrigin.getV2();
    }

    public float getX() {
        return this.positions.getV1();
    }

    public float getY() {
        return this.positions.getV2();
    }

    public String getUserId() {
        return this.userId;
    }

    public String getId() {
        return this.id;
    }

    public float getDamage() {
        return this.damage;
    }

    public float getSpeed() {
        return this.speed;
    }

    public float getAngle() {
        return this.angle;
    }

    public EnumGameObject getType() {
        return this.type;
    }

    public boolean getExplode() {
        return this.explode;
    }

    public Animator getAnimator() {
        return animator;
    }

    public Pair<Float, Float> getPositions() {
        return this.positions;
    }

    public Pair<Float, Float> getShiftOrigin() {
        return this.shiftOrigin;
    }

    public Pair<Float, Float> getShiftToExplode() {
        return this.shiftToExplode;
    }

    public List<Block> getCollisionObject() {
        return this.collisionObject;
    }

    public List<EnumGameObject> getIgnoredObjectList() {
        List<EnumGameObject> types = new ArrayList<>();

        if (this.type.equals(EnumGameObject.LASER))
            types.add(EnumGameObject.PLASMA_WALL);
        return types;
    }
}
