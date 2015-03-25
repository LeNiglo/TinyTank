package com.lefrantguillaume.gameComponent.gameObject.projectiles;

import com.lefrantguillaume.Utils.tools.Debug;
import com.lefrantguillaume.Utils.stockage.Pair;
import com.lefrantguillaume.Utils.stockage.Tuple;
import com.lefrantguillaume.Utils.tools.MathTools;
import com.lefrantguillaume.Utils.tools.Rectangle;
import com.lefrantguillaume.gameComponent.animations.Animator;
import com.lefrantguillaume.gameComponent.gameObject.EnumType;

import java.util.*;

/**
 * Created by andres_k on 13/03/2015.
 */
public class Shot extends Observable implements Observer {
    private final UUID id;
    private final String userId;
    private final Animator animator;
    private final Pair<Float, Float> shiftOrigin;
    private final Pair<Float, Float> shiftToImpact;
    private Pair<Float, Float> positions;
    private float damage;
    private float speed;
    private float angle;
    private boolean explode;
    private List<Rectangle> collisionObject;

    public Shot(String userId, float damage, float speed, Animator animator, Tuple<Float, Float, Float> positioning, Pair<Float, Float> shiftOrigin, Pair<Float, Float> shiftToImpact) {
        this.shiftOrigin = shiftOrigin;
        this.shiftToImpact = shiftToImpact;
        this.explode = false;
        this.userId = userId;
        this.id = UUID.randomUUID();
        this.positions = new Pair<Float, Float>(positioning.getV1(), positioning.getV2());
        this.angle = positioning.getV3();
        this.damage = damage;
        this.speed = speed;
        this.animator = animator;
        this.collisionObject = new ArrayList<Rectangle>();
    }

    @Override
    public void update(Observable o, Object arg) {
        Tuple<Float, Float, EnumType> order = (Tuple<Float, Float, EnumType>) arg;

        Debug.debug("check Object");
        if (order.getV3() == EnumType.OBSTACLE || order.getV3() == EnumType.SHOT || order.getV3() == EnumType.TANK) {
//            this.shiftOrigin.setV1(this.shiftToImpact.getV1());
 //           this.shiftOrigin.setV2(this.shiftToImpact.getV2());

//            Debug.debug("newX= "+order.getV1()+" + " + this.shiftToImpact.getV1()+ " = " + newX);
  //          Debug.debug("newY= "+order.getV2()+" + " + this.shiftToImpact.getV2() + " = " + newY);

            //rotate newX et newY

   //         this.positions.setV1(order.getV1());
     //       this.positions.setV2(order.getV2());
            this.animator.setIndex(EnumAnimationShot.EXPLODE.getValue());
            this.explode = true;
        }
    }

    // FUNCTIONS
    public void move(int delta) {
        Pair<Float, Float> coords = MathTools.movePredict(this.angle, this.speed, delta);
        this.positions.setV1(this.positions.getV1() + coords.getV1());
        this.positions.setV2(this.positions.getV2() + coords.getV2());
    }

    public Pair<Float, Float> movePredict(int delta) {
        return MathTools.movePredict(this.angle, this.speed, delta);
    }

    public void addCollisionObject(Rectangle rectangle) {
        this.collisionObject.add(rectangle);
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

    public UUID getId() {
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

    public boolean getExplode() {
        return this.explode;
    }

    public Animator getAnimator() {
        return animator;
    }

    public Pair<Float, Float> getPositions() {
        return this.positions;
    }

    public Pair<Float, Float> getShiftOrigin(){
        return this.shiftOrigin;
    }

    public Pair<Float, Float> getShiftToImpact(){
        return this.shiftToImpact;
    }

    public List<Rectangle> getCollisionObject(){
        return this.collisionObject;
    }
}
