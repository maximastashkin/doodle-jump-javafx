package ru.rsreu.doodle_game.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import ru.rsreu.doodle_game.control.KeyPolling;
import ru.rsreu.doodle_game.loop.GameLoopTimer;
import ru.rsreu.doodle_game.model.GameObject;
import ru.rsreu.doodle_game.model.GameObjectType;
import ru.rsreu.doodle_game.physic.PhysicEngine;
import ru.rsreu.doodle_game.physic.RigidBody;
import ru.rsreu.doodle_game.renderer.Renderer;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class GameController implements Initializable {
    private final static Image BACKGROUND_IMAGE =
            new Image(Objects.requireNonNull(GameController.class.getResourceAsStream("/img/background.png")));
    private final static PhysicEngine PHYSIC_ENGINE = new PhysicEngine();

    @FXML
    public AnchorPane gameAnchor;

    @FXML
    public Canvas gameCanvas;

    private final List<GameObject> gameObjects = new ArrayList<>();

    private final GameObject player = new GameObject(
            new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/doodler.png"))), 0.4f,
            new Point2D(20, 0), GameObjectType.PLAYER);

    private final KeyPolling keyPolling = KeyPolling.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GameObject platform = new GameObject(
                new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/platform.png"))), 1.5f,
                new Point2D(0, 500), GameObjectType.PLATFORM
        );
        gameObjects.add(platform);
        gameObjects.add(player);
        initialiseCanvas();
        Renderer renderer = new Renderer(this.gameCanvas, BACKGROUND_IMAGE, gameObjects);

        GameLoopTimer gameLoopTimer = new GameLoopTimer() {
            @Override
            public void tick(float secondSinceLastFrame) {
                updateGameObjects();
                renderer.render();
                System.out.println(keyPolling);
            }
        };
        gameLoopTimer.start();
    }

    private void initialiseCanvas() {
        gameCanvas.widthProperty().bind(gameAnchor.widthProperty());
        gameCanvas.heightProperty().bind(gameAnchor.heightProperty());
    }

    private void updateGameObjects() {
        List<RigidBody> rigidBodiesWithoutPlayer = this.getRigidBodiesWithoutPlayer();
        //gameObjects.forEach(gameObject -> gameObject.update(PHYSIC_ENGINE, rigidBodiesWithoutPlayer));
        player.update(PHYSIC_ENGINE, rigidBodiesWithoutPlayer);
    }

    private List<RigidBody> getRigidBodiesWithoutPlayer() {
        return gameObjects
                .stream()
                .filter(gameObject -> gameObject.getGameObjectType() != GameObjectType.PLAYER)
                .map(GameObject::getRigidBody).collect(Collectors.toList());
    }
}