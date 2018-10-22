package com.bnpgames.android.colorflow.Helpers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.bnpgames.android.colorflow.Activities.Ingame.GameActivity;
import com.bnpgames.android.colorflow.GameModes.Game;

public class GameHelper {


    /**
     * Method designed to simplify starting a game from anywhere
     * @param activity Activity instance needed to start Activity and override transition
     * @param game The game to be played
     */
    public static void startGame(Activity activity, Game game){
        startGameForResult(activity,game,0);
    }

    public static void startGameForResult(Activity activity,Game game, int requestCode){
        Intent intent = new Intent(activity,GameActivity.class);
        intent.putExtra("game",game);
        if(requestCode != 0) {
            activity.startActivityForResult(intent,requestCode);
        }else{
            activity.startActivity(intent);

        }
        activity.overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }

}
