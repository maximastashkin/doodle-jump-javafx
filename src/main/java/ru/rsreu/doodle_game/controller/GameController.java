package ru.rsreu.doodle_game.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import ru.rsreu.doodle_game.control.KeyPolling;
import ru.rsreu.doodle_game.loop.GameLoopTimer;
import ru.rsreu.doodle_game.model.GameObject;
import ru.rsreu.doodle_game.renderer.Renderer;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class GameController implements Initializable {
    private final static Image BACKGROUND_IMAGE =
            new Image(Objects.requireNonNull(GameController.class.getResourceAsStream("/img/background.png")));

    @FXML
    public AnchorPane gameAnchor;

    @FXML
    public Canvas gameCanvas;

    private final GameObject player = new GameObject(
            new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/doodler.png"))));

    private final KeyPolling keyPolling = KeyPolling.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        initialiseCanvas();
        player.setPosition(200, 500);
        Renderer renderer = new Renderer(this.gameCanvas, BACKGROUND_IMAGE);
        player.setScale(0.4f);
        renderer.addGameObject(player);

        GameLoopTimer gameLoopTimer = new GameLoopTimer() {
            @Override
            public void tick(float secondSinceLastFrame) {
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
}