package com.bnpgames.android.Gradients.Activities.Pregame;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bnpgames.android.Gradients.Activities.Ingame.GameActivity;
import com.bnpgames.android.Gradients.Fragments.ChooseGameModeFragment;
import com.bnpgames.android.Gradients.Fragments.CreditsFragment;
import com.bnpgames.android.Gradients.Fragments.FailedFragment;
import com.bnpgames.android.Gradients.Fragments.FlowModeFragment;
import com.bnpgames.android.Gradients.Fragments.SettingsFragment;
import com.bnpgames.android.Gradients.Fragments.SpeedModeFragment;
import com.bnpgames.android.Gradients.Fragments.StartFragment;
import com.bnpgames.android.Gradients.GameModes.ColorFlow;
import com.bnpgames.android.Gradients.GameModes.Game;
import com.bnpgames.android.Gradients.Helpers.ActivityLifecycleHelper;
import com.bnpgames.android.Gradients.Helpers.FullscreenHelper;
import com.bnpgames.android.Gradients.Helpers.GameFinishedHelper;
import com.bnpgames.android.Gradients.Helpers.MusicHelper;
import com.bnpgames.android.Gradients.Levels.LevelRandomizer;
import com.bnpgames.android.Gradients.R;
import com.bnpgames.android.Gradients.Resources.ColorHandler;
import com.bnpgames.android.Gradients.Statistics.Highscore;
import com.bnpgames.android.Gradients.Statistics.PointsHandler;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.google.ads.consent.*;
import com.google.android.gms.common.api.internal.LifecycleActivity;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StartActivity extends FragmentActivity implements FlowModeFragment.StartGameListener,
        ActivityLifecycleHelper.Listener,
        ChooseGameModeFragment.OnGameModeChosenListener,
        StartFragment.OnButtonPressedListener,
        SpeedModeFragment.OnSpeedModeSelectedListener ,
        FailedFragment.OnFragmentInteractionListener,
        SettingsFragment.OnSettingsInteractionListener
{

    @Nullable @BindView(R.id.highscore_text) TextView highscoretext;
    @Nullable @BindView(R.id.totalScoreText) TextView totalScoreText;
    @BindView(R.id.start_background) RelativeLayout background;
    @BindView(R.id.colorflow) ColorFlow colorFlow;
    private static final int REQUEST_CODE_GAME = 335;
    public static final int RESULT_CODE_LOST = 299;

    View startView;
    View gameModeView;
    private Game game;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getApplication().registerActivityLifecycleCallbacks(new ActivityLifecycleHelper(this));
        FullscreenHelper.setFullscreen(this);
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);
        initialiseAds();

        getSupportFragmentManager().beginTransaction().add(R.id.start_frame,new StartFragment()).addToBackStack(null).commit();

        colorFlow.setLevel(LevelRandomizer.getStartLevel(ColorHandler.getColors(this)));
        colorFlow.start();
    }

    @Override
    public void onCreditsClicked() {
        getSupportFragmentManager().beginTransaction().replace(R.id.start_frame,new CreditsFragment()).addToBackStack(null).commit();
    }

    @Override
    public void onDisableAdsClicked() {

    }

    @Override
    public void onBackPressedFromSettings() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onEnterForeground() {
        MusicHelper.getInstance(this).restart(this);
    }

    @Override
    public void onEnterBackground() {
        MusicHelper.getInstance(this).release(this);
    }

    private void initialiseAds() {

        MobileAds.initialize(this, "ca-app-pub-8431172432849630~7417322490");
        mAdView = findViewById(R.id.adViewStartScreen);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                Log.e("Failed to load ad","Errorcode:"+errorCode);
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the user is about to return
                // to the app after tapping on an ad.
            }
        });

    }

    @Override
    public void onModeChosen(Game.GameMode mode) {
        game = new Game(mode);
        if(mode.equals(Game.GameMode.Speed)){
            getSupportFragmentManager().beginTransaction().replace(R.id.start_frame,new SpeedModeFragment()).addToBackStack(null).commit();
        }else {
            Intent intent = new Intent(this, FlowModeSelectActivity.class);
            intent.putExtra("game", game);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        }
    }

    @Override
    public void onButtonPressed(StartFragment.StartButton button) {
        switch(button){
            case Start:
                getSupportFragmentManager().beginTransaction().replace(R.id.start_frame,new FlowModeFragment()).addToBackStack(null).commit();
                break;
            case Profile:
                Toast.makeText(StartActivity.this,"Premium version will be enabled soon!",Toast.LENGTH_LONG).show();
                break;
            case Settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.start_frame,new SettingsFragment()).addToBackStack(null).commit();
                break;
        }
    }

    @Override
    public void onStartPressed(Game.FlowMode flowMode) {
        game = new Game(Game.GameMode.Casual);
        game.setFlowMode(flowMode);
        game.setIndex(1);
        Intent intent = new Intent(StartActivity.this, GameActivity.class);
        intent.putExtra("game",game);
        startActivityForResult(intent,REQUEST_CODE_GAME);
    }

    @Override
    public void onSpeedModeSelected(Game.Difficulty difficulty, int time) {
        game.setDifficulty(difficulty);
        game.setTime(time);
        Intent intent = new Intent(this,FlowModeSelectActivity.class);
        intent.putExtra("game",game);
        startActivity(intent);
    }

    @Override
    public void onBackClicked() {
        backClicked(null);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("stopped","yo");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("restarted","yo");
    }

    @Override
    public void onPlayAgainClicked() {
        Intent intent = new Intent(StartActivity.this, GameActivity.class);
        intent.putExtra("game",game);
        startActivityForResult(intent,REQUEST_CODE_GAME);
    }

    @Override
    protected void onResume() {
        colorFlow.setLevel(LevelRandomizer.getStartLevel(ColorHandler.getColors(this)));
        FullscreenHelper.setFullscreen(this);
        super.onResume();
    }

    public void backClicked(View view) {
        getSupportFragmentManager().popBackStack();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE_GAME){
            if((resultCode==RESULT_OK && data!=null && data.hasExtra("gamefinished"))){
                getSupportFragmentManager().popBackStack();
                getSupportFragmentManager().beginTransaction().replace(R.id.start_frame, FailedFragment.newInstance(data.getParcelableExtra("gamefinished"))).addToBackStack(null).commit();
            }else if(GameFinishedHelper.getInstance()!=null&&GameFinishedHelper.getInstance().getGameFinished()!=null){
                getSupportFragmentManager().popBackStack();
                getSupportFragmentManager().beginTransaction().replace(R.id.start_frame, FailedFragment.newInstance(GameFinishedHelper.getInstance().getGameFinished())).addToBackStack(null).commit();
            }
        }
    }
}
