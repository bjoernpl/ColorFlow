package com.example.android.colorflow;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

    private TextView tv;
    private BackgroundView bgview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.textview);
        bgview =  findViewById(R.id.background);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        bgview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                    tv.setVisibility(View.GONE);
                    bgview.togglePause();
                    return true;
                }else if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    int color = ((BackgroundView)view).getColor((int)motionEvent.getX(),(int)motionEvent.getY());
                    //tv.setText(String.format("R: %d, G: %d, B: %d",getR(color),getG(color),getB(color)));
                    tv.setVisibility(View.VISIBLE);
                    bgview.togglePause();
                    return true;
                }
                return false;
            }
        });
    }






    
}
