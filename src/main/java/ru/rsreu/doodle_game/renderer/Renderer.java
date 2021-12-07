package ru.rsreu.doodle_game.renderer;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import ru.rsreu.doodle_game.model.GameObject;

import java.util.ArrayList;
import java.util.List;

public class Renderer {
    private final Canvas canvas;
    private final GraphicsContext graphicsContext;

    private final Image backgroundSprite;

    private final List<GameObject> gameObjects = new ArrayList<>();

    public Renderer(Canvas canvas, Image backgroundSprite) {
        this.canvas = canvas;
        this.graphicsContext = canvas.getGraphicsContext2D();
        this.backgroundSprite = backgroundSprite;
    }

    public void addGameObject(GameObject gameObject) {
        this.gameObjects.add(gameObject);
    }

    public void removeGameObject(GameObject gameObject) {
        this.gameObjects.remove(gameObject);
    }

    public void clearGameObjects() {
        this.gameObjects.clear();
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
        Point2D position = gameObject.getPosition();
        this.graphicsContext.drawImage(
                gameObject.getObjectSprite(),
                position.getX(),
                position.getY(),
                gameObject.getWidth(),
                gameObject.getHeight()
        );
    }


}
