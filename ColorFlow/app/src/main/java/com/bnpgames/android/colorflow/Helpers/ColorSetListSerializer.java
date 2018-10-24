package com.bnpgames.android.colorflow.Helpers;

import android.content.Context;

import com.bnpgames.android.colorflow.Colors.ColorHandler;

import java.util.ArrayList;
import java.util.Random;

public class ColorSetListSerializer {

    public static ArrayList<ColorHandler.ColorSet> getSelectedSets(Context context){
        ArrayList<ColorHandler.ColorSet> colorSets = new ArrayList<>();
        String listString = context.getSharedPreferences("setlist",Context.MODE_PRIVATE).getString("list",null);
        if(listString!=null){
            String[] words = listString.split(",");
            for(String word : words){
                colorSets.add(ColorHandler.ColorSet.valueOf(word));
            }
        }else {
            colorSets.add(ColorHandler.ColorSet.Default);
        }

        return colorSets;

    }

    public static ColorHandler.ColorSet getRandomColorSet(Context context){
        ArrayList<ColorHandler.ColorSet> sets = getSelectedSets(context);
        if(sets!=null)return sets.get(new Random().nextInt(sets.size()));
        else return ColorHandler.ColorSet.Default;
    }

    public static void addSelectedSet(Context context,ColorHandler.ColorSet set){
        ArrayList<ColorHandler.ColorSet> sets = getSelectedSets(context);
        if(sets==null)sets = new ArrayList<>();
        if(!sets.contains(set)){
            sets.add(0,set);
            saveList(context);
        }
    }

    public static void removeSelectedSet(Context context,ColorHandler.ColorSet set){
        ArrayList<ColorHandler.ColorSet> sets = getSelectedSets(context);
        if(sets.contains(set)&&set!= ColorHandler.ColorSet.Default){
            sets.remove(set);
            saveList(context);
        }
    }

    private static void saveList(Context context){
        ArrayList<ColorHandler.ColorSet> list = getSelectedSets(context);
        StringBuilder builder = new StringBuilder();
        for(ColorHandler.ColorSet set : list){
            builder.append(set.name());
            builder.append(",");
        }
        context.getSharedPreferences("setlist",Context.MODE_PRIVATE).edit().putString("list",builder.toString()).apply();
    }


}
