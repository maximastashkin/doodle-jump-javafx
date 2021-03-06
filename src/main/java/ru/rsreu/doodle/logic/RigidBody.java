package ru.rsreu.doodle.logic;

import javafx.geometry.Point2D;

public class RigidBody {
    private final Collider collider;
    private RigidBodyType rigidBodyType;
    private Point2D velocity;
    private boolean isGravity;

    public RigidBody(Collider collider, RigidBodyType rigidBodyType, Point2D startVelocity) {
        this.collider = collider;
        this.rigidBodyType = rigidBodyType;
        this.velocity = startVelocity;
        this.isGravity = rigidBodyType == RigidBodyType.PLAYER;
    }

    public void addForce(Point2D force) {
        this.velocity = velocity.add(force);
    }

    public void subtractForce(Point2D force) {
        this.velocity = velocity.subtract(force);
    }

    public void compensateXVelocity() {
        this.velocity = new Point2D(0, velocity.getY());
    }

    public void compensateYVelocity() {
        this.velocity = new Point2D(velocity.getX(), 0);
    }

    public boolean isFalling(float fallingSpeed) {
        return this.velocity.getY() > fallingSpeed;
    }

    public Collider getCollider() {
        return this.collider;
    }

    public RigidBodyType getRigidBodyType() {
        return this.rigidBodyType;
    }

    public void setRigidBodyType(RigidBodyType rigidBodyType) {
        this.rigidBodyType = rigidBodyType;
    }

    public boolean isGravity() {
        return this.isGravity;
    }

    public void setGravity(boolean gravity) {
        isGravity = gravity;
    }

    public Point2D getVelocity() {
        return this.velocity;
    }
}
