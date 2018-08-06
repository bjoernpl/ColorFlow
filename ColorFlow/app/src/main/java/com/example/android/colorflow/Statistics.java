package com.example.android.colorflow;

import android.support.annotation.ColorInt;

public class Statistics {
    private static Statistics instance;

    private int totalAttempts;
    private int correctClicks;
    private int wrongClicks;
    private float percentCorrectClicks;
    private float averageAccuracy;


    public static Statistics getInstance(){
        if(instance==null)instance = new Statistics();
        return instance;
    }

    private Statistics(){

    }

    public void colorClicked(boolean correct, @ColorInt int expectedColor, @ColorInt int clickedColor, int accuracy){
        if(correct){ clickedCorrectColor(expectedColor,clickedColor,accuracy);} else {clickedWrongColor(expectedColor,clickedColor,accuracy);}


    }

    public void clickedCorrectColor(@ColorInt int expectedColor, @ColorInt int clickedColor, int accuracy){

    }

    public void clickedWrongColor(@ColorInt int expectedColor, @ColorInt int clickedColor, int accuracy){

    }
}
