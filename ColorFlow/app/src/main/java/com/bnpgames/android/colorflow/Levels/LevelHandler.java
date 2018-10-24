package com.bnpgames.android.colorflow.Levels;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.bnpgames.android.colorflow.Colors.ColorSetData;
import com.bnpgames.android.colorflow.GameModes.Game;
import com.bnpgames.android.colorflow.Colors.ColorHandler;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;
import java.util.concurrent.ExecutionException;

/**
 * Created by bjoer on 6/24/2018.
 */

public class LevelHandler extends Observable{

    private static LevelHandler instance;
    private ArrayList<Level> levels;
    private int currentLevel = 1;
    private static int[] colors = null;
    public static ArrayList<ColorSetData> colorSets;
    static Game.Difficulty difficulty = null;

    private LevelHandler(){

    }

    public void setColors(Context context, Game.Difficulty difficultysetter){
        difficulty = difficultysetter;
        setColors(context);
    }

    public void setColors(Context context){
        if(colors==null) {
            //colors = ColorHandler.getColors(context);
            colorSets = ColorHandler.getColorList(context);
            colors = getRandomColors();
            initiateLevels();
        }else{
            colorSets = ColorHandler.getColorList(context);
            colors = ColorHandler.getColors(context);
        }
    }

    private int[] getRandomColors(){
        return colorSets.get(new Random().nextInt(colorSets.size())).getColors();
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
                colors = getRandomColors();
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
        //Level level1 = new Level(1,85,new int[]{Color.MAGENTA,Color.CYAN,Color.YELLOW},Color.MAGENTA,1,0,false);

        //levels.add(level1);
        for(int i = 0; i<5;i++){
            try {
                levels.add(new RandomLevelTask().execute(i).get());
                colors = getRandomColors();
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
            if(difficulty!=null)return LevelRandomizer.getRandomLevel(integers[0],colors,difficulty);
            return LevelRandomizer.getRandomLevel(integers[0],colors);
        }
    }

}
