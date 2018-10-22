package com.bnpgames.android.colorflow.Activities.Pregame;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.AudioManager;
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

import com.bnpgames.android.colorflow.Activities.Ingame.GameActivity;
import com.bnpgames.android.colorflow.Fragments.ChooseGameModeFragment;
import com.bnpgames.android.colorflow.Fragments.CreditsFragment;
import com.bnpgames.android.colorflow.Fragments.FailedFragment;
import com.bnpgames.android.colorflow.Fragments.FlowModeFragment;
import com.bnpgames.android.colorflow.Fragments.SettingsFragment;
import com.bnpgames.android.colorflow.Fragments.SpeedModeFragment;
import com.bnpgames.android.colorflow.Fragments.StartFragment;
import com.bnpgames.android.colorflow.GameModes.ColorFlow;
import com.bnpgames.android.colorflow.GameModes.Game;
import com.bnpgames.android.colorflow.Helpers.ActivityLifecycleHelper;
import com.bnpgames.android.colorflow.Helpers.FullscreenHelper;
import com.bnpgames.android.colorflow.Helpers.GameFinishedHelper;
import com.bnpgames.android.colorflow.Helpers.GameHelper;
import com.bnpgames.android.colorflow.Helpers.MusicHelper;
import com.bnpgames.android.colorflow.Helpers.PurchaseHelper;
import com.bnpgames.android.colorflow.Levels.LevelRandomizer;
import com.bnpgames.android.colorflow.R;
import com.bnpgames.android.colorflow.Resources.ColorHandler;
import com.bnpgames.android.colorflow.Statistics.Highscore;
import com.bnpgames.android.colorflow.Statistics.PointsHandler;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.google.ads.consent.*;
import com.google.android.gms.common.api.internal.LifecycleActivity;
import com.google.firebase.analytics.FirebaseAnalytics;


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

    private FirebaseAnalytics firebaseAnalytics;

    private static final int REQUEST_CODE_GAME = 335;
    public static final int RESULT_CODE_LOST = 299;

    View startView;
    View gameModeView;
    private Game game;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        AudioManager manager = (AudioManager)this.getSystemService(Context.AUDIO_SERVICE);
        if(manager!=null&&manager.isMusicActive()&&MusicHelper.getInstance(this).isMusicEnabled(this))
        {
            MusicHelper.getInstance(this).setMusicDisabled(this);
            Toast.makeText(this,"We don't want to interrupt your songs, so in-game music was disabled. Go to settings to enable",Toast.LENGTH_LONG).show();
        }
        getApplication().registerActivityLifecycleCallbacks(new ActivityLifecycleHelper(this));
        FullscreenHelper.setFullscreen(this);
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);
        initialiseAds();

        new PurchaseHelper(this, hasPremium -> {
              if(mAdView!=null)mAdView.setVisibility(View.GONE);
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.start_frame,new StartFragment()).commit();

        colorFlow.setLevel(LevelRandomizer.getStartLevel(ColorHandler.getColors(this)));
        colorFlow.start();
    }

    @Override
    public void onCreditsClicked() {
        getSupportFragmentManager().beginTransaction().replace(R.id.start_frame,new CreditsFragment()).addToBackStack(null).commit();
    }

    @Override
    public void onDisableAdsClicked() {
        showPurchaseDialog();
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

        if(!PurchaseHelper.hasPremium(this)) {
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
    }

    @Override
    public void onModeChosen(Game.GameMode mode) {
        game = new Game(mode);
        if(mode.equals(Game.GameMode.Speed)){
            getSupportFragmentManager().beginTransaction().replace(R.id.start_frame,new SpeedModeFragment()).addToBackStack(null).commit();
        }else {
            GameHelper.startGame(this,game);
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        }
    }

    @Override
    public void onButtonPressed(StartFragment.StartButton button) {
        switch(button){
            case Start:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out).replace(R.id.start_frame,new FlowModeFragment()).addToBackStack(null).commit();
                break;
            case Profile:
                //Toast.makeText(StartActivity.this,"Premium version will be enabled soon!",Toast.LENGTH_LONG).show();
                showPurchaseDialog();
                break;
            case Settings:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out).replace(R.id.start_frame,new SettingsFragment()).addToBackStack(null).commit();
                break;
        }
    }

    private void showPurchaseDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(getLayoutInflater().inflate(R.layout.purchase_premium_dialog,null));
        AlertDialog dialog = builder.create();
        dialog.show();

        PurchaseHelper helper = new PurchaseHelper(this, new PurchaseHelper.PurchaseInteractionListener() {
            @Override
            public void onPriceReceived(String price) {
                TextView priceText = dialog.findViewById(R.id.purchase_price);
                priceText.setText(String.format("Price: %s",price));
            }

            @Override
            public void onPurchaseCanceled() {

            }

            @Override
            public void onPurchaseSuccess() {

                AlertDialog.Builder builder = new AlertDialog.Builder(StartActivity.this);
                builder.setView(getLayoutInflater().inflate(R.layout.thanks_for_premium_view,null));
                AlertDialog dialog = builder.create();
                dialog.show();
                mAdView.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.start_frame,new StartFragment()).commit();
                new Handler().postDelayed(dialog::dismiss,6000);
            }

            @Override
            public void onPurchaseFailed() {
                new Handler().postDelayed(() -> FullscreenHelper.setFullscreen(StartActivity.this),200);
                Toast.makeText(StartActivity.this,"Something went wrong, please try again",Toast.LENGTH_LONG).show();
            }
        });
        dialog.setOnDismissListener(dialogInterface -> FullscreenHelper.setFullscreen(StartActivity.this));
        dialog.findViewById(R.id.purchase_cancel).setOnClickListener(view -> {
            dialog.dismiss();
        });
        dialog.findViewById(R.id.purchase_confirm).setOnClickListener(view -> {
            helper.startBillingFlow();
            dialog.dismiss();
        });

    }

    @Override
    public void onStartPressed(Game.FlowMode flowMode) {
        game = new Game(Game.GameMode.Casual);
        game.setFlowMode(flowMode);
        game.setIndex(1);
        GameHelper.startGameForResult(this,game,REQUEST_CODE_GAME);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
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
        GameHelper.startGameForResult(this,game,REQUEST_CODE_GAME);
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
                getSupportFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out,android.R.anim.slide_in_left,R.anim.slide_out_right).replace(R.id.start_frame, FailedFragment.newInstance(data.getParcelableExtra("gamefinished"))).addToBackStack(null).commit();
            }else if(GameFinishedHelper.getInstance()!=null&&GameFinishedHelper.getInstance().getGameFinished()!=null){
                getSupportFragmentManager().popBackStack();
                getSupportFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out,android.R.anim.slide_in_left,R.anim.slide_out_right).replace(R.id.start_frame, FailedFragment.newInstance(GameFinishedHelper.getInstance().getGameFinished())).addToBackStack(null).commit();
            }
        }
    }
}
