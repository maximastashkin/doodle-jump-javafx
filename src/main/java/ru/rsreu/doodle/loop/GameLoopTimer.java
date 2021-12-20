package ru.rsreu.doodle.loop;

import javafx.animation.AnimationTimer;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public abstract class GameLoopTimer extends AnimationTimer {
    private final static double GIGA_CONST = 1e9;

    private long pauseStart;
    private long animationStart;
    private final DoubleProperty animationDuration = new SimpleDoubleProperty(0L);

    private long lastFrameTimeNanos;

    private boolean isPaused;
    private boolean isActive;

    private boolean pauseScheduled;
    private boolean playScheduled;
    private boolean restartScheduled;

    public boolean isPaused() {
        return this.isPaused;
    }

    public boolean isActive() {
        return this.isActive;
    }

    public DoubleProperty animationDurationProperty() {
        return this.animationDuration;
    }

    public void pause() {
        if (!this.isPaused) {
            this.pauseScheduled = true;
        }
    }

    public void play() {
        if (this.isPaused) {
            this.playScheduled = true;
        }
    }

    @Override
    public void start() {
        super.start();
        this.isActive = true;
        this.restartScheduled = true;
    }

    @Override
    public void stop() {
        super.stop();
        this.pauseStart = 0;
        this.isPaused = false;
        this.isActive = false;
        this.pauseScheduled = false;
        this.playScheduled = false;
        this.animationDuration.set(0);
    }

    @Override
    public void handle(long now) {
        this.handlePauseSchedule(now);
        this.handlePlaySchedule(now);
        this.handleRestartSchedule(now);
        this.handlePaused(now);
    }

    private void handlePauseSchedule(long now) {
        if (this.pauseScheduled) {
            this.pauseStart = now;
            this.isPaused = true;
            this.pauseScheduled = false;
        }
    }

    private void handlePlaySchedule(long now) {
        if (this.playScheduled) {
            this.animationStart += (now - this.pauseStart);
            this.isPaused = false;
            this.playScheduled = false;
        }
    }

    private void handleRestartSchedule(long now) {
        if (restartScheduled) {
            this.isPaused = false;
            this.animationStart = now;
            this.restartScheduled = false;
        }
    }

    private void handlePaused(long now) {
        if (!this.isPaused) {
            animationDuration.set(calculateNanoSecondsAnimationDuration(now));
            tick(calculateNanoSecondsSinceLastFrame(now));
            this.lastFrameTimeNanos = now;
        }
    }

    private double calculateNanoSecondsAnimationDuration(long now) {
        return (now - this.animationStart) / GIGA_CONST;
    }

    private float calculateNanoSecondsSinceLastFrame(long now) {
        return (float) ((now - this.lastFrameTimeNanos) / GIGA_CONST);
    }

    public abstract void tick(float secondSinceLastFrame);
}
