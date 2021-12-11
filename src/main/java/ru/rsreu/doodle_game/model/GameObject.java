package ru.rsreu.doodle_game.model;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import ru.rsreu.doodle_game.physic.Collider;
import ru.rsreu.doodle_game.physic.PhysicEngine;
import ru.rsreu.doodle_game.physic.RigidBody;

import java.util.List;

public class GameObject {
    private final RigidBody rigidBody;

    private final float scale;
    private final double width;
    private final double height;

    private final Image objectSprite;

    private final GameObjectType gameObjectType;

    public GameObject(
            Image objectSprite, float scale, Point2D startPosition, GameObjectType gameObjectType) {
        this.objectSprite = objectSprite;
        this.scale = scale;
        this.width = objectSprite.getWidth() * scale;
        this.height = objectSprite.getHeight() * scale;
        this.gameObjectType = gameObjectType;
        this.rigidBody = new RigidBody(
                new Collider(startPosition, new Point2D(width, height)),
                new Point2D(0, 0),
                this.gameObjectType == GameObjectType.PLAYER);
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

    public GameObjectType getGameObjectType() {
        return this.gameObjectType;
    }

    public void update(PhysicEngine physicEngine, List<RigidBody> otherBodies) {
        this.rigidBody.getCollider().move(physicEngine.tryMoveRigidBody(rigidBody, otherBodies));
    }
}
