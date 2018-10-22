package com.bnpgames.android.colorflow.Statistics;

import android.os.Parcel;
import android.os.Parcelable;

import com.bnpgames.android.colorflow.Levels.Level;

/**
 * Created by bjoer on 6/25/2018.
 */

public class Highscore implements Parcelable{

    private int score;
    private int level;
    private float averageAccuracy;

    public Highscore(int score, int level, float averageAccuracy) {
        this.score = score;
        this.level = level;
        this.averageAccuracy = averageAccuracy;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public float getAverageAccuracy() {
        return averageAccuracy;
    }

    public void setAverageAccuracy(float averageAccuracy) {
        this.averageAccuracy = averageAccuracy;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(score);
        dest.writeInt(level);
        dest.writeFloat(averageAccuracy);
    }

    private void readFromParcel(Parcel in) {
        score = in.readInt();
        level = in.readInt();
        averageAccuracy = in.readFloat();
    }

    public Highscore(Parcel in) {
        readFromParcel(in);
    }

    public static final Parcelable.Creator<Highscore> CREATOR = new Parcelable.Creator<Highscore>() {
        @Override
        public Highscore createFromParcel(Parcel source) {
            return new Highscore(source);
        }

        @Override
        public Highscore[] newArray(int size) {
            return new Highscore[size];
        }
    };
}
