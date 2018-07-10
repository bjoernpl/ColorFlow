package com.example.android.colorflow;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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
