package com.example.android.colorflow.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.widget.TextView;

import com.example.android.colorflow.GameModes.ColorFlow;
import com.example.android.colorflow.Levels.LevelRandomizer;
import com.example.android.colorflow.R;
import com.example.android.colorflow.Resources.ColorHandler;
import com.example.android.colorflow.Statistics.Highscore;
import com.example.android.colorflow.Statistics.PointsHandler;
import com.example.android.colorflow.Statistics.Statistics;

public class StartActivity extends Activity {

    TextView highscoretext;
    TextView totalScoreText;
    ColorFlow colorFlow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
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
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(StartActivity.this, GameModeSelectActivity.class);
               startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            },400);
            colorFlow.setFadeAway(7);
        });
    }

    private void setHighscoreText(Highscore score){
        highscoretext.setText(String.format(getString(R.string.highscore_text),score.getLevel(),score.getScore()));
    }

    @Override
    protected void onResume() {
        colorFlow.setLevel(LevelRandomizer.getStartLevel(ColorHandler.getColors(this)));
        super.onResume();
    }
}
