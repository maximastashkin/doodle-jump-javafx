package ru.rsreu.doodle.physic;

import javafx.geometry.Point2D;

public enum PlatformMovingDirection {
    RIGHT {
        @Override
        public void applyMoving(RigidBody rigidBody) {
            rigidBody.addForce(PlatformMovingDirection.getReverseForce(rigidBody).add(MOVE_PLATFORM_FORCE));
        }
    }, LEFT {
        @Override
        public void applyMoving(RigidBody rigidBody) {
            rigidBody.addForce(PlatformMovingDirection.getReverseForce(rigidBody).subtract(MOVE_PLATFORM_FORCE));
        }
    };

    private final static Point2D MOVE_PLATFORM_FORCE = new Point2D(2, 0);

    public abstract void applyMoving(RigidBody rigidBody);

    private static Point2D getReverseForce(RigidBody rigidBody) {
        return new Point2D(-rigidBody.getVelocity().getX(), 0);
    }
}
