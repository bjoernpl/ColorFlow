package com.example.android.colorflow.Levels;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.colorflow.Resources.ColorHandler;

import java.util.ArrayList;
import java.util.Observable;
import java.util.concurrent.ExecutionException;

/**
 * Created by bjoer on 6/24/2018.
 */

public class LevelHandler extends Observable{

    private static LevelHandler instance;
    private ArrayList<Level> levels;
    private int currentLevel = 1;
    private static int[] colors = null;
    static int difficulty = 0;

    private LevelHandler(){

    }

    public void setColors(Context context,int difficulty){
        this.difficulty = difficulty;
        setColors(context);
    }

    public void setColors(Context context){
        if(colors==null) {
            colors = ColorHandler.getColors(context);
            initiateLevels();
        }
    }

    public static LevelHandler getInstance(){
        if(instance==null){
            instance = new LevelHandler();
        }
        return instance;
    }

    public Level getLevel(int index){
        if(index>= levels.size()-1){
            doubleList();
        }
        return levels.get(index);
    }

    private void doubleList(){
        int a = levels.size();
        for(int i = 1; i<a;i++){
            try {
                int levelIndex = a+i;
                levels.add(new RandomLevelTask().execute(i).get());
                Log.i("levelhandler","added new level "+levelIndex);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    public void reset(){
        currentLevel = 0;
        initiateLevels();
    }

    public int getLevelsAmount(){
        return levels.size();
    }

    private void initiateLevels(){
        levels = new ArrayList<>();
        Level level1 = new Level(1,85,new int[]{Color.MAGENTA,Color.CYAN,Color.YELLOW},Color.MAGENTA,1,0,false);

        levels.add(level1);
        for(int i = 1; i<5;i++){
            try {
                levels.add(new RandomLevelTask().execute(i).get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void incrementLevel(){
        currentLevel++;
    }

    static class RandomLevelTask extends AsyncTask<Integer,Void,Level> {
        @Override
        protected Level doInBackground(Integer... integers) {
            if(difficulty!=0)return LevelRandomizer.getRandomLevel(integers[0],colors,difficulty);
            return LevelRandomizer.getRandomLevel(integers[0],colors);
        }
    }

}
