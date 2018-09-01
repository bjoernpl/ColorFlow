package com.example.android.colorflow.GameModes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.example.android.colorflow.Levels.Level;

import java.util.ArrayList;


public class ColorFlow extends Flow {



    private Paint paint;
    private int x0 = 0;
    private int x1 = 0;

    private boolean expandFromCorrectColor = false;
    private int y;
    private int y1;

    public ColorFlow(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    public void setLevel(Level level) {
        super.setLevel(level);

        colorGradient = new LinearGradient(x0,0,super.getWidth()*WIDTH,0,colors,null,TILEMODE );
        paint = new Paint();
        paint.setShader(colorGradient);
    }

    public void start(){
        super.start();
        this.setVisibility(VISIBLE);
        invalidate();
    }


    @Override
    public void correctColorClicked(){
        newColors();
        positions = new float[colors.length];
        for(int i = 0; i<colors.length-1;i++){
            positions[i] = i*(1f/(colors.length-2));
        }
        int expectedPosition = getPositionOfExpectedColor();
        for(int i = colors.length-1 ; i > expectedPosition;i--){
            positions[i] = positions[i-1];
        }
        //System.arraycopy(colors,expectedPosition+1,colors,expectedPosition+2,);
        positions[expectedPosition+1] = positions[expectedPosition];
        expandFromCorrectColor = true;
    }

    private int getPositionOfExpectedColor(){
        for(int i = 0; i<colors.length;i++){
            if(colors[i]==expectedColor){
                return i;
            }
        }
        return 0;
    }


    private  void newColors(){
        ArrayList<Integer> newcolors = new ArrayList<Integer>();
        for(int i : colors){
            newcolors.add(i);
        }
        newcolors.add(getPositionOfExpectedColor(),expectedColor);
        colors = new int[newcolors.size()];
        for(int i = 0; i< newcolors.size();i++){
            colors[i] = newcolors.get(i);
        }
    }


    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if(fadeAway){
                fadeToWhite();
            }
            if(expandFromCorrectColor){
                for(int i = 0;i<positions.length;i++){
                    if(i<=getPositionOfExpectedColor()){
                        if(positions[i] != 0){
                            positions[i] -= positions[i]/120f;
                        }
                    }else{
                        if(positions[i] != 1){
                            positions[i] += (1-positions[i])/120f;
                        }
                    }
                }
            }else {
                if (!isPaused) {
                    frame += speed;
                }
                frame %= FRAMERATE;

                factor = leftToRight ? 1 : -1;
                x0 = factor * frame * this.getWidth() / (FRAMERATE / (WIDTH * 2));
                x1 = factor * (this.getWidth() * WIDTH) + x0;
                y = (int) (factor * angle * frame * super.getHeight() / (FRAMERATE / (WIDTH * 2)));
                y1 = (int) (factor * (super.getHeight() * WIDTH) * angle + y);
            }
            colorGradient = new LinearGradient(x0, y, x1, y1, colors, positions, TILEMODE);
            paint.setShader(colorGradient);
            canvas.drawPaint(paint);
            invalidate();
    }



}
