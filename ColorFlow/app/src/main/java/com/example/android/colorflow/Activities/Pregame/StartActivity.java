package com.example.android.colorflow.Activities.Pregame;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.colorflow.Activities.Ingame.GameActivity;
import com.example.android.colorflow.Fragments.ChooseGameModeFragment;
import com.example.android.colorflow.Fragments.SpeedModeFragment;
import com.example.android.colorflow.Fragments.StartFragment;
import com.example.android.colorflow.GameModes.ColorFlow;
import com.example.android.colorflow.GameModes.Game;
import com.example.android.colorflow.Levels.LevelRandomizer;
import com.example.android.colorflow.R;
import com.example.android.colorflow.Resources.ColorHandler;
import com.example.android.colorflow.Statistics.Highscore;
import com.example.android.colorflow.Statistics.PointsHandler;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StartActivity extends FragmentActivity implements ChooseGameModeFragment.OnGameModeChosenListener, StartFragment.OnButtonPressedListener, SpeedModeFragment.OnSpeedModeSelectedListener {

    @Nullable @BindView(R.id.highscore_text) TextView highscoretext;
    @Nullable @BindView(R.id.totalScoreText) TextView totalScoreText;
    @BindView(R.id.start_background) RelativeLayout background;
    @BindView(R.id.colorflow) ColorFlow colorFlow;
    View startView;
    View gameModeView;
    RadioGroup difficultyChoice;
    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullscreen();
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);
        getSupportFragmentManager().beginTransaction().add(R.id.start_frame,new StartFragment()).addToBackStack(null).commit();

        colorFlow.setLevel(LevelRandomizer.getStartLevel(ColorHandler.getColors(this)));
        colorFlow.start();
    }

    @Override
    public void onModeChosen(Game.GameMode mode) {
        game = new Game(mode);
        if(mode.equals(Game.GameMode.Speed)){
            getSupportFragmentManager().beginTransaction().replace(R.id.start_frame,new SpeedModeFragment()).addToBackStack(null).commit();
        }else {
            Intent intent = new Intent(this, FlowModeSelectActivity.class);
            intent.putExtra("game", game);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        }
    }

    @Override
    public void onButtonPressed(StartFragment.StartButton button) {
        switch(button){
            case Start:
                getSupportFragmentManager().beginTransaction().replace(R.id.start_frame,new ChooseGameModeFragment()).addToBackStack(null).commit();
                break;
        }
    }

    @Override
    public void onSpeedModeSelected(Game.Difficulty difficulty, int time) {
        game.setDifficulty(difficulty);
        game.setTime(time);
        Intent intent = new Intent(this,FlowModeSelectActivity.class);
        intent.putExtra("game",game);
        startActivity(intent);
    }

    private void toggleMode(boolean showMode) {
        gameModeView.setVisibility(showMode? View.VISIBLE : View.GONE);
        startView.animate().alpha(showMode? 0.0f : 1).setDuration(600).start();
        gameModeView.animate().alpha(showMode ? 1 : 0.0f).setDuration(600).start();
    }

    @Override
    protected void onResume() {
        colorFlow.setLevel(LevelRandomizer.getStartLevel(ColorHandler.getColors(this)));
        setFullscreen();
        super.onResume();
    }

    public void backClicked(View view) {
        getSupportFragmentManager().popBackStack();
    }

    private void setFullscreen() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(uiOptions);
    }


}
