package com.bnpgames.android.Gradients.GameModes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.bnpgames.android.Gradients.Levels.Level;

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
        if (!isPaused) {
            frame += 1;
            if(frame <= FRAMERATE/2){
                radius *= 1.01f;
            }else{
                radius *= (1/1.01f);
            }
            frame %= FRAMERATE;

            int factor = leftToRight ? 1 : -1;

            x += x_direction;
            y += y_direction;
            if(x<0){
                x=0;
                x_direction *= -1;
            }
            if(y<0){
                y=0;
                y_direction *= -1;
            }
            if(x>getWidth()){
                x = getWidth();
                x_direction *= -1;
            }
            if(y>getHeight()){
                y=getHeight();
                y_direction *= -1;
            }

            colorGradient = new RadialGradient(x, y, radius, colors, null, TILEMODE);
            paint.setShader(colorGradient);
            canvas.drawPaint(paint);
            invalidate();
        }else{
            colorGradient = new RadialGradient(x, y, radius, colors, null, TILEMODE);
            paint.setShader(colorGradient);
            canvas.drawPaint(paint);
        }


    }


}
