package com.example.android.colorflow.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.android.colorflow.Statistics.Highscore;
import com.example.android.colorflow.Statistics.PointsHandler;
import com.example.android.colorflow.R;

public class GameModeSelectActivity extends Activity {

    TextView totalScoreText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_mode_select);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(uiOptions);
        findViewById(R.id.radioButton2).setOnClickListener(view -> {
            Intent intent = new Intent(GameModeSelectActivity.this, GameActivity.class);
            intent.putExtra("level",1);
            intent.putExtra("gameMode","linear");
            startActivity(intent);
        });
        findViewById(R.id.radioButton4).setOnClickListener(view -> {
            Intent intent = new Intent(GameModeSelectActivity.this, GameActivity.class);
            intent.putExtra("level",1);
            intent.putExtra("gameMode","radial");
            startActivity(intent);
        });
        totalScoreText = findViewById(R.id.totalScoreText);
        PointsHandler.getInstance().addObserver((observable, o) -> {
            if(!(o instanceof Highscore)){
                totalScoreText.setText(o+"");
            }
        });
        PointsHandler.getInstance().loadPoints(this);
        findViewById(R.id.backTextView).setOnClickListener(view -> finish());
    }
}
