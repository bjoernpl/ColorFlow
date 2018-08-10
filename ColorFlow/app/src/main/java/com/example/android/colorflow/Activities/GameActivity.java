package com.example.android.colorflow.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.colorflow.GameModes.Flow;
import com.example.android.colorflow.Levels.Level;
import com.example.android.colorflow.Levels.LevelHandler;
import com.example.android.colorflow.Statistics.PointsHandler;
import com.example.android.colorflow.R;
import com.example.android.colorflow.Resources.AdjectiveGiver;

public class GameActivity extends Activity {

    private Level level;
    private Flow colorFlow;
    private TextView colorTitle;
    private TextView retryButton;
    private TextView levelTitle;
    private ViewGroup successGroup;
    private TextView scoreView;
    private TextView adjectiveView;
    private TextView totalScoreView;
    private boolean pressedRestart = false;
    private int index;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getIntent().getStringExtra("gameMode").equals("radial")){
            setContentView(R.layout.activity_game_radial);
        }else{
            setContentView(R.layout.activity_game);
        }
        setFullscreen();
        initialiseViews();
        setListeners();

        LevelHandler.getInstance().setColors(this);
        index = getIntent().getIntExtra("level",0);
        level = LevelHandler.getInstance().getLevel(index);
        levelTitle.setText(String.format("Level %d",index));
        colorFlow.setLevel(level);

        showExpectedColor();
    }

    @SuppressLint("DefaultLocale")
    private void failure(int accuracy){
        colorFlow.wrongColorClicked();
        showFailBorder();
        successGroup.setVisibility(View.VISIBLE);
        scoreView.setText(String.format("%d",accuracy));
        totalScoreView.setText(String.format("Total score: %d", PointsHandler.getInstance().getScore()));
        colorTitle.setVisibility(View.GONE);
        adjectiveView.setText("You failed!");
        retryButton.setVisibility(View.VISIBLE);
        retryButton.setText(String.format("Retry for %d",PointsHandler.getInstance().getRetryCost()));
        new Handler().postDelayed(this::startOver,4600);
    }


    @SuppressLint("DefaultLocale")
    private void success(int accuracy){
        colorFlow.correctColorClicked();
        successGroup.setVisibility(View.VISIBLE);
        scoreView.setText(String.format("%d",accuracy));
        totalScoreView.setText(String.format("Total score: %d",PointsHandler.getInstance().getScore()));
        colorTitle.setVisibility(View.GONE);
        adjectiveView.setText(AdjectiveGiver.getAdjective(accuracy));
        new Handler().postDelayed(this::startNextLevel, 2200);
    }

    private void startNextLevel(){
        finish();
        PointsHandler.getInstance().saveHighscore(GameActivity.this);
        LevelHandler.getInstance().incrementLevel();
        Intent intent = new Intent(GameActivity.this, GameActivity.class);
        intent.putExtra("level", index + 1);
        intent.putExtra("gameMode",getIntent().getStringExtra("gameMode"));
        startActivity(intent);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setListeners() {
        retryButton.setOnClickListener(view -> {
            if(PointsHandler.getInstance().retry(this)) {
                pressedRestart = true;
                finish();
                Intent intent = new Intent(GameActivity.this, GameActivity.class);
                intent.putExtra("level", index);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }else{
                Toast.makeText(this,"Not enough points!",Toast.LENGTH_LONG).show();
            }
        });

        colorFlow.setOnTouchListener((view, motionEvent) -> {
            if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                //colorFlow.performClick();
                if(!colorFlow.isPaused) {
                    int accuracy = colorFlow.getAccuracy((int) motionEvent.getX(), (int) motionEvent.getY());
                    PointsHandler.getInstance().addScore(accuracy,GameActivity.this);
                    if (accuracy >= level.getRequiredAccuracy()) {
                        success(accuracy);
                    } else {
                        failure(accuracy);
                    }
                    //colorTitle.setVisibility(View.VISIBLE);
                    colorFlow.setPaused(true);
                }
                return true;
            }
            return false;
        });
    }

    private void showFailBorder(){
        View view = findViewById(R.id.fail_border);
        view.animate().alpha(1.0f).setDuration(200).start();
        new Handler().postDelayed(() -> view.animate().alpha(0f).setDuration(400).start(),400);
    }

    private void initialiseViews() {
        levelTitle      = findViewById(R.id.levelTitle);
        colorTitle      = findViewById(R.id.expectedColorTitle);
        colorFlow       = findViewById(R.id.colorflow);
        //colorFlow       = getIntent().getStringExtra("gameMode").equals("radial")? new ColorFlowRadial(this,colorFlow.getAttrs()): new ColorFlow(this,colorFlow.getAttrs());
        retryButton     = findViewById(R.id.retryButton);
        successGroup    = findViewById(R.id.successGroup);
        scoreView       = findViewById(R.id.scoreTextView);
        totalScoreView  = findViewById(R.id.totalScoreTextView);
        adjectiveView   = findViewById(R.id.adjectiveTextView);
    }

    private void setFullscreen() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(uiOptions);
    }


    private void startOver(){
        if(!pressedRestart) {
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            LevelHandler.getInstance().reset();
            PointsHandler.getInstance().reset();
        }
    }

    private void showExpectedColor(){
        View background = findViewById(R.id.gameBackground);
        background.setBackgroundColor(level.getExpectedColor());
        Runnable run  = () -> {
            background.setVisibility(View.GONE);
            colorTitle.setVisibility(View.GONE);
            colorFlow.start();
        };
        background.setOnClickListener(view -> {
              run.run();
        });
        new Handler().postDelayed(run,5000);
    }
}
