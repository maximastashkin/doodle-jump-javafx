package ru.rsreu.doodle.model;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import ru.rsreu.doodle.controller.GameController;
import ru.rsreu.doodle.logic.Collider;
import ru.rsreu.doodle.logic.GameLogic;
import ru.rsreu.doodle.logic.RigidBody;
import ru.rsreu.doodle.logic.RigidBodyType;

import java.util.List;

public class GameObject {
    private Image objectSprite;
    private final RigidBody rigidBody;
    private final double width;
    private final double height;

    public GameObject(
            Image objectSprite, RigidBodyType rigidBodyType, float scale, Point2D startPosition) {
        this.objectSprite = objectSprite;
        this.width = objectSprite.getWidth() * scale;
        this.height = objectSprite.getHeight() * scale;
        this.rigidBody = new RigidBody(
                new Collider(startPosition, calculateColliderSizes(rigidBodyType)),
                rigidBodyType, new Point2D(0, 0)
        );
    }

    private Point2D calculateColliderSizes(RigidBodyType rigidBodyType) {
        Point2D spriteSizes = new Point2D(this.width, this.height);
        if (rigidBodyType == RigidBodyType.PLAYER) {
            spriteSizes = spriteSizes.subtract(GameController.PLAYER_SPRITE_NOISE_DELTA, 0);
        }
        return spriteSizes;
    }

    public void update(GameLogic gameLogic, List<RigidBody> otherBodies) {
        this.rigidBody.getCollider().move(gameLogic.tryMoveRigidBody(rigidBody, otherBodies));
        applyAnimation();
    }

    private void applyAnimation() {
        if (this.rigidBody.getRigidBodyType() == RigidBodyType.PLAYER) {
            applyPlayerAnimation();
        }
        if (this.rigidBody.getRigidBodyType() == RigidBodyType.BROKEN_PLATFORM) {
            applyPlatformAnimation();
        }
    }

    private void applyPlayerAnimation() {
        if (!this.rigidBody.isFalling(GameLogic.FALLING_SPEED)) {
            this.objectSprite = GameController.JUMPING_DOODLER_SPRITE;
        } else {
            this.objectSprite = GameController.DOODLER_SPRITE;
        }
    }

    private void applyPlatformAnimation() {
        this.objectSprite = GameController.BROKEN_PLATFORM_SPRITE;
    }

    public Image getObjectSprite() {
        return this.objectSprite;
    }

    public RigidBody getRigidBody() {
        return this.rigidBody;
    }


    public double getWidth() {
        return this.width;
    }

    public double getHeight() {
        return this.height;
    }

    public Point2D getDrawPosition() {
        return this.rigidBody.getCollider().getPosition();
    }
}
