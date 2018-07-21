package com.example.android.colorflow;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;

public class ColorHandler {

    public static int[] getColors(Context context){
        return new int[]{Color.BLUE,
                Color.RED,
                Color.GREEN,
                Color.MAGENTA,
                Color.CYAN,
                context.getResources().getColor(R.color.purple),
                context.getResources().getColor(R.color.turquoise),
                context.getResources().getColor(R.color.dark_green),
                context.getResources().getColor(R.color.burgunder),
                context.getResources().getColor(R.color.yellow)};
    }

}
