package ru.rsreu.doodle_game.model;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import ru.rsreu.doodle_game.physic.Collider;
import ru.rsreu.doodle_game.physic.PhysicEngine;
import ru.rsreu.doodle_game.physic.RigidBody;
import ru.rsreu.doodle_game.physic.RigidBodyType;

import java.util.List;

public class GameObject {
    private final RigidBody rigidBody;

    private final float scale;
    private final double width;
    private final double height;

    private final Image objectSprite;

    public GameObject(
            Image objectSprite, float scale, Point2D startPosition, RigidBodyType rigidBodyType) {
        this.objectSprite = objectSprite;
        this.scale = scale;
        this.width = objectSprite.getWidth() * scale;
        this.height = objectSprite.getHeight() * scale;
        this.rigidBody = new RigidBody(
                new Collider(startPosition, calculateColliderSizes(rigidBodyType)),
                new Point2D(0, 0),
                rigidBodyType);
    }

    public RigidBody getRigidBody() {
        return this.rigidBody;
    }

    public float getScale() {
        return this.scale;
    }

    public Image getObjectSprite() {
        return this.objectSprite;
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

    public Point2D getDebugColliderExtendPoint() {
        return this.rigidBody.getCollider().getDebugExtend();
    }

    public void update(PhysicEngine physicEngine, List<RigidBody> otherBodies) {
        this.rigidBody.getCollider().move(physicEngine.tryMoveRigidBody(rigidBody, otherBodies));
    }

    private Point2D calculateColliderSizes(RigidBodyType rigidBodyType) {
        Point2D spriteSizes = new Point2D(this.width, this.height);
        if (rigidBodyType == RigidBodyType.PLAYER) {
            spriteSizes = spriteSizes.subtract(32, 0);
        }
        return spriteSizes;
    }
}
