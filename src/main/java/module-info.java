module ru.rsreu.doodle_game {
    requires javafx.controls;
    requires javafx.fxml;


    opens ru.rsreu.doodle_game to javafx.fxml;
    exports ru.rsreu.doodle_game;
    exports ru.rsreu.doodle_game.controller;
    opens ru.rsreu.doodle_game.controller to javafx.fxml;
}