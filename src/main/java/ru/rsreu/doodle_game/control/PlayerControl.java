package ru.rsreu.doodle_game.control;

import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import ru.rsreu.doodle_game.physic.RigidBody;

public class PlayerControl {
    private final static Point2D MOVEMENT_FORCE = new Point2D(5f, 0);

    private final KeyPolling keyPolling = KeyPolling.getInstance();

    private PlayerControl() {

    }

    public static PlayerControl getInstance() {
        return new PlayerControl();
    }

    public void controlPlayer(RigidBody playerRigidBody) {
        if (keyPolling.isClear()) {
            playerRigidBody.compensateXVelocity();
        }
        if (keyPolling.isDown(KeyCode.RIGHT)) {
            if (playerRigidBody.getVelocity().getX() <= 0) {
                playerRigidBody.addForce(MOVEMENT_FORCE);
            }
        }
        if (keyPolling.isDown(KeyCode.LEFT)) {
            if (playerRigidBody.getVelocity().getX() >= 0) {
                playerRigidBody.subtractForce(MOVEMENT_FORCE);
            }
        }
    }
}
