package ru.rsreu.doodle.model;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import ru.rsreu.doodle.controller.GameController;
import ru.rsreu.doodle.logic.Collider;
import ru.rsreu.doodle.logic.GameLogic;
import ru.rsreu.doodle.logic.RigidBody;
import ru.rsreu.doodle.logic.RigidBodyType;

import java.util.List;
import java.util.Objects;

public class GameObject {
    public final static float SUFFICIENT_Y_VELOCITY_FOR_JUMPING_ANIMATION = 0f;
    private final RigidBody rigidBody;
    private final double width;
    private final double height;
    private Image objectSprite;

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
            spriteSizes = spriteSizes.subtract(GameController.DOODLER_SPRITE_NOISE_DELTA, 0);
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
        if (this.rigidBody.getVelocity().getY() <= SUFFICIENT_Y_VELOCITY_FOR_JUMPING_ANIMATION) {
            this.objectSprite = RigidBodyType.JUMPING_PLAYER.getSprite();
        } else {
            this.objectSprite = RigidBodyType.PLAYER.getSprite();
        }
    }

    private void applyPlatformAnimation() {
        this.objectSprite = this.getRigidBody().getRigidBodyType().getSprite();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameObject that = (GameObject) o;
        return Double.compare(that.width, width) == 0
                && Double.compare(that.height, height) == 0
                && Objects.equals(objectSprite, that.objectSprite)
                && Objects.equals(rigidBody, that.rigidBody);
    }

    @Override
    public int hashCode() {
        return Objects.hash(objectSprite, rigidBody, width, height);
    }
}
