package ru.rsreu.doodle_game.physic;

import javafx.geometry.Point2D;

import java.util.List;

public class PhysicEngine {
    private final static Point2D GRAVITY_FORCE = new Point2D(0, 0.2f);
    private final static Point2D JUMP_FORCE = new Point2D(0, -10);

    public PhysicEngine() {

    }

    public Point2D tryMoveRigidBody(RigidBody rigidBody, List<RigidBody> otherBodies) {
        otherBodies.forEach(otherBody -> {
            if(!rigidBody.getCollider().isCollideWith(otherBody.getCollider())) {
                applyGravity(rigidBody);
            } else {
                rigidBody.addForce(new Point2D(0, -rigidBody.getVelocity().getY()).add(JUMP_FORCE));
            }
        });

        return rigidBody.getVelocity();
    }

    private void applyGravity(RigidBody rigidBody) {
        if (rigidBody.isGravity()) {
            rigidBody.addForce(GRAVITY_FORCE);
        }
    }
}
