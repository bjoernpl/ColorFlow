package com.example.android.colorflow.Activities.Ingame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.android.colorflow.Activities.Ingame.GameActivity;
import com.example.android.colorflow.R;

public class SpeedModeTimeOver extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speed_mode_time_over);
        TextView totalScore = findViewById(R.id.time_up_total_score);
        totalScore.setText(String.format("Total score: %d",getIntent().getIntExtra("totalScore",0)));
        TextView highScores = findViewById(R.id.times_up_highscores);
        highScores.setText(String.format("Highscores %d seconds",getIntent().getIntExtra("timeMode",10)));
    }

    public void playAgain(View view) {
        finish();
        Intent intent = new Intent(this,GameActivity.class);
        intent.putExtra("gameMode",getIntent().getStringExtra("gameMode"));
        intent.putExtra("flowMode",getIntent().getStringExtra("flowMode"));
        intent.putExtra("timeMode",getIntent().getStringExtra("timeMode"));
        intent.putExtra("level",1);
        startActivity(intent);

    }

    public void playDifferentMode(View view) {
        finish();
    }
}
