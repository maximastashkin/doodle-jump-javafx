package ru.rsreu.doodle.control;

import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import ru.rsreu.doodle.physic.PhysicEngine;
import ru.rsreu.doodle.physic.RigidBody;

public class PlayerControl {
    private final static Point2D MOVEMENT_FORCE = new Point2D(5f, 0);

    private final KeyPolling keyPolling = KeyPolling.getInstance();

    private PlayerControl() {

    }

    public static PlayerControl getInstance() {
        return new PlayerControl();
    }

    public void controlPlayer(RigidBody playerRigidBody) {
        if (!(keyPolling.isDown(KeyCode.RIGHT) || keyPolling.isDown(KeyCode.LEFT))) {
            playerRigidBody.compensateXVelocity();
        } else {
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
        if (keyPolling.isDown(KeyCode.SPACE)) {
            PhysicEngine.JUMP_FORCE = new Point2D(0, -12);
        }
        if (keyPolling.isDown(KeyCode.BACK_SPACE)) {
            PhysicEngine.JUMP_FORCE = new Point2D(0, -20);
        }
    }
}
