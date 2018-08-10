package com.example.android.colorflow.Resources;

public class AdjectiveGiver {

    public static String getAdjective(int score){
        if(score < 80){
            return "Good!";
        }else if(score < 85){
            return "nice!";
        }else if(score < 90){
            return "great!";
        }else if(score < 95){
            return "awesome!";
        }else if(score < 100){
            return "excellent!";
        }else if(score == 100){
            return "perfect!";
        }else return "You failed!";
    }
}
