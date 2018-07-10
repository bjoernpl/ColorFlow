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


public class ColorFlowRadial extends View {

    private final Context context;
    private int[] colors;
    private float angle;
    private int speed;
    private boolean leftToRight;

    private RadialGradient colorGradient;
    private int frame;
    private Paint paint;
    private int x0 = 0;
    private static final int FRAMERATE = 240;
    private int WIDTH;
    private static final Shader.TileMode TILEMODE = Shader.TileMode.MIRROR;
    public boolean isPaused = true;
    private int expectedColor;
    private int radius;
    public ColorFlowRadial(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public void setLevel(Level level) {
        this.speed = level.getSpeed();
        this.colors = level.getColors();
        this.angle = level.getAngle();
        this.expectedColor = level.getExpectedColor();
        this.leftToRight = level.isLeftToRight();
        radius = 3500;
        WIDTH = colors.length;

        colorGradient = new RadialGradient(x0,x0,radius,colors,null,TILEMODE);
        paint = new Paint();
        paint.setShader(colorGradient);
    }



    @Override
    public boolean performClick() {
        return super.performClick();
    }

    public void start(){
        isPaused = false;
        this.setVisibility(VISIBLE);
    }

    public int getColor(int x, int y){
        Bitmap b = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        layout(0, 0, getWidth(), getHeight());
        draw(c);
        return b.getPixel(x,y);
    }

    public int getAccuracy(int x, int y){
        int color = getColor(x,y);
        float rAccuracy = 100-Math.abs(getR(expectedColor)-getR(color))*(100f/255f);
        float gAccuracy = 100-Math.abs(getG(expectedColor)-getG(color))*(100f/255f);
        float bAccuracy = 100-Math.abs(getB(expectedColor)-getB(color))*(100f/255f);
        return (int)(rAccuracy+gAccuracy+bAccuracy)/3;
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if(!isPaused) {
                frame += speed;
            }
            frame %= FRAMERATE;
            int factor = leftToRight?1:-1;

            if(frame<FRAMERATE/4){
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
            }*/
            y = (int)(factor *angle*frame *this.getHeight()/(FRAMERATE/(WIDTH*2)));

            colorGradient = new RadialGradient(x0,x0*angle,radius,colors,null,TILEMODE);
            paint.setShader(colorGradient);
            canvas.drawPaint(paint);
            invalidate();

    }


    public void setPaused(boolean paused){
        isPaused = paused;
        if(!isPaused){
            invalidate();
        }
    }

    public static int getA(int color){
        return (color >> 24) & 0xff;
    }
    public static int getR(int color){
        return (color >> 16) & 0xff;
    }
    public static int getG(int color){
        return (color >>  8) & 0xff;
    }
    public static int getB(int color){
        return (color      ) & 0xff;
    }
}
