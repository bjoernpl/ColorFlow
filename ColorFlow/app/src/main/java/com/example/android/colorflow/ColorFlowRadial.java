package com.example.android.colorflow;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;


public class ColorFlowRadial extends Flow {


    private RadialGradient colorGradient;
    private Paint paint;
    private int radius;

    public ColorFlowRadial(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setLevel(Level level) {
        super.setLevel(level);
        radius = 3500;
        colorGradient = new RadialGradient(this.getWidth() / 2, this.getHeight() / 2, radius, colors, null, TILEMODE);
        paint = new Paint();
        paint.setShader(colorGradient);
    }


    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isPaused) {
            frame += speed;
        }
        frame %= FRAMERATE;
        int factor = leftToRight ? 1 : -1;

            /*if(frame<FRAMERATE/4){
                x0 += speed*50;
            }else if(frame<3*FRAMERATE/4){
                x0 -= speed*50;
            }else{
                x0 += speed*50;
            }


            int y;
            /*if(frame<FRAMERATE/2){
                y = (int) (factor * frame * this.getHeight() * angle / (FRAMERATE/2));
            }else{
                y = (int) (factor * this.getHeight() * angle) - (int)(factor * (frame-FRAMERATE/2) * this.getHeight() * angle/ (FRAMERATE/2));
            }
            y = (int)(factor *angle*frame *this.getHeight()/(FRAMERATE/(WIDTH*2)));

            colorGradient = new RadialGradient(x0,x0*angle,radius,colors,null,TILEMODE);*/
        int addition = frame > FRAMERATE / 2 ? -1 * (FRAMERATE - frame) : frame;
        colorGradient = new RadialGradient(this.getWidth() / 2, this.getHeight() / 2, radius += addition, colors, null, TILEMODE);
        paint.setShader(colorGradient);
        canvas.drawPaint(paint);
        invalidate();

    }
}
