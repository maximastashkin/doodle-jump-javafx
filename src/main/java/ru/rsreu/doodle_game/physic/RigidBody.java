package ru.rsreu.doodle_game.physic;

import javafx.geometry.Point2D;

public class RigidBody {
    private final Collider collider;
    private Point2D velocity;
    private boolean isGravity;
    private RigidBodyType rigidBodyType;

    public RigidBody(Collider collider, Point2D startVelocity, RigidBodyType rigidBodyType) {
        this.collider = collider;
        this.velocity = startVelocity;
        this.rigidBodyType = rigidBodyType;
        this.isGravity = rigidBodyType == RigidBodyType.PLAYER;
    }

    public Collider getCollider() {
        return this.collider;
    }

    public Point2D getVelocity() {
        return this.velocity;
    }

    public void addForce(Point2D force) {
        this.velocity = velocity.add(force);
    }

    public void subtractForce(Point2D force) {
        this.velocity = velocity.subtract(force);
    }

    public boolean isGravity() {
        return this.isGravity;
    }

    public RigidBodyType getRigidBodyType() {
        return rigidBodyType;
    }

    public void compensateXVelocity() {
        this.velocity = new Point2D(0, velocity.getY());
    }
}
