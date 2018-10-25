package com.bnpgames.android.colorflow.Colors;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.provider.CalendarContract;
import android.support.annotation.ColorInt;

import com.bnpgames.android.colorflow.Helpers.ColorSetListSerializer;
import com.bnpgames.android.colorflow.R;

import java.util.ArrayList;

import static com.bnpgames.android.colorflow.Colors.ColorHandler.ColorSet.Autumn;
import static com.bnpgames.android.colorflow.Colors.ColorHandler.ColorSet.Circus;
import static com.bnpgames.android.colorflow.Colors.ColorHandler.ColorSet.Default;
import static com.bnpgames.android.colorflow.Colors.ColorHandler.ColorSet.France;
import static com.bnpgames.android.colorflow.Colors.ColorHandler.ColorSet.Spring;
import static com.bnpgames.android.colorflow.Colors.ColorHandler.ColorSet.Summer;
import static com.bnpgames.android.colorflow.Colors.ColorHandler.ColorSet.Winter;

public class ColorHandler {

    public enum ColorSet{
        Default,
        Winter,
        Spring,
        Summer,
        Autumn,
        Circus,
        France
    }

    private static final int LOW_COST = 10000;
    private static final int MEDIUM_COST = 25000;
    private static final int HIGH_COST = 100000;

    public static ArrayList<ColorSetData> getColorList(Context context){
        ArrayList<ColorSetData> list = new ArrayList<>();
        list.add(new ColorSetData(getColors(context,Default),Default,0,true));
        list.add(new ColorSetData(getColors(context,Winter),Winter,LOW_COST,true));
        list.add(new ColorSetData(getColors(context,Spring),Spring,LOW_COST,true));
        list.add(new ColorSetData(getColors(context,Summer),Summer,MEDIUM_COST,true));
        list.add(new ColorSetData(getColors(context,Autumn),Autumn,MEDIUM_COST,true));
        list.add(new ColorSetData(getColors(context,France),France,HIGH_COST,true));
        list.add(new ColorSetData(getColors(context,Circus),Circus,MEDIUM_COST,true));
        return list;
    }

    public static ArrayList<ColorSetData> getColorListForLevels(Context context){
        ArrayList<ColorSetData> list = new ArrayList<>();
        ArrayList<ColorSet> colorSets = ColorSetListSerializer.getSelectedSets(context);
        for(ColorSet set: colorSets){
            list.add(new ColorSetData(getColors(context,set),set,0,true));
        }
        return list;
    }

    public static int[] getColors(Context context){
        return getColors(context,getSelectedColorSet(context));
    }

    private static int[] getColors(Context context,ColorSet set){
        switch (set){
            case Default: return new int[]{Color.BLUE,
                    Color.RED,
                    Color.GREEN,
                    Color.MAGENTA,
                    Color.CYAN,
                    context.getResources().getColor(R.color.purple),
                    context.getResources().getColor(R.color.turquoise),
                    context.getResources().getColor(R.color.dark_green),
                    context.getResources().getColor(R.color.burgunder),
                    context.getResources().getColor(R.color.yellow)};
            case Winter:
                return new int[]{
                        context.getResources().getColor(R.color.winter1),
                        context.getResources().getColor(R.color.winter2),
                        context.getResources().getColor(R.color.winter3),
                        context.getResources().getColor(R.color.winter4),
                        context.getResources().getColor(R.color.winter5),
                        context.getResources().getColor(R.color.winter6)};
            case Spring:
                return new int[]{
                        context.getResources().getColor(R.color.spring1),
                        context.getResources().getColor(R.color.spring2),
                        context.getResources().getColor(R.color.spring3),
                        context.getResources().getColor(R.color.spring4),
                        context.getResources().getColor(R.color.spring5),
                        context.getResources().getColor(R.color.spring6)};
            case Summer:
                return new int[]{
                        context.getResources().getColor(R.color.summer1),
                        context.getResources().getColor(R.color.summer2),
                        context.getResources().getColor(R.color.summer3),
                        context.getResources().getColor(R.color.summer4),
                        context.getResources().getColor(R.color.summer5),
                        context.getResources().getColor(R.color.summer6)};
            case Autumn:
                return new int[]{
                        context.getResources().getColor(R.color.autumn1),
                        context.getResources().getColor(R.color.autumn2),
                        context.getResources().getColor(R.color.autumn3),
                        context.getResources().getColor(R.color.autumn4),
                        context.getResources().getColor(R.color.autumn5),
                        context.getResources().getColor(R.color.autumn6)};
            case Circus:
                return new int[]{
                        context.getResources().getColor(R.color.circus1),
                        context.getResources().getColor(R.color.circus4),
                        context.getResources().getColor(R.color.spring2),
                        context.getResources().getColor(R.color.circus3),
                        context.getResources().getColor(R.color.circus2),
                        context.getResources().getColor(R.color.circus5)};
            case France:
                return new int[]{
                        context.getResources().getColor(R.color.france1),
                        context.getResources().getColor(R.color.france2),
                        context.getResources().getColor(R.color.france3),
                        context.getResources().getColor(R.color.france4),
                        context.getResources().getColor(R.color.france5)};
        }
        return getColors(context);
    }

    public static void setSelectedColorSet(Context context,ColorSet set){
        context.getSharedPreferences("colors",Context.MODE_PRIVATE).edit().putString("colorset",set.name()).apply();
    }

    public static ColorSet getSelectedColorSet(Context context){
        //return ColorSet.valueOf(context.getSharedPreferences("colors",Context.MODE_PRIVATE).getString("colorset",Default.name()));
        return ColorSetListSerializer.getRandomColorSet(context);
    }


}
