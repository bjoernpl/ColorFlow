package com.example.android.colorflow.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android.colorflow.GameModes.ColorFlow;
import com.example.android.colorflow.GameModes.ColorFlowRadial;
import com.example.android.colorflow.Levels.LevelRandomizer;
import com.example.android.colorflow.Resources.ColorHandler;
import com.example.android.colorflow.Statistics.Highscore;
import com.example.android.colorflow.Statistics.PointsHandler;
import com.example.android.colorflow.R;

import java.util.Random;

public class FlowModeSelectActivity extends Activity{

    TextView totalScoreText;
    RelativeLayout background;
    boolean isWhite = false;
    private RadioGroup group;
    private TextView nextButton;
    private ColorFlow colorFlow;
    private ColorFlowRadial colorFlowRadial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_mode_select);
        View decorView = getWindow().getDecorView();
        background = findViewById(R.id.gameChooserBackground);
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(uiOptions);
        /*
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
        */
        nextButton = findViewById(R.id.nextButton);
        colorFlow = findViewById(R.id.colorFlow);
        colorFlowRadial = findViewById(R.id.colorFlowRadial);
        nextButton.setOnClickListener(view -> startLevel());
        group = findViewById(R.id.radioGroupGameChooser);
        group.setOnCheckedChangeListener((radioGroup, i) -> {
            setWhiteText();
            nextButton.setVisibility(View.VISIBLE);
            switch (radioGroup.getCheckedRadioButtonId()){
                case R.id.radioButton:
                    colorFlow.setVisibility(View.VISIBLE);
                    colorFlowRadial.setVisibility(View.GONE);
                    colorFlow.setLevel(LevelRandomizer.getStartLevel(ColorHandler.getColors(this)));
                    colorFlow.start();

                    break;
                case R.id.radioButton2:
                    colorFlow.setVisibility(View.GONE);
                    colorFlowRadial.setVisibility(View.VISIBLE);
                    colorFlowRadial.setLevel(LevelRandomizer.getStartLevel(ColorHandler.getColors(this)));
                    colorFlowRadial.start();
                    break;
                case R.id.radioButton3:
                    if(new Random().nextBoolean()){
                        colorFlow.setVisibility(View.VISIBLE);
                        colorFlowRadial.setVisibility(View.GONE);
                        colorFlow.setLevel(LevelRandomizer.getStartLevel(ColorHandler.getColors(this)));
                        colorFlow.start();
                    }else{
                        colorFlow.setVisibility(View.GONE);
                        colorFlowRadial.setVisibility(View.VISIBLE);
                        colorFlowRadial.setLevel(LevelRandomizer.getStartLevel(ColorHandler.getColors(this)));
                        colorFlowRadial.start();
                    }
                    break;
            }
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

    private void startLevel() {
        Intent intent = new Intent(FlowModeSelectActivity.this, GameActivity.class);
        intent.putExtra("level",1);
        if(group.getCheckedRadioButtonId()==R.id.radioButton){
            intent.putExtra("flowMode","linear");
        }else if(group.getCheckedRadioButtonId()==R.id.radioButton2){
            intent.putExtra("flowMode","radial");
        }else{
            intent.putExtra("flowMode","random");
        }
        intent.putExtra("gameMode",getIntent().getStringExtra("gameMode"));
        if(getIntent().hasExtra("timeMode"))intent.putExtra("timeMode",getIntent().getIntExtra("timeMode",10));
        startActivity(intent);
    }


    private void setWhiteText(){
        if(!isWhite){
            for(int i = 0; i < group.getChildCount(); i++){
                RadioButton button = ((RadioButton)group.getChildAt(i));
                button.setTextColor(getResources().getColor(android.R.color.white));
                button.setButtonTintList(ColorStateList.valueOf(getResources().getColor(android.R.color.white)));
            }
            for(int i = 0; i < background.getChildCount(); i++){
                View view = background.getChildAt(i);
                if(view instanceof TextView){
                    TextView textview = ((TextView) view);
                    textview.setTextColor(getResources().getColor(android.R.color.white));
                    if(Build.VERSION.SDK_INT >=23) textview.setCompoundDrawableTintList(ColorStateList.valueOf(0xFFFFFF));

                }
            }
        }
    }
}
