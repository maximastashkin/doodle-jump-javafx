package ru.rsreu.doodle.model;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import ru.rsreu.doodle.controller.GameController;
import ru.rsreu.doodle.physic.Collider;
import ru.rsreu.doodle.physic.PhysicEngine;
import ru.rsreu.doodle.physic.RigidBody;
import ru.rsreu.doodle.physic.RigidBodyType;

import java.util.List;

public class GameObject {
    private Image objectSprite;
    private final RigidBody rigidBody;
    private final float scale;
    private final double width;
    private final double height;

    public GameObject(
            Image objectSprite, RigidBodyType rigidBodyType, float scale, Point2D startPosition) {
        this.objectSprite = objectSprite;
        this.scale = scale;
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
            spriteSizes = spriteSizes.subtract(32, 0);
        }
        return spriteSizes;
    }

    public void update(PhysicEngine physicEngine, List<RigidBody> otherBodies) {
        this.rigidBody.getCollider().move(physicEngine.tryMoveRigidBody(rigidBody, otherBodies));
        applyAnimation();
    }

    private void applyAnimation() {
        if (this.rigidBody.getRigidBodyType() == RigidBodyType.PLAYER) {
            if (!this.rigidBody.isFalling(PhysicEngine.FALLING_SPEED)) {
                this.objectSprite = GameController.JUMPING_DOODLER_SPRITE;
            } else {
                this.objectSprite = GameController.DOODLER_SPRITE;
            }
        }
        if (this.rigidBody.getRigidBodyType() == RigidBodyType.BROKEN_PLATFORM) {
            this.objectSprite = GameController.BROKEN_PLATFORM_SPRITE;
        }
    }

    public Image getObjectSprite() {
        return this.objectSprite;
    }

    public RigidBody getRigidBody() {
        return this.rigidBody;
    }

    public float getScale() {
        return this.scale;
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
