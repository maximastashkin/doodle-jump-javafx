package ru.rsreu.doodle.physic;

import javafx.geometry.Point2D;
import ru.rsreu.doodle.GameApplication;

import java.util.List;
import java.util.Random;

public class PhysicEngine {
    // 185 max-height doodler jump for -12 jump force!!!!!!!!!!
    // 655 max-height doodler jump for -20 jump force!!!!!!!!!!
    private final static boolean SUBJECT_TO_GRAVITY = true;
    private final static Point2D PLAYER_GRAVITY_FORCE = new Point2D(0, 0.4f);
    private final static Point2D PLATFORM_GRAVITY_FORCE = new Point2D(0, 0.15f);
    private final static float FALLING_SPEED = 4f;
    //Debug!!!!, will make private
    public static Point2D JUMP_FORCE = new Point2D(0, -12);

    private final static int LEFT_SCREEN_BORDER_COORDINATE = -30;
    private final static int RIGHT_SCREEN_BORDER_COORDINATE = GameApplication.WINDOW_WIDTH - 30;

    public PhysicEngine() {

    }

    public Point2D tryMoveRigidBody(RigidBody rigidBody, List<RigidBody> otherBodies) {
        applyGravity(rigidBody);
        if (rigidBody.getRigidBodyType() == RigidBodyType.PLAYER) {
            applyScreenTransition(rigidBody.getCollider(), rigidBody.getVelocity());
            otherBodies.forEach(otherBody -> {
                if (rigidBody.getCollider().isCollideWith(otherBody.getCollider())
                        && rigidBody.getCollider().isUpperCollideWith(otherBody.getCollider())
                        && rigidBody.isFalling(FALLING_SPEED)) {
                    applyJumpOrBreakPlatform(rigidBody, otherBody);
                }
            });
        }
        if (rigidBody.getRigidBodyType() == RigidBodyType.MOVING_PLATFORM) {
            applyPlatformMove(rigidBody);
        }
        return rigidBody.getVelocity();
    }

    private void applyGravity(RigidBody rigidBody) {
        if (rigidBody.isGravity()) {
            applyGravityForConcreteType(rigidBody);
        }
    }

    private void applyGravityForConcreteType(RigidBody rigidBody) {
        if (rigidBody.getRigidBodyType() == RigidBodyType.PLAYER) {
            rigidBody.addForce(PLAYER_GRAVITY_FORCE);
        }
        if (rigidBody.getRigidBodyType() == RigidBodyType.BREAKING_PLATFORM) {
            rigidBody.addForce(PLATFORM_GRAVITY_FORCE);
        }
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

    private void applyJumpOrBreakPlatform(RigidBody playerBody, RigidBody otherBody) {
        if (otherBody.getRigidBodyType() == RigidBodyType.BREAKING_PLATFORM) {
            otherBody.setGravity(SUBJECT_TO_GRAVITY);
        } else {
            applyJump(playerBody);
        }
    }

    private void applyJump(RigidBody rigidBody) {
        rigidBody.addForce(new Point2D(0, -rigidBody.getVelocity().getY()).add(JUMP_FORCE));
    }

    private void applyPlatformMove(RigidBody rigidBody) {
        if (rigidBody.getVelocity().getX() == 0) {
            this.getRandomPlatformMovingDirection().applyMoving(rigidBody);
        }
        if (rigidBody.getCollider().getPosition().getX() <= 0) {
            PlatformMovingDirection.RIGHT.applyMoving(rigidBody);
        }
        if (rigidBody.getCollider().getPositionAddExtend().getX() >= GameApplication.WINDOW_WIDTH) {
            PlatformMovingDirection.LEFT.applyMoving(rigidBody);
        }
    }

    private PlatformMovingDirection getRandomPlatformMovingDirection() {
        Random random = new Random();
        if (random.nextFloat() >= 0.5f) {
            return PlatformMovingDirection.RIGHT;
        }
        return PlatformMovingDirection.LEFT;
    }
}
