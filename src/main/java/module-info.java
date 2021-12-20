module ru.rsreu.doodle_game {
    requires javafx.controls;
    requires javafx.fxml;


    opens ru.rsreu.doodle to javafx.fxml;
    exports ru.rsreu.doodle;
    exports ru.rsreu.doodle.controller;
    exports ru.rsreu.doodle.physic;
    exports ru.rsreu.doodle.model;
    opens ru.rsreu.doodle.controller to javafx.fxml;
}