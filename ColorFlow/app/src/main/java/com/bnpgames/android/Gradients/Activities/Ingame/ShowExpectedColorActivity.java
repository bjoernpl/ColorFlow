package com.bnpgames.android.Gradients.Activities.Ingame;

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

import com.bnpgames.android.Gradients.GameModes.Game;
import com.bnpgames.android.Gradients.R;
import com.bnpgames.android.Gradients.Resources.ClickListener;
import com.bnpgames.android.Gradients.TimeHandling.Timer;

import org.w3c.dom.Text;

import java.util.Observable;
import java.util.Observer;

public class ShowExpectedColorActivity extends Activity {

    TextView colorTitle;
    TextView gameModeTitle;
    View background;
    ProgressBar pbar;
    TextView remainingTime;

    int time;
    private Observer observer = new Observer() {
        @Override
        public void update(Observable observable, Object o) {
            remainingTime.setText(String.format("Remaining time: %d seconds", o));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullscreen();
        setContentView(R.layout.activity_show_expected_color);
        colorTitle      = findViewById(R.id.expectedColorTitle);
        gameModeTitle = findViewById(R.id.title_game_mode);
        pbar = findViewById(R.id.progressbar);
        Game game = getIntent().getParcelableExtra("game");
        if(game.getGameMode().equals(Game.GameMode.Speed)){
            time = game.getTime();
            TextView tv = findViewById(R.id.requiredAccuracy);
            tv.setText("Score as many points as possible in 10 seconds!\n\nTap anywhere to start timer!");
            remainingTime = findViewById(R.id.remaining_time);
            remainingTime.setVisibility(View.VISIBLE);
            Timer timer = Timer.getInstance();
            if(!timer.isRunning()) {
                remainingTime.setText(String.format("Remaining time: %s seconds", time));
            }else{
                timer.addObserver(observer);
            }
        }
        gameModeTitle.setText(String.format("%s\nMode",game.getGameMode().name()));
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
        Timer timer = Timer.getInstance();
        if(!timer.isRunning()){
            timer.setTime(time);
            timer.startTimer();
            timer.deleteObserver(observer);
        }
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
