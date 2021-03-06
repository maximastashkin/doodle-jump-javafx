package ru.rsreu.doodle.logic;

import javafx.geometry.Point2D;

public class Collider {
    private final static float PLATFORM_COLLIDER_BUFFER_HEIGHT = 13f;
    private final Point2D extend;
    private Point2D position;
    private float rotation;

    public Collider(Point2D position, Point2D extend) {
        this.position = position;
        this.extend = extend;
        this.rotation = 0;
    }

    public boolean isCollideWith(Collider other) {
        double deltaX = this.position.getX() - other.position.getX();
        double deltaY = this.position.getY() - other.position.getY();
        if (-deltaX > this.extend.getX() || deltaX > other.extend.getX()) {
            return false;
        }
        return !(-deltaY > this.extend.getY()) && !(deltaY > other.extend.getY());
    }

    public boolean isUpperCollideWith(Collider other) {
        double thisLeftBottomY = this.position.getY() + this.extend.getY();
        double otherLeftBottomY = other.position.getY() + other.extend.getY();
        return thisLeftBottomY > other.position.getY() && thisLeftBottomY <
                otherLeftBottomY + PLATFORM_COLLIDER_BUFFER_HEIGHT;
    }

    public void move(Point2D direction) {
        this.position = this.position.add(direction);
    }

    public Point2D getCenter() {
        return new Point2D(this.position.getX() + this.extend.getX() / 2,
                this.position.getY() + this.extend.getY() / 2);
    }

    public Point2D getPosition() {
        return position;
    }

    public void setPosition(Point2D position) {
        this.position = position;
    }

    public Point2D getPositionAddExtend() {
        return this.position.add(extend);
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }
}
