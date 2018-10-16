package com.example.android.colorflow.Activities.Ingame;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.colorflow.GameModes.Flow;
import com.example.android.colorflow.GameModes.Game;
import com.example.android.colorflow.Helpers.FullscreenHelper;
import com.example.android.colorflow.Levels.Level;
import com.example.android.colorflow.Levels.LevelHandler;
import com.example.android.colorflow.Statistics.Highscore;
import com.example.android.colorflow.Statistics.PointsHandler;
import com.example.android.colorflow.R;
import com.example.android.colorflow.Resources.AdjectiveGiver;
import com.example.android.colorflow.TimeHandling.Timer;

import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GameActivity extends Activity {

    private Level level;
    @BindView(R.id.colorflow) Flow colorFlow;
    @BindView(R.id.retryButton) TextView retryButton;
    @BindView(R.id.levelTitle) TextView levelTitle;
    @BindView(R.id.successGroup) ViewGroup successGroup;
    @BindView(R.id.scoreTextView) TextView scoreView;
    @BindView(R.id.adjectiveTextView) TextView adjectiveView;
    @BindView(R.id.totalScoreTextView) TextView sessionScoreView;
    @BindView(R.id.totalScoreText) TextView totalScoreView;
    private Game.FlowMode flowMode;
    private Game.GameMode gameMode;
    private boolean pressedRestart = false;
    private int index;
    private boolean finished = false;
    private boolean succeeded = false;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Game game = getIntent().getParcelableExtra("game");
        initialiseGame(game);

        if(flowMode.equals(Game.FlowMode.Radial)){
            setContentView(R.layout.activity_game_radial);
        }else if(flowMode.equals(Game.FlowMode.Linear)){
            setContentView(R.layout.activity_game);
        }
        ButterKnife.bind(this);
        if(gameMode.equals(Game.GameMode.Speed)){
            startTimer(game.getTime());
        }
        FullscreenHelper.setFullscreen(this);
        setListeners();

        levelTitle.setText(String.format("Level %d",index));
        colorFlow.setLevel(level);

        showExpectedColor();
    }



    private void initialiseGame(Game game) {
        flowMode = game.getFlowMode();
        gameMode = game.getGameMode();
        index = game.getIndex();
        if(game.getDifficulty()!=null){
            LevelHandler.getInstance().setColors(this,game.getDifficulty());
        }else {
            LevelHandler.getInstance().setColors(this);
        }
        level = LevelHandler.getInstance().getLevel(index);
    }

    private void startTimer(int duration) {
        if(!Timer.getInstance().isRunning()){
            Timer.getInstance().addTimeObserver((observable, o) -> {
                if((int)o==Timer.FINISHED){
                    showSpeedModeTimeOver();
                }
            }).startTimer();
        }
    }

    private void showSpeedModeTimeOver() {
        finished = true;
        finish();
        Intent intent = new Intent(this,SpeedModeTimeOver.class);
        Game game = getIntent().getParcelableExtra("game");
        intent.putExtra("totalScore",PointsHandler.getInstance().getScore());
        intent.putExtra("game",game);
        startActivity(intent);
        PointsHandler.getInstance().reset();
        LevelHandler.getInstance().reset();
    }

    @SuppressLint("DefaultLocale")
    private void failure(int accuracy){
        showFailBorder();
        if(gameMode.equals(Game.GameMode.Speed)){
            colorFlow.setPaused(true);
            adjectiveView.setText("Not close enough!");
            successGroup.setVisibility(View.VISIBLE);
            accuracy=-100;
            scoreView.setText(String.format("%d", accuracy));
            //if(PointsHandler.getInstance().getScore()>=100) {
                PointsHandler.getInstance().addScore(accuracy, GameActivity.this);
                sessionScoreView.setText(String.format("Total score: %d", PointsHandler.getInstance().getScore()));
            //}

        }else {
            colorFlow.wrongColorClicked();
            new Handler().postDelayed(this::startOver, 4600);
            adjectiveView.setText("You failed!");
            retryButton.setVisibility(View.VISIBLE);
            retryButton.setText(String.format("Retry for %d", PointsHandler.getInstance().getRetryCost()));
            successGroup.setVisibility(View.VISIBLE);
            scoreView.setText(String.format("%d", accuracy));
            sessionScoreView.setText(String.format("Total score: %d", PointsHandler.getInstance().getScore()));
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @SuppressLint("DefaultLocale")
    private void success(int accuracy){
        PointsHandler.getInstance().addScore(accuracy,GameActivity.this);
        colorFlow.correctColorClicked();
        successGroup.setVisibility(View.VISIBLE);
        scoreView.setText(String.format("%d",accuracy));
        sessionScoreView.setText(String.format("Total score: %d",PointsHandler.getInstance().getScore()));
        //colorTitle.setVisibility(View.GONE);
        adjectiveView.setText(AdjectiveGiver.getAdjective(accuracy));
        new Handler().postDelayed(this::startNextLevel, 2200);
    }

    private void startNextLevel(){
        if(!finished) {
            finished = true;
            finish();
            PointsHandler.getInstance().saveHighscore(GameActivity.this);
            LevelHandler.getInstance().incrementLevel();
            Intent intent = new Intent(GameActivity.this, GameActivity.class);
            Game game = getIntent().getParcelableExtra("game");
            game.setIndex(game.getIndex()+1);
            intent.putExtra("game",game);
            startActivity(intent);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setListeners() {
        PointsHandler.getInstance().addObserver((observable, o) -> {
            if(!(o instanceof Highscore)){
                totalScoreView.setText(o+"");
            }
        });
        retryButton.setOnClickListener(view -> {
            if(PointsHandler.getInstance().retry(this)) {
                pressedRestart = true;
                finish();
                Intent intent = new Intent(GameActivity.this, GameActivity.class);
                intent.putExtra("game",(Game)getIntent().getParcelableExtra("game"));
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }else{
                Toast.makeText(this,"Not enough points!",Toast.LENGTH_LONG).show();
            }
        });

        colorFlow.setOnTouchListener((view, motionEvent) -> {
            if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                //colorFlow.performClick();
                if(!colorFlow.isPaused&&!succeeded) {
                    int accuracy = colorFlow.getAccuracy((int) motionEvent.getX(), (int) motionEvent.getY());
                    totalScoreView.setVisibility(View.VISIBLE);
                    if (accuracy >= level.getRequiredAccuracy()) {
                        succeeded = true;
                        success(accuracy);

                    } else {
                        failure(accuracy);
                    }
                    //colorTitle.setVisibility(View.VISIBLE);
                    colorFlow.setPaused(true);
                }else if(succeeded){
                    startNextLevel();
                }else if(gameMode!=null&&gameMode.equals("speed")){
                    startNextLevel();
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


    private void startOver(){
        if(!pressedRestart) {
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            LevelHandler.getInstance().reset();
            PointsHandler.getInstance().reset();
        }
    }

    private void showExpectedColor() {
        Intent intent = new Intent(this, ShowExpectedColorActivity.class);
        intent.putExtra("game",(Game) getIntent().getParcelableExtra("game"));
        intent.putExtra("expectedColor", level.getExpectedColor());
        startActivityForResult(intent,2);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2&&resultCode==RESULT_OK){
            colorFlow.start();
        }
    }
}
