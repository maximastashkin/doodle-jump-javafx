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
        return !((-deltaY > this.extend.getY()) && !(deltaY > other.extend.getY()) ||
                -deltaX > this.extend.getX() || deltaX > other.extend.getX());
    }
}
