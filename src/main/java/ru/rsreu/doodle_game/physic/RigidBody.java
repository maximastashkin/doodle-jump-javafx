package ru.rsreu.doodle_game.physic;

import javafx.geometry.Point2D;

public class RigidBody {
    private final Collider collider;
    private Point2D velocity;
    private final boolean isGravity;

    public RigidBody(Collider collider, Point2D startVelocity, boolean isGravity) {
        this.collider = collider;
        this.velocity = startVelocity;
        this.isGravity = isGravity;
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

    public boolean isGravity() {
        return this.isGravity;
    }
}
