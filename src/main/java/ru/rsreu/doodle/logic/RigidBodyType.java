package ru.rsreu.doodle.logic;

import javafx.scene.image.Image;
import ru.rsreu.doodle.controller.GameController;

import java.util.Objects;

public enum RigidBodyType {
    PLAYER(new Image(
            Objects.requireNonNull(GameController.class.getResourceAsStream("/img/doodler.png")))),
    JUMPING_PLAYER(new Image(
            Objects.requireNonNull(GameController.class.getResourceAsStream("/img/jumping_doodler.png")))),
    PLATFORM(new Image(
            Objects.requireNonNull(GameController.class.getResourceAsStream("/img/platform.png")))),
    MOVING_PLATFORM(new Image(
            Objects.requireNonNull(GameController.class.getResourceAsStream("/img/moving_platform.png")))),
    BREAKING_PLATFORM(new Image(
                    Objects.requireNonNull(GameController.class.getResourceAsStream("/img/breaking_platform.png")))),
    BROKEN_PLATFORM(new Image(
            Objects.requireNonNull(GameController.class.getResourceAsStream("/img/broken_platform.png")))),;

    private final Image sprite;

    RigidBodyType(Image sprite) {
        this.sprite = sprite;
    }

    public Image getSprite() {
        return sprite;
    }
}
