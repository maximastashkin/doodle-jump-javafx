package ru.rsreu.doodle.renderer;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import ru.rsreu.doodle.logic.Collider;
import ru.rsreu.doodle.model.GameObject;

import java.util.List;

public class Renderer {
    private final Canvas canvas;
    private final GraphicsContext graphicsContext;
    private final Image backgroundSprite;

    private final List<GameObject> gameObjects;

    public Renderer(Canvas canvas, Image backgroundSprite, List<GameObject> gameObjects) {
        this.canvas = canvas;
        this.graphicsContext = canvas.getGraphicsContext2D();
        this.backgroundSprite = backgroundSprite;
        this.gameObjects = gameObjects;
    }

    public void render() {
        this.prepare();
        this.graphicsContext.save();
        this.drawBackground();
        gameObjects.forEach(this::drawGameObject);
        graphicsContext.restore();
    }

    private void prepare() {
        this.graphicsContext.setFill(Color.WHITE);
        this.graphicsContext.fillRect(0, 0, this.canvas.getWidth(), this.canvas.getHeight());
    }

    private void drawBackground() {
        if (this.backgroundSprite != null) {
            this.graphicsContext.drawImage(this.backgroundSprite, 0, 0);
        }
    }

    private void drawGameObject(GameObject gameObject) {
        Point2D position = gameObject.getDrawPosition();
        this.graphicsContext.setFill(Color.LIGHTGREEN);
        transformContext(gameObject.getRigidBody().getCollider());
        this.graphicsContext.drawImage(
                gameObject.getObjectSprite(),
                position.getX(),
                position.getY(),
                gameObject.getWidth(),
                gameObject.getHeight()
        );
    }

    private void transformContext(Collider collider) {
        Rotate rotate = new Rotate(collider.getRotation(), collider.getCenter().getX(), collider.getCenter().getY());
        rotate.setAxis(new Point3D(0, 1, 0));
        graphicsContext.setTransform(
                rotate.getMxx(), rotate.getMyx(), rotate.getMxy(), rotate.getMyy(), rotate.getTx(), rotate.getTy());
    }
}
