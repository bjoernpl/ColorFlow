package com.bnpgames.android.Gradients.Levels;

import com.bnpgames.android.Gradients.GameModes.Game;
import com.bnpgames.android.Gradients.Levels.Level;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by bjoer on 6/24/2018.
 */

public class LevelRandomizer {


    private static ArrayList<Integer> colorlist;
    private static Game.Difficulty difficulty = null;

    public static Level getStartLevel(int[] colors){
        colorlist = new ArrayList<>();
        for(int i : colors){
            colorlist.add(i);
        }
        int[] colorArray = getRandomColors(colors);
        return new Level(0,
                75,
                colorArray,getExpectedColor(colorArray),
                1,
                new Random().nextFloat()*2,
                new Random().nextBoolean());
    }

    public static Level getRandomLevel(int index, int[] colors, Game.Difficulty difficultysetter){
        difficulty = difficultysetter;
        return getRandomLevel(index,colors);
    }

    public static Level getRandomLevel(int index, int[] colors){
       colorlist = new ArrayList<>();
        for(int i : colors){
            colorlist.add(i);
        }
        int[] colorArray = getRandomColors(colors);
        return new Level(index,
                80,
                colorArray,getExpectedColor(colorArray),
                getRandomSpeed(index),
                new Random().nextFloat()*2,
                new Random().nextBoolean());
    }

    private static int getExpectedColor(int[] colors){
        return colors[new Random().nextInt(colors.length)];
    }

    private static int getRandomSpeed(int index){
        if(difficulty!=null){
            switch (difficulty){
                case Easy: return 2;
                case Medium: return 4;
                case Hard: return 7;
            }
        }
        Random random = new Random();
        if(index < 5){
            return random.nextInt(1)+1;
        }else if(index < 10){
            return random.nextInt(2)+2;
        }else if(index < 15){
            return random.nextInt(3)+3;
        }else{
            return random.nextInt(5)+4;
        }
    }

    private static int[] getRandomColors(int[] colors){
        int size = new Random().nextInt(colors.length-5)+3;
        int[] ints = new int[size];
        for(int i = 0; i<size;i++){
            ints[i] = getRandomColor(ints);
        }
        return ints;
    }

    private static int getRandomColor(int[] ints){
        int color = colorlist.get(new Random().nextInt(colorlist.size()));
        colorlist.remove(colorlist.indexOf(color));
        return color;
    }
}
