package com.example.android.colorflow.Statistics;

/**
 * Created by bjoer on 6/25/2018.
 */

public class Highscore {

    int score;
    int level;
    float averageAccuracy;

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
}
