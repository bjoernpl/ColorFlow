package com.example.android.colorflow.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android.colorflow.GameModes.ColorFlow;
import com.example.android.colorflow.Levels.LevelRandomizer;
import com.example.android.colorflow.R;
import com.example.android.colorflow.Resources.ColorHandler;
import com.example.android.colorflow.Statistics.Highscore;
import com.example.android.colorflow.Statistics.PointsHandler;

public class StartActivity extends Activity {

    TextView highscoretext;
    TextView totalScoreText;
    RelativeLayout background;
    ColorFlow colorFlow;
    View startView;
    View gameModeView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullscreen();
        setContentView(R.layout.activity_start);
        background = findViewById(R.id.start_background);
        startView = getLayoutInflater().inflate(R.layout.start_screen,null);
        gameModeView = getLayoutInflater().inflate(R.layout.activity_game_mode_select2,null);
        gameModeView.setVisibility(View.GONE);
        background.addView(startView);
        background.addView(gameModeView);
        highscoretext = findViewById(R.id.highscore_text);
        totalScoreText = findViewById(R.id.totalScoreText);
        colorFlow = findViewById(R.id.colorflow);
        colorFlow.setLevel(LevelRandomizer.getStartLevel(ColorHandler.getColors(this)));
        colorFlow.start();
        setHighscoreText(PointsHandler.getInstance().getHighscore(this));
        PointsHandler.getInstance().addObserver((observable, o) -> {
            if (o instanceof Highscore) setHighscoreText((Highscore) o);
        });
        PointsHandler.getInstance().addObserver((observable, o) -> {
            if(!(o instanceof Highscore)){
                totalScoreText.setText(o+"");
            }
        });
        PointsHandler.getInstance().loadPoints(this);

        findViewById(R.id.startplaying_text).setOnClickListener(view -> {
            /*
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(StartActivity.this, GameModeSelectActivity.class);
               startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            },400);
            colorFlow.setFadeAway(1);*/
            toggleMode(true);
        });
    }

    private void toggleMode(boolean showMode) {
        gameModeView.setVisibility(showMode? View.VISIBLE : View.GONE);
        startView.animate().alpha(showMode? 0.0f : 1).setDuration(600).start();
        gameModeView.animate().alpha(showMode ? 1 : 0.0f).setDuration(600).start();
    }

    public void modeClicked(View view){
        Intent intent = new Intent(StartActivity.this, FlowModeSelectActivity.class);


        switch (view.getId()){
            case R.id.rl_casual_mode:
                intent.putExtra("gameMode","casual");
                break;
            case R.id.rl_speed_mode:
                intent.putExtra("gameMode","speed");
                break;
            case R.id.rl_continuous_mode:
                intent.putExtra("gameMode","continuous");
                break;
            case R.id.rl_endurance_mode:
                intent.putExtra("gameMode","endurance");
                break;
            default:

                break;
        }
        new Handler().postDelayed(() -> {
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        },400);
    }

    private void setHighscoreText(Highscore score){
        highscoretext.setText(String.format(getString(R.string.highscore_text),score.getLevel(),score.getScore()));
    }

    @Override
    protected void onResume() {
        colorFlow.setLevel(LevelRandomizer.getStartLevel(ColorHandler.getColors(this)));
        super.onResume();
    }

    public void backClicked(View view) {
        toggleMode(false);
    }

    private void setFullscreen() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(uiOptions);
    }

}
