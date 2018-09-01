package com.example.android.colorflow.Activities;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.colorflow.R;
import com.example.android.colorflow.Resources.ClickListener;

public class ShowExpectedColorActivity extends Activity {

    TextView colorTitle;
    TextView gameModeTitle;
    View background;
    ProgressBar pbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullscreen();
        setContentView(R.layout.activity_show_expected_color);
        colorTitle      = findViewById(R.id.expectedColorTitle);
        gameModeTitle = findViewById(R.id.title_game_mode);
        pbar = findViewById(R.id.progressbar);
        String gameMode = getIntent().getStringExtra("gameMode");
        gameMode = gameMode.substring(0,1).toUpperCase() + gameMode.substring(1).toLowerCase();
        gameModeTitle.setText(String.format("%s\nMode",gameMode));
        background = findViewById(R.id.expectedColorBackground);
        background.setBackgroundColor(getIntent().getIntExtra("expectedColor",0));
        Runnable run = () -> {
            if(!isDestroyed()){
                startLevel();
            }
        };
        background.setOnClickListener(view -> startLevel());
        new Handler().postDelayed(run,5000);
        ObjectAnimator animation = ObjectAnimator.ofInt(pbar, "progress", pbar.getProgress(), 500);
        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(10000);
        animation.start();
    }


    private void startLevel() {
        setResult(RESULT_OK);
        finish();
    }

    private void setFullscreen() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(uiOptions);
    }

}
