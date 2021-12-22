package ru.rsreu.doodle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.rsreu.doodle.control.KeyPolling;

import java.io.IOException;
import java.util.Objects;

public class GameApplication extends Application {
    public final static float WINDOW_WIDTH = 450;
    public final static float WINDOW_HEIGHT = 750;

    @Override
    public void start(Stage stage) throws IOException {
        Scene scene = initialiseScene();
        KeyPolling.getInstance().pollScene(scene);
        configureStage(stage, scene);
        stage.show();
    }

    private void configureStage(Stage stage, Scene scene) {
        stage.setScene(scene);
        stage.setMaximized(false);
        stage.setResizable(false);
        stage.setTitle("Doodle Jump");
    }

    private Scene initialiseScene() throws IOException {
        Scene scene = loadGameScene();
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/style.css")).toExternalForm());
        return scene;
    }

    public static void main(String[] args) {
        launch();
    }

    private Scene loadGameScene() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GameApplication.class.getResource("/fxml/main-game-view.fxml"));
        return new Scene(fxmlLoader.load(), WINDOW_WIDTH, WINDOW_HEIGHT);
    }
}