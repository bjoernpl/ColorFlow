package com.bnpgames.android.colorflow.GameModes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.bnpgames.android.colorflow.Levels.Level;

import java.util.Random;


public class ColorFlowRadial extends Flow {


    private RadialGradient colorGradient;
    private Paint paint;
    private int radius = 1000;
    private float x;
    private float y;
    private boolean init = false;
    double x_direction;
    double y_direction;
    private boolean expandFromCorrectColor;
    private static final float speedConstant = 1.4f;

    public ColorFlowRadial(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setLevel(Level level) {
        super.setLevel(level);

        int speed = 12 * this.speed;

        x_direction = new Random().nextFloat()-0.5f;
        x_direction*=speed;

        y_direction = new Random().nextFloat()-0.5f;
        y_direction*=speed;
        colorGradient = new RadialGradient(x, y, radius, colors, null, TILEMODE);
        paint = new Paint();
        paint.setShader(colorGradient);
    }

    @Override
    public void correctColorClicked() {
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

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(!init){
            init = true;
            radius = this.getWidth()/2;
            x = this.getWidth() / 2f;
            y = this.getHeight() / 2f;
        }
        if(fadeAway){
            fadeToWhite();
        }
        if(expandFromCorrectColor){
            for(int i = 0;i<positions.length;i++){
                if(i<=getPositionOfExpectedColor()){
                    if(positions[i] != 0){
                        if(positions[i]/100f<=minExpand) positions[i] -= minExpand;
                        else positions[i] -= positions[i]/200f;
                    }
                }else{
                    if(positions[i] != 1){
                        if(positions[i]/100f<=minExpand) positions[i] += minExpand;
                        else positions[i] += positions[i]/200f;
                    }
                }
            }
        }else {
            if (!isPaused) {
                frame += 1;
                if (frame <= FRAMERATE / 2) {
                    radius = Math.round(radius * 1.01f);
                } else {
                    radius = Math.round(radius*(1 / 1.01f));
                }
                frame %= FRAMERATE;

                int factor = leftToRight ? 1 : -1;

                x +=    speedConstant*x_direction;
                y +=    speedConstant*y_direction;
                if (x < 0) {
                    x = 0;
                    x_direction *= -1;
                }
                if (y < 0) {
                    y = 0;
                    y_direction *= -1;
                }
                if (x > getWidth()) {
                    x = getWidth();
                    x_direction *= -1;
                }
                if (y > getHeight()) {
                    y = getHeight();
                    y_direction *= -1;
                }
            }
        }
        colorGradient = new RadialGradient(x, y, radius > 0 ? radius : 1, colors, positions, TILEMODE);
        paint.setShader(colorGradient);
        canvas.drawPaint(paint);
        invalidate();
    }


}
