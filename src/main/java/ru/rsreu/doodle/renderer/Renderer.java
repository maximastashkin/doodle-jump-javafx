package ru.rsreu.doodle.renderer;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import ru.rsreu.doodle.model.GameObject;

import java.util.List;

public class Renderer {
    private final Canvas canvas;
    private final GraphicsContext graphicsContext;
    private final Image backgroundSprite;

    private List<GameObject> gameObjects;

    public Renderer(Canvas canvas, Image backgroundSprite, List<GameObject> gameObjects) {
        this.canvas = canvas;
        this.graphicsContext = canvas.getGraphicsContext2D();
        this.backgroundSprite = backgroundSprite;
        this.gameObjects = gameObjects;
    }

    public void setGameObjects(List<GameObject> gameObjects) {
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
            this.graphicsContext.drawImage(this.backgroundSprite, 0 ,0);
        }
    }

    private void drawGameObject(GameObject gameObject) {
        Point2D position = gameObject.getDrawPosition();
        this.graphicsContext.setFill(Color.LIGHTGREEN);
        this.graphicsContext.fillRect(
                gameObject.getDrawPosition().getX(),
                gameObject.getDrawPosition().getY(),
                gameObject.getRigidBody().getCollider().getDebugExtend().getX(),
                gameObject.getRigidBody().getCollider().getDebugExtend().getY()
        );
        this.graphicsContext.drawImage(
                gameObject.getObjectSprite(),
                position.getX(),
                position.getY(),
                gameObject.getWidth(),
                gameObject.getHeight()
        );
    }
}
