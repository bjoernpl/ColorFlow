package com.example.android.colorflow;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

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
