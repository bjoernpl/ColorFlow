package com.example.android.colorflow;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import java.util.Observable;

public class PointsHandler extends Observable{


    private static PointsHandler instance;
    private int totalScore = 0;

    private int score = 0;
    private int attempts = 1;

    public static PointsHandler getInstance(){
        if(instance==null){
            instance = new PointsHandler();
        }
        return instance;
    }

    private PointsHandler(){

    }

    public void loadPoints(Context context){
        SharedPreferences preferences = context.getSharedPreferences("score",0);
        totalScore = preferences.getInt("totalscore",0);
        setChanged();
        notifyObservers(totalScore);
    }

    public void addScore(int score,Context context){
        this.score += score;
        this.totalScore += score;
        saveTotalScore(context);
        setChanged();
        notifyObservers(totalScore);
    }

    private void saveTotalScore(Context context){
        context.getSharedPreferences("score",0).edit().putInt("totalscore",totalScore).apply();
    }

    public boolean retry(Context context){
        int cost = 400*attempts++;
        if(totalScore>=cost){
            totalScore -= cost;
            saveTotalScore(context);
            return true;
        }else{
            return false;
        }
    }

    public int getRetryCost(){
        return  400*attempts;
    }

    public int getScore(){
        return this.score;
    }


    public void reset(){
        score = 0;
        attempts = 1;
    }




    public void saveHighscore(Context context){
        SharedPreferences preferences = context.getSharedPreferences("highscore", 0);
        if (preferences.getInt("level", 0) < LevelHandler.getInstance().getCurrentLevel() || preferences.getInt("score", 0) < getScore()) {
            preferences.edit().putInt("level",LevelHandler.getInstance(). getCurrentLevel()).putInt("score", getScore()).apply();
        }
        setChanged();
        notifyObservers(new Highscore(getScore(),LevelHandler.getInstance().getCurrentLevel(),0));
    }

    public Highscore getHighscore(Context context){
        SharedPreferences preferences = context.getSharedPreferences("highscore",0);
        return new Highscore(preferences.getInt("score",0),preferences.getInt("level",0),preferences.getFloat("accuracy",0));
    }
}
