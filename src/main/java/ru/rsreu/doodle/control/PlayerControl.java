package ru.rsreu.doodle.control;

import javafx.scene.input.KeyCode;
import ru.rsreu.doodle.logic.GameLogic;
import ru.rsreu.doodle.logic.RigidBody;

public class PlayerControl {
    private final KeyPolling keyPolling = KeyPolling.getInstance();

    private PlayerControl() {

    }

    public static PlayerControl getInstance() {
        return new PlayerControl();
    }

    public void controlPlayer(RigidBody playerRigidBody) {
        if (!(keyPolling.isDown(KeyCode.D) || keyPolling.isDown(KeyCode.A))
                || keyPolling.isDown(KeyCode.LEFT) || keyPolling.isDown(KeyCode.RIGHT)) {
            playerRigidBody.compensateXVelocity();
        } else {
            if (keyPolling.isDown(KeyCode.D) || keyPolling.isDown(KeyCode.RIGHT)) {
                float PLAYER_RIGHT_ROTATION = 0f;
                playerRigidBody.getCollider().setRotation(PLAYER_RIGHT_ROTATION);
                if (playerRigidBody.getVelocity().getX() <= 0) {
                    playerRigidBody.addForce(GameLogic.MOVEMENT_FORCE);
                }
            }
            if (keyPolling.isDown(KeyCode.A) || keyPolling.isDown(KeyCode.LEFT)) {
                float PLAYER_LEFT_ROTATION = 180f;
                playerRigidBody.getCollider().setRotation(PLAYER_LEFT_ROTATION);
                if (playerRigidBody.getVelocity().getX() >= 0) {
                    playerRigidBody.subtractForce(GameLogic.MOVEMENT_FORCE);
                }
            }
        }
    }
}
