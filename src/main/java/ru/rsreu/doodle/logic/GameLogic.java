package ru.rsreu.doodle.logic;

import javafx.geometry.Point2D;
import ru.rsreu.doodle.GameApplication;

import java.util.List;
import java.util.Random;

public class GameLogic {
    public final static Point2D MOVEMENT_FORCE = new Point2D(5f, 0);
    public final static float FALLING_SPEED = 4f;
    private final static float SUFFICIENT_Y_VELOCITY_FOR_SCROLLING = -6f;
    // 185 max-height doodler jump for -12 jump force!!!!!!!!!!
    // 655 max-height doodler jump for -20 jump force!!!!!!!!!!
    private final static boolean SUBJECT_TO_GRAVITY = true;
    private final static Point2D PLAYER_GRAVITY_FORCE = new Point2D(0, 0.4f);
    private final static Point2D PLATFORM_GRAVITY_FORCE = new Point2D(0, 0.15f);
    private final static float LEFT_SCREEN_BORDER_COORDINATE = -30;
    private final static float RIGHT_SCREEN_BORDER_COORDINATE = GameApplication.WINDOW_WIDTH - 30;
    private final static float MIDDLE_SCREEN_LINE_Y_COORDINATE = GameApplication.WINDOW_HEIGHT / 2f;

    //Debug!!!!, will make private
    public static Point2D JUMP_FORCE = new Point2D(0, -12);

    private int gameScore = 0;

    public GameLogic() {

    }

    public Point2D tryMoveRigidBody(RigidBody rigidBody, List<RigidBody> otherBodies) {
        applyGravity(rigidBody);
        switch (rigidBody.getRigidBodyType()) {
            case PLAYER: {
                applyScreenTransition(rigidBody.getCollider(), rigidBody.getVelocity());
                otherBodies.forEach(otherBody -> {
                    if (isBodiesIntersectionFitsForJump(rigidBody, otherBody)) {
                        applyJumpOrBreakPlatform(rigidBody, otherBody);
                    }
                });
                if (isBodyIntersectionScreenMiddleLineFitsForScrolling(rigidBody)) {
                    applyScreenScrolling(rigidBody, otherBodies);
                }
                break;
            }
            case MOVING_PLATFORM: {
                applyPlatformMove(rigidBody);
                break;
            }
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
        if (rigidBody.getRigidBodyType() == RigidBodyType.BROKEN_PLATFORM) {
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

    private boolean isBodiesIntersectionFitsForJump(RigidBody playerBody, RigidBody otherBody) {
        return playerBody.getCollider().isCollideWith(otherBody.getCollider())
                && playerBody.getCollider().isUpperCollideWith(otherBody.getCollider())
                && playerBody.isFalling(FALLING_SPEED);
    }

    private void applyJumpOrBreakPlatform(RigidBody playerBody, RigidBody otherBody) {
        if (otherBody.getRigidBodyType() == RigidBodyType.BREAKING_PLATFORM
                || otherBody.getRigidBodyType() == RigidBodyType.BROKEN_PLATFORM) {
            otherBody.setRigidBodyType(RigidBodyType.BROKEN_PLATFORM);
            otherBody.setGravity(SUBJECT_TO_GRAVITY);
        } else {
            applyJump(playerBody);
        }
    }

    private void applyJump(RigidBody rigidBody) {
        rigidBody.addForce(new Point2D(0, -rigidBody.getVelocity().getY()).add(JUMP_FORCE));
    }

    private boolean isBodyIntersectionScreenMiddleLineFitsForScrolling(RigidBody playerBody) {
        return playerBody.getCollider().getCenter().getY() <= MIDDLE_SCREEN_LINE_Y_COORDINATE
                && playerBody.getVelocity().getY() <= 0;
    }

    private void applyScreenScrolling(RigidBody playerRigidBody, List<RigidBody> otherBodies) {
        double playerYVelocity = playerRigidBody.getVelocity().getY();
        boolean isPlatformsStoppedMoving = otherBodies.get(0).getVelocity().getY() < 0;
        if ((playerYVelocity <= SUFFICIENT_Y_VELOCITY_FOR_SCROLLING
                || !playerRigidBody.isGravity()) && !isPlatformsStoppedMoving) {
            updateGameScore(otherBodies.get(0).getVelocity().getY());
            playerRigidBody.setGravity(!SUBJECT_TO_GRAVITY);
            playerRigidBody.compensateYVelocity();
            otherBodies.forEach(otherBody -> {
                otherBody.addForce(new Point2D(0, -playerYVelocity));
                otherBody.addForce(Point2D.ZERO.subtract(PLAYER_GRAVITY_FORCE));
            });
        } else {
            stopScreenScrolling(playerRigidBody, otherBodies);
        }
    }

    private void stopScreenScrolling(RigidBody playerRigidBody, List<RigidBody> otherBodies) {
        otherBodies.forEach(otherBody -> {
            if (otherBody.getRigidBodyType() != RigidBodyType.BROKEN_PLATFORM) {
                otherBody.compensateYVelocity();
            }
        });
        playerRigidBody.setGravity(SUBJECT_TO_GRAVITY);
    }

    private void updateGameScore(double playerYVelocity) {
        gameScore += playerYVelocity;
    }

    private void applyPlatformMove(RigidBody rigidBody) {
        if (rigidBody.getVelocity().getX() == 0) {
            getRandomPlatformMovingDirection().applyMoving(rigidBody);
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

    public int getGameScore() {
        return this.gameScore;
    }
}
