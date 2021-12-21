package ru.rsreu.doodle.control;

import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import ru.rsreu.doodle.physic.PhysicEngine;
import ru.rsreu.doodle.physic.RigidBody;

public class PlayerControl {
    private final float PLAYER_RIGHT_ROTATION = 0f;
    private final float PLAYER_LEFT_ROTATION = 180f;

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
                playerRigidBody.getCollider().setRotation(PLAYER_RIGHT_ROTATION);
                if (playerRigidBody.getVelocity().getX() <= 0) {
                    playerRigidBody.addForce(PhysicEngine.MOVEMENT_FORCE);
                }
            }
            if (keyPolling.isDown(KeyCode.LEFT)) {
                playerRigidBody.getCollider().setRotation(PLAYER_LEFT_ROTATION);
                if (playerRigidBody.getVelocity().getX() >= 0) {
                    playerRigidBody.subtractForce(PhysicEngine.MOVEMENT_FORCE);
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
