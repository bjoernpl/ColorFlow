package com.example.android.colorflow.Levels;

/**
 * Created by bjoer on 6/24/2018.
 */

public class Level {
    private int levelIndex;
    private int requiredAccuracy;
    private int[] colors;
    private int expectedColor;
    private int speed;
    private float angle;
    private boolean leftToRight;

    Level(int levelIndex, int requiredAccuracy, int[] colors,int expectedColor ,int speed, float angle,boolean leftToRight) {
        this.levelIndex = levelIndex;
        this.requiredAccuracy = requiredAccuracy;
        this.colors = colors;
        this.expectedColor = expectedColor;
        this.speed = speed;
        this.angle = angle;
        this.leftToRight = leftToRight;
    }

    public boolean isLeftToRight() {
        return leftToRight;
    }

    public void setLeftToRight(boolean leftToRight) {
        this.leftToRight = leftToRight;
    }

    public int getExpectedColor() {
        return expectedColor;
    }

    public void setExpectedColor(int expectedColor) {
        this.expectedColor = expectedColor;
    }

    public int getLevelIndex() {
        return levelIndex;
    }

    public void setLevelIndex(int levelIndex) {
        this.levelIndex = levelIndex;
    }

    public int getRequiredAccuracy() {
        return requiredAccuracy;
    }

    public void setRequiredAccuracy(int requiredAccuracy) {
        this.requiredAccuracy = requiredAccuracy;
    }

    public int[] getColors() {
        return colors;
    }

    public void setColors(int[] colors) {
        this.colors = colors;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }
}
