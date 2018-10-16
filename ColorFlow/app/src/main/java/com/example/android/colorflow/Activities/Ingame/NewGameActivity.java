package com.example.android.colorflow.Activities.Ingame;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.android.colorflow.GameModes.Flow;
import com.example.android.colorflow.GameModes.Game;
import com.example.android.colorflow.Helpers.FullscreenHelper;
import com.example.android.colorflow.Helpers.GameHelper;
import com.example.android.colorflow.Levels.Level;
import com.example.android.colorflow.Levels.LevelHandler;
import com.example.android.colorflow.R;
import com.example.android.colorflow.Statistics.Highscore;
import com.example.android.colorflow.Statistics.PointsHandler;

public abstract class NewGameActivity extends AppCompatActivity {

    /* The game which will be played */
    Game game;
    /* The level to be played*/
    Level level;
    /* The FLow object which will be shown*/
    Flow flow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FullscreenHelper.setFullscreen(this);

        game = getIntent().getParcelableExtra("game");

        // Initialise color array
        LevelHandler levelHandler = LevelHandler.getInstance();
        levelHandler.setColors(this);
        level = levelHandler.getLevel(game.getIndex());

        setObservers();
        setFlow();
        flow.setLevel(level);
    }

    protected abstract void setFlow();

    private void setObservers() {
        PointsHandler.getInstance().addObserver((observable, o) -> {
            if(!(o instanceof Highscore)){

            }
        });

    }

    private void retry(){
        if(PointsHandler.getInstance().retry(this)) {
            finish();
            GameHelper.startGame(this, game);
        }else{
            Toast.makeText(this, R.string.not_enough_points_to_restart,Toast.LENGTH_LONG).show();
        }
    }


}
