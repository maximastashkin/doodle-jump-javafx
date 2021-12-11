package ru.rsreu.doodle_game.model;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;

public class GameObject {
    private Point2D position;
    private float rotation;
    private float scale = 1;
    private final double width;
    private final double height;

    private final Image objectSprite;

    public GameObject(Image objectSprite) {
        this.objectSprite = objectSprite;
        this.width = objectSprite.getWidth();
        this.height = objectSprite.getHeight();
    }

    public Point2D getPosition() {
        return this.position;
    }

    public void setPosition(float x, float y) {
        this.position = new Point2D(x, y);
    }
    // collider have position
    // rigidbody have collider
    // game_object have rigidbody

    public void rotate(float rotation) {
        this.rotation += rotation;
    }

    public void move(Point2D vector) {
        this.position = this.position.add(vector);
    }

    public float getRotation() {
        return this.rotation;
    }

    public float getScale() {
        return this.scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public Point2D getCenter() {
        Point2D position = this.getPosition();
        return new Point2D(position.getX() + this.width / 2, position.getY() + this.height / 2);
    }

    public Image getObjectSprite() {
        return this.objectSprite;
    }

    public double getWidth() {
        return this.width * this.scale;
    }

    public double getHeight() {
        return this.height * this.scale;
    }
}
