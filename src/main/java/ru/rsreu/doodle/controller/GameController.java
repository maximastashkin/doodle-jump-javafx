package ru.rsreu.doodle.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import ru.rsreu.doodle.control.PlayerControl;
import ru.rsreu.doodle.loop.GameLoopTimer;
import ru.rsreu.doodle.model.GameObject;
import ru.rsreu.doodle.physic.RigidBodyType;
import ru.rsreu.doodle.physic.PhysicEngine;
import ru.rsreu.doodle.physic.RigidBody;
import ru.rsreu.doodle.renderer.Renderer;

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
            new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/doodler.png"))), RigidBodyType.PLAYER, 0.4f,
            new Point2D(50, 0));

    private final PlayerControl playerControl = PlayerControl.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        generateDebugPlatforms();
        gameObjects.add(player);
        initialiseCanvas();
        Renderer renderer = new Renderer(this.gameCanvas, BACKGROUND_IMAGE, gameObjects);

        GameLoopTimer gameLoopTimer = new GameLoopTimer() {
            @Override
            public void tick(float secondSinceLastFrame) {
                playerControl.controlPlayer(player.getRigidBody());
                updateGameObjects();
                renderer.render();
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
        gameObjects.forEach(gameObject -> gameObject.update(PHYSIC_ENGINE, rigidBodiesWithoutPlayer));
    }

    private List<RigidBody> getRigidBodiesWithoutPlayer() {
        return gameObjects
                .stream()
                .map(GameObject::getRigidBody)
                .filter(rigidBody -> rigidBody.getRigidBodyType() != RigidBodyType.PLAYER)
                .collect(Collectors.toList());
    }

    private void generateDebugPlatforms() {
        gameObjects.add(new GameObject(
                new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/platform.png"))),
                RigidBodyType.PLATFORM, 1.5f,
                new Point2D(0, 700)
        ));
        gameObjects.add(new GameObject(
                new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/platform.png"))),
                RigidBodyType.PLATFORM, 1.5f,
                new Point2D(300, 0)
        ));
        gameObjects.add(new GameObject(
                new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/platform.png"))),
                RigidBodyType.PLATFORM, 1.5f,
                new Point2D(200, 100)
        ));
        gameObjects.add(new GameObject(
                new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/platform.png"))),
                RigidBodyType.PLATFORM, 1.5f,
                new Point2D(150, 200)
        ));
        gameObjects.add(new GameObject(
                new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/platform.png"))),
                RigidBodyType.PLATFORM, 1.5f,
                new Point2D(160, 250)
        ));
        gameObjects.add(new GameObject(
                new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/platform.png"))),
                RigidBodyType.PLATFORM, 1.5f,
                new Point2D(160, 360)
        ));
        gameObjects.add(new GameObject(
                new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/platform.png"))),
                RigidBodyType.PLATFORM, 1.5f,
                new Point2D(300, 320)
        ));
        gameObjects.add(new GameObject(
                new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/platform.png"))),
                RigidBodyType.PLATFORM, 1.5f,
                new Point2D(100, 700)
        ));
        gameObjects.add(new GameObject(
                new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/platform.png"))),
                RigidBodyType.PLATFORM, 1.5f,
                new Point2D(200, 700)
        ));
        gameObjects.add(new GameObject(
                new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/platform.png"))),
                RigidBodyType.PLATFORM, 1.5f,
                new Point2D(300, 700)
        ));
        gameObjects.add(new GameObject(
                new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/platform.png"))),
                RigidBodyType.PLATFORM, 1.5f,
                new Point2D(400, 700)
        ));
        gameObjects.add(new GameObject(
                new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/moving_platform.png"))),
                RigidBodyType.MOVING_PLATFORM, 1.5f,
                new Point2D(100, 600)
        ));

        gameObjects.add(new GameObject(
                new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/moving_platform.png"))),
                RigidBodyType.MOVING_PLATFORM, 1.5f,
                new Point2D(300, 550)
        ));

        gameObjects.add(new GameObject(
                new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/breaking_platform.png"))),
                RigidBodyType.BREAKING_PLATFORM, 1.5f,
                new Point2D(300, 510)
        ));
    }
}