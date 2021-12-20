package ru.rsreu.doodle.control;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

import java.util.HashSet;
import java.util.Set;

public class KeyPolling {
    private final static Set<KeyCode> KEY_CURRENTLY_DOWN = new HashSet<>();

    private static Scene scene;

    private KeyPolling() {
    }

    public static KeyPolling getInstance() {
        return new KeyPolling();
    }

    public void pollScene(Scene scene) {
        clearKeys();
        removeCurrentKeyHandlers();
        setScene(scene);
    }

    private void clearKeys() {
        KEY_CURRENTLY_DOWN.clear();
    }

    private void removeCurrentKeyHandlers() {
        if (scene != null) {
            KeyPolling.scene.setOnKeyPressed(null);
            KeyPolling.scene.setOnKeyReleased(null);
        }
    }

    private void setScene(Scene scene) {
        KeyPolling.scene = scene;
        if (scene != null) {
            scene.setOnKeyPressed(keyEvent -> KEY_CURRENTLY_DOWN.add(keyEvent.getCode()));
            scene.setOnKeyReleased(keyEvent -> KEY_CURRENTLY_DOWN.remove(keyEvent.getCode()));
        }
    }

    public boolean isDown(KeyCode keyCode) {
        return KEY_CURRENTLY_DOWN.contains(keyCode);
    }

    public boolean isClear() {
        return KEY_CURRENTLY_DOWN.isEmpty();
    }

    @Override
    public String toString() {
        StringBuilder keysDown = new StringBuilder("KeyPolling on scene (").append(scene).append("):");
        KEY_CURRENTLY_DOWN.forEach (keyCode -> keysDown.append((keyCode.getName())).append(" "));
        return keysDown.toString();
    }
}
