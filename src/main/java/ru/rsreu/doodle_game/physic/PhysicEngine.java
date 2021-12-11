package ru.rsreu.doodle_game.physic;

import javafx.geometry.Point2D;
import ru.rsreu.doodle_game.GameApplication;

import java.util.List;

public class PhysicEngine {
    // 185 max-height doodler jump for -12 jump force!!!!!!!!!!
    // 655 max-height doodler jump for -20 jump force!!!!!!!!!!
    private final static Point2D GRAVITY_FORCE = new Point2D(0, 0.4f);
    public static Point2D JUMP_FORCE = new Point2D(0, -12);

    private final static int LEFT_SCREEN_BORDER_COORDINATE = -30;
    private final static int RIGHT_SCREEN_BORDER_COORDINATE = GameApplication.WINDOW_WIDTH - 30;

    public PhysicEngine() {

    }

    public Point2D tryMoveRigidBody(RigidBody rigidBody, List<RigidBody> otherBodies) {
        if (rigidBody.getRigidBodyType() == RigidBodyType.PLAYER) {
            applyScreenTransition(rigidBody.getCollider(), rigidBody.getVelocity());
            applyGravity(rigidBody);
            otherBodies.forEach(otherBody -> {
                if(rigidBody.getCollider().isCollideWith(otherBody.getCollider())
                        && rigidBody.getCollider().isUpperCollideWith(otherBody.getCollider())
                        && rigidBody.isFalling()) {
                    applyJump(rigidBody);
                }
            });
        }
        return rigidBody.getVelocity();
    }

    private void applyScreenTransition(Collider collider, Point2D velocity) {
        Point2D newPosition = collider.getPosition();
        if (isColliderIntersectRightScreenBorderWithPositiveVelocity(collider, velocity)) {
            newPosition = new Point2D(LEFT_SCREEN_BORDER_COORDINATE, collider.getPosition().getY());
        }
        if (isColliderIntersectLeftScreenBorderWithNegativeVelocity(collider, velocity)) {
            newPosition = new Point2D(RIGHT_SCREEN_BORDER_COORDINATE, collider.getPosition().getY());
        }
        collider.setPosition(newPosition);
    }

    private boolean isColliderIntersectRightScreenBorderWithPositiveVelocity(Collider collider, Point2D velocity) {
        return collider.getPosition().getX() > RIGHT_SCREEN_BORDER_COORDINATE && velocity.getX() > 0;
    }

    private boolean isColliderIntersectLeftScreenBorderWithNegativeVelocity(Collider collider, Point2D velocity) {
        return collider.getPosition().getX() < LEFT_SCREEN_BORDER_COORDINATE && velocity.getX() < 0;
    }

    private void applyGravity(RigidBody rigidBody) {
        if (rigidBody.isGravity()) {
            rigidBody.addForce(GRAVITY_FORCE);
        }
    }

    private void applyJump(RigidBody rigidBody) {
        rigidBody.addForce(new Point2D(0, -rigidBody.getVelocity().getY()).add(JUMP_FORCE));
    }
}
