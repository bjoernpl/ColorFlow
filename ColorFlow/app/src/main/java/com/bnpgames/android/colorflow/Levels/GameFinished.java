package com.bnpgames.android.colorflow.Levels;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorInt;

import com.bnpgames.android.colorflow.GameModes.Game;
import com.bnpgames.android.colorflow.Statistics.Highscore;

public class GameFinished implements Parcelable {
    @ColorInt private int expectedColor;
    @ColorInt private int actualColor;
    private Highscore score;
    private boolean isHighScore;
    private Game.FlowMode flowMode;


    public GameFinished(int expectedColor, int actualColor, Highscore score, boolean isHighScore, Game.FlowMode flowMode) {
        this.expectedColor = expectedColor;
        this.actualColor = actualColor;
        this.score = score;
        this.isHighScore = isHighScore;
        this.flowMode = flowMode;
    }

    public GameFinished(){

    }

    public Game.FlowMode getFlowMode() {
        return flowMode;
    }

    public void setFlowMode(Game.FlowMode flowMode) {
        this.flowMode = flowMode;
    }

    public int getExpectedColor() {
        return expectedColor;
    }

    public void setExpectedColor(int expectedColor) {
        this.expectedColor = expectedColor;
    }

    public int getActualColor() {
        return actualColor;
    }

    public void setActualColor(int actualColor) {
        this.actualColor = actualColor;
    }

    public Highscore getScore() {
        return score;
    }

    public void setScore(Highscore score) {
        this.score = score;
    }

    public boolean isHighScore() {
        return isHighScore;
    }

    public void setHighScore(boolean highScore) {
        isHighScore = highScore;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(expectedColor);
        dest.writeInt(actualColor);
        dest.writeParcelable(score,0);
        dest.writeByte(isHighScore ? (byte) 1 : 0);
        dest.writeString(flowMode.name());
    }

    private void readFromParcel(Parcel in) {
        expectedColor = in.readInt();
        actualColor = in.readInt();
        score = in.readParcelable(Highscore.class.getClassLoader());
        isHighScore = in.readByte() == 1;
        flowMode = Game.FlowMode.valueOf(in.readString());
    }

    public GameFinished(Parcel in) {
        readFromParcel(in);
    }

    public static final Parcelable.Creator<GameFinished> CREATOR = new Parcelable.Creator<GameFinished>() {
        @Override
        public GameFinished createFromParcel(Parcel source) {
            return new GameFinished(source);
        }

        @Override
        public GameFinished[] newArray(int size) {
            return new GameFinished[size];
        }
    };

}
