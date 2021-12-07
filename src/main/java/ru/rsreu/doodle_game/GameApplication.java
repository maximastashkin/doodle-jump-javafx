package ru.rsreu.doodle_game;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GameApplication extends Application {
    private final static int WINDOW_WIDTH = 450;
    private final static int WINDOW_HEIGHT = 750;
    @Override
    public void start(Stage stage) throws IOException {
        Scene scene = loadGameScene();
        stage.setScene(scene);

        stage.setMaximized(false);
        stage.setResizable(false);
        stage.setTitle("Doodle Jump");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    private Scene loadGameScene() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GameApplication.class.getResource("/fxml/main-game-view.fxml"));
        return new Scene(fxmlLoader.load(), WINDOW_WIDTH, WINDOW_HEIGHT);
    }
}