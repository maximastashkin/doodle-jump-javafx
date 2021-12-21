package ru.rsreu.doodle.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import ru.rsreu.doodle.GameApplication;
import ru.rsreu.doodle.control.PlayerControl;
import ru.rsreu.doodle.generator.PlatformsGenerator;
import ru.rsreu.doodle.logic.GameLogic;
import ru.rsreu.doodle.logic.RigidBody;
import ru.rsreu.doodle.logic.RigidBodyType;
import ru.rsreu.doodle.loop.GameLoopTimer;
import ru.rsreu.doodle.model.GameObject;
import ru.rsreu.doodle.renderer.Renderer;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class GameController implements Initializable {
    public final static float DOODLER_SPRITE_NOISE_DELTA = 32f;
    public final static float DOODLER_SPRITE_SCALE = 0.4f;
    public final static float PLATFORM_SPRITE_SCALE = 1.5f;

    private final static Image BACKGROUND_IMAGE =
            new Image(Objects.requireNonNull(GameController.class.getResourceAsStream("/img/background.png")));

    private final static GameLogic GAME_LOGIC = new GameLogic();

    private final List<GameObject> gameObjects = new ArrayList<>();

    private final GameObject player = new GameObject(RigidBodyType.PLAYER.getSprite(),
            RigidBodyType.PLAYER, DOODLER_SPRITE_SCALE, new Point2D(50, 0));

    private final PlayerControl playerControl = PlayerControl.getInstance();

    private final PlatformsGenerator platformsGenerator = new PlatformsGenerator();

    @FXML
    public AnchorPane gameAnchor;
    @FXML
    public Canvas gameCanvas;
    @FXML
    public Label scoreLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initialiseCanvas();
        Renderer renderer = new Renderer(this.gameCanvas, BACKGROUND_IMAGE, gameObjects);
        GameLoopTimer gameLoopTimer = new GameLoopTimer() {
            @Override
            public void tick(float secondSinceLastFrame) {
                playerControl.controlPlayer(player.getRigidBody());
                updateGameObjects();
                renderer.render();
                updateGameScore();
            }
        };
        gameLoopTimer.start();
    }

    private void initialiseCanvas() {
        gameObjects.addAll(platformsGenerator.generateNextPlatforms(10000));
        gameObjects.add(player);
        gameCanvas.widthProperty().bind(gameAnchor.widthProperty());
        gameCanvas.heightProperty().bind(gameAnchor.heightProperty());
    }

    private void updateGameObjects() {
        List<RigidBody> rigidBodiesWithoutPlayer = this.getRigidBodiesWithoutPlayer();
        List<GameObject> gameObjectsForDeleting = new ArrayList<>();
        gameObjects.forEach(gameObject -> {
            if (gameObject.getDrawPosition().getY() > GameApplication.WINDOW_HEIGHT) {
                gameObjectsForDeleting.add(gameObject);
            } else {
                gameObject.update(GAME_LOGIC, rigidBodiesWithoutPlayer);
            }
        });
        deleteGameObjects(gameObjectsForDeleting);
    }

    private void deleteGameObjects(List<GameObject> gameObjectsForDeleting) {
        List<GameObject> buffer = new ArrayList<>(gameObjects);
        gameObjects.clear();
        buffer.forEach(gameObject -> {
            if (!gameObjectsForDeleting.contains(gameObject)) {
                gameObjects.add(gameObject);
            }
        });
    }

    private List<RigidBody> getRigidBodiesWithoutPlayer() {
        return gameObjects
                .stream()
                .map(GameObject::getRigidBody)
                .filter(rigidBody -> rigidBody.getRigidBodyType() != RigidBodyType.PLAYER)
                .collect(Collectors.toList());
    }

    private void updateGameScore() {
        this.scoreLabel.setText(String.valueOf(GAME_LOGIC.getGameScore()));
    }
}