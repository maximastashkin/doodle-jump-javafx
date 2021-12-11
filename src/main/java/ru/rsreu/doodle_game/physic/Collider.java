package ru.rsreu.doodle_game.physic;

import javafx.geometry.Point2D;

public class Collider {
    private Point2D position;
    private Point2D extend;

    public Collider(Point2D position, Point2D extend) {
        this.position = position;
        this.extend = extend;
    }

    public Point2D getPosition() {
        return position;
    }

    public void setPosition(Point2D position) {
        this.position = position;
    }

    public boolean isCollideWith(Collider other) {
        double deltaX = this.position.getX() - other.position.getX();
        double deltaY = this.position.getY() - other.position.getY();
        if (-deltaX > this.extend.getX() || deltaX > other.extend.getX()) {
            return false;
        }
        if (-deltaY > this.extend.getY() || deltaY > other.extend.getY()) {
            return false;
        }
        return true;
    }

    public Point2D getDebugExtend() {
        return this.extend;
    }

    public void move(Point2D direction) {
        this.position = this.position.add(direction);
    }
}
