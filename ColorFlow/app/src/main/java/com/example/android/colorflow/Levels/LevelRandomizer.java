package com.example.android.colorflow.Levels;

import com.example.android.colorflow.Levels.Level;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by bjoer on 6/24/2018.
 */

public class LevelRandomizer {


    private static ArrayList<Integer> colorlist;

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
        Random random = new Random();
        if(index < 5){
            return random.nextInt(1)+1;
        }else if(index < 10){
            return random.nextInt(2)+1;
        }else{
            return random.nextInt(3)+2;
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
