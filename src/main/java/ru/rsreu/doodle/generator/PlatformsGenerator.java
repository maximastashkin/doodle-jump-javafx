package ru.rsreu.doodle.generator;

import javafx.geometry.Point2D;
import ru.rsreu.doodle.GameApplication;
import ru.rsreu.doodle.controller.GameController;
import ru.rsreu.doodle.logic.RigidBodyType;
import ru.rsreu.doodle.model.GameObject;

import java.util.ArrayList;
import java.util.List;

public class PlatformsGenerator {
    private final static float LEFT_GENERATION_BOUND = 0;
    private final static float RIGHT_GENERATION_BOUND = GameApplication.WINDOW_WIDTH -
            (float) RigidBodyType.PLATFORM.getSprite().getWidth() * GameController.PLATFORM_SPRITE_SCALE;
    private final static float BOTTOM_GENERATION_BOUND = GameApplication.WINDOW_HEIGHT;
    private final static float STANDARD_JUMP_HEIGHT = 182.5f;

    private final static float BREAKING_PLATFORM_PROBABILITY = 0.2f;
    private final static float MOVING_PLATFORM_PROBABILITY = 0.5f;

    public List<GameObject> generateNextPlatforms(int quantity) {
        List<GameObject> generatedPlatforms = new ArrayList<>();
        double currentBottomGenerationBound = BOTTOM_GENERATION_BOUND;
        for (int i = 0; i < quantity; i++) {
            double currentUpperGenerationBound = currentBottomGenerationBound - STANDARD_JUMP_HEIGHT;
            Point2D position = generatePosition(currentBottomGenerationBound, currentUpperGenerationBound);
            RigidBodyType platformType = generatePlatformType();
            generatedPlatforms.add(generatePlatform(platformType, position));
            if (platformType == RigidBodyType.BREAKING_PLATFORM) {
                position = generatePosition(currentBottomGenerationBound, currentUpperGenerationBound);
                generatedPlatforms.add(generatePlatform(RigidBodyType.PLATFORM, position));
                quantity--;
            }
            currentBottomGenerationBound = position.getY();
        }
        return generatedPlatforms;
    }

    private Point2D generatePosition(double bottomGenerationBound, double upperGenerationBound) {
        return new Point2D(
                LEFT_GENERATION_BOUND + (RIGHT_GENERATION_BOUND - LEFT_GENERATION_BOUND) * Math.random(),
                bottomGenerationBound + (upperGenerationBound - bottomGenerationBound) *
                        Math.random()
        );
    }

    private RigidBodyType generatePlatformType() {
        double probability = Math.random();
        if (probability <= BREAKING_PLATFORM_PROBABILITY) {
            return RigidBodyType.BREAKING_PLATFORM;
        } else if (probability <= MOVING_PLATFORM_PROBABILITY) {
            return RigidBodyType.MOVING_PLATFORM;
        } else {
            return RigidBodyType.PLATFORM;
        }
    }

    private GameObject generatePlatform(RigidBodyType platformType, Point2D position) {
        return new GameObject(
                platformType.getSprite(),
                platformType,
                GameController.PLATFORM_SPRITE_SCALE,
                position
        );
    }

    public GameObject generateStartPlatform() {
        return new GameObject(
                RigidBodyType.PLATFORM.getSprite(),
                RigidBodyType.PLATFORM,
                GameController.PLATFORM_SPRITE_SCALE,
                new Point2D(175, 680)
        );
    }
}
