package ru.rsreu.doodle.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
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
    private final PlayerControl playerControl = PlayerControl.getInstance();
    private final PlatformsGenerator platformsGenerator = new PlatformsGenerator();

    @FXML
    public AnchorPane gameAnchor;
    @FXML
    public Canvas gameCanvas;
    @FXML
    public Label scoreLabel;
    @FXML
    public Button startGameButton;
    @FXML
    public Button pauseGameButton;
    @FXML
    public Button restartGameButton;
    private boolean isGameStarted = false;
    private GameObject player;

    private GameObject getNewPlayer() {
        return new GameObject(RigidBodyType.PLAYER.getSprite(),
                RigidBodyType.PLAYER, DOODLER_SPRITE_SCALE, new Point2D(195, 0));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initialiseCanvas();
        GameLoopTimer gameLoopTimer = getGameLoopTimer();
        startScreenForming(gameLoopTimer);
        restartGameButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> startScreenForming(gameLoopTimer));
        startGameButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> startGame());
        pauseGameButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> pauseGame(gameLoopTimer));
    }

    private void initialiseCanvas() {
        gameCanvas.widthProperty().bind(gameAnchor.widthProperty());
        gameCanvas.heightProperty().bind(gameAnchor.heightProperty());
    }

    private GameLoopTimer getGameLoopTimer() {
        return new GameLoopTimer() {
            private final Renderer renderer = new Renderer(gameCanvas, BACKGROUND_IMAGE, gameObjects);

            @Override
            public void tick(float secondSinceLastFrame) {
                if (isGameStarted) {
                    playerControl.controlPlayer(player.getRigidBody());
                }
                updateGameObjects();
                renderer.render();
                updateGameScore();
                if (isGameOver()) {
                    endGame(this);
                }
            }
        };
    }

    private void startScreenForming(GameLoopTimer gameLoopTimer) {
        scoreLabel.setVisible(false);
        restartGameButton.setVisible(false);
        startGameButton.setVisible(true);

        GAME_LOGIC.resetGameScore();
        player = getNewPlayer();
        gameObjects.clear();
        gameObjects.add(platformsGenerator.generateStartPlatform());
        gameObjects.add(player);
        gameLoopTimer.start();
    }

    private void startGame() {
        isGameStarted = true;
        scoreLabel.setVisible(true);
        this.startGameButton.setVisible(false);
        this.pauseGameButton.setVisible(true);

        gameObjects.remove(player);
        gameObjects.addAll(platformsGenerator.generateNextPlatforms(10000));
        gameObjects.add(player);
    }

    private void pauseGame(GameLoopTimer gameLoopTimer) {
        if (!gameLoopTimer.isPaused()) {
            gameLoopTimer.pause();
            pauseGameButton.setText("Resume");
        } else {
            gameLoopTimer.play();
            pauseGameButton.setText("Pause");
        }
    }

    private boolean isGameOver() {
        return !gameObjects.contains(player);
    }

    private void endGame(GameLoopTimer gameLoopTimer) {
        isGameStarted = false;
        gameLoopTimer.pause();
        scoreLabel.setText("You are lose with score: " + GAME_LOGIC.getGameScore());
        this.pauseGameButton.setVisible(false);
        restartGameButton.setVisible(true);
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