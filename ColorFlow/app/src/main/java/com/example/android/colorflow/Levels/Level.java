package com.example.android.colorflow.Levels;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.android.colorflow.GameModes.Game;

/**
 * Created by bjoer on 6/24/2018.
 */

public class Level implements Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(levelIndex);
        dest.writeInt(requiredAccuracy);
        dest.writeIntArray(colors);
        dest.writeInt(expectedColor);
        dest.writeInt(speed);
        dest.writeFloat(angle);
        dest.writeByte((byte) (leftToRight?1:0));

    }

    private void readFromParcel(Parcel in) {
        levelIndex = in.readInt();
        requiredAccuracy = in.readInt();
        colors = in.createIntArray();
        expectedColor = in.readInt();
        speed = in.readInt();
        angle = in.readFloat();
        leftToRight = in.readByte()==1;
    }

    public Level(Parcel in) {
        readFromParcel(in);
    }

    public static final Parcelable.Creator<Level> CREATOR = new Parcelable.Creator<Level>() {
        @Override
        public Level createFromParcel(Parcel source) {
            return new Level(source);
        }

        @Override
        public Level[] newArray(int size) {
            return new Level[size];
        }
    };
}
