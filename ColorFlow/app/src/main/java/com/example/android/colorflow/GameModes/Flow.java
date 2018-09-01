package com.example.android.colorflow.GameModes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Shader;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.android.colorflow.Levels.Level;

public abstract class Flow extends View {

    static final int FRAMERATE = 480;
    static final Shader.TileMode TILEMODE = Shader.TileMode.MIRROR;
    static int fadeSpeed = 1;

    final   Context context;
    final   AttributeSet attrs;
    float[] positions = null;
    int[]   colors;
    Shader colorGradient;

    int     frame;

    float   angle;
    int     speed;
    int     WIDTH;
    int     expectedColor;
    int     factor;

    boolean fadeAway = false;
    boolean leftToRight;
    public boolean isPaused = true;

    public Flow(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.attrs = attrs;
    }

    AttributeSet getAttrs(){
        return attrs;
    }

    public void setLevel(Level level){
        this.fadeAway = false;
        this.speed = level.getSpeed();
        this.colors = level.getColors().clone();
        this.angle = level.getAngle();
        this.expectedColor = level.getExpectedColor();
        this.leftToRight = level.isLeftToRight();
        WIDTH = colors.length;
    }

    public void start(){
        isPaused = false;
        setVisibility(VISIBLE);
        invalidate();
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    public int getColor(int x,  int y){
        Bitmap b = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        layout(0, 0, getWidth(), getHeight());
        draw(c);
        return b.getPixel(x,y);
    }

    public int getAccuracy(int x,  int y){
        setPaused(true);
        int color = getColor(x,y);
        float rAccuracy = 100-Math.abs(getR(expectedColor)-getR(color))*(100f/255f);
        float gAccuracy = 100-Math.abs(getG(expectedColor)-getG(color))*(100f/255f);
        float bAccuracy = 100-Math.abs(getB(expectedColor)-getB(color))*(100f/255f);
        return (int)(rAccuracy+gAccuracy+bAccuracy)/3;
    }

    public void correctColorClicked(){
        setPaused(true);
    }

    public void setFadeAway(int speed){
        fadeSpeed = speed;
        fadeAway = true;
    }

    public void wrongColorClicked(){
        fadeAway = true;
        setPaused(true);
    }

    public void fadeToWhite(){
        for(int i = 0; i<colors.length;i++){
            colors[i] = adjustColors(colors[i]);
        }
    }

    @ColorInt
    private int adjustColors(@ColorInt int color){
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        if(red<=255-fadeSpeed){
            red += fadeSpeed;
        }else{
            red = 255;
        }
        if(green<=255-fadeSpeed){
            green += fadeSpeed;
        }else{
            green = 255;
        }
        if(blue<=255-fadeSpeed){
            blue += fadeSpeed;
        }else{
            blue = 255;
        }
        return Color.rgb(red, green, blue);
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
