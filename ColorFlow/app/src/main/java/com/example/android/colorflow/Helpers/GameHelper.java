package com.example.android.colorflow.Helpers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.example.android.colorflow.Activities.Ingame.GameActivity;
import com.example.android.colorflow.GameModes.Game;

public class GameHelper {


    /**
     * Method designed to simplify starting a game from anywhere
     * @param activity Activity instance needed to start Activity and override transition
     * @param game The game to be played
     */
    public static void startGame(Activity activity, Game game){
        Intent intent = new Intent(activity,GameActivity.class);
        intent.putExtra("game",game);
        activity.startActivity(intent);
        activity.overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);

    }
}
