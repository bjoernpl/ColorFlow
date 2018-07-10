package com.example.android.colorflow;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;


public class ColorFlow extends View {

    private final Context context;
    private float[] positions = null;
    private int[] colors;
    private float angle;
    private int speed;
    private boolean leftToRight;

    private LinearGradient colorGradient;
    private int frame;
    private Paint paint;
    private int x0 = 0;
    private int x1 = 0;
    private static final int FRAMERATE = 240;
    private int WIDTH;
    private static final Shader.TileMode TILEMODE = Shader.TileMode.MIRROR;
    public boolean isPaused = true;
    private int expectedColor;
    private boolean expandFromCorrectColor = false;
    private int y;
    private int y1;
    private int factor;
    private static final float resizeAmount = 0.02f;
    private boolean fadeAway = false;
    private int fadeSpeed = 1;

    public ColorFlow(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public void setLevel(Level level) {
        this.fadeAway = false;
        this.speed = level.getSpeed();
        this.colors = level.getColors().clone();
        this.angle = level.getAngle();
        this.expectedColor = level.getExpectedColor();
        this.leftToRight = level.isLeftToRight();
        WIDTH = colors.length;

        colorGradient = new LinearGradient(x0,0,this.getWidth()*WIDTH,0,colors,null,TILEMODE );
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


    public void wrongColorClicked(){
        fadeAway = true;
    }

    public void wrongColorClicked(int speed){
        fadeAway = true;
        fadeSpeed = speed;
    }

    private void fadeToWhite(){
        for(int i = 0; i<colors.length;i++){
            colors[i] = adjustColors(colors[i]);
        }
    }

    @ColorInt
    public int adjustColors(@ColorInt int color){
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        if(red<=255-fadeSpeed){
            red += fadeSpeed;
        }
        if(green<=255-fadeSpeed){
            green += fadeSpeed;
        }
        if(blue<=255-fadeSpeed){
            blue += fadeSpeed;
        }
        return Color.rgb(red, green, blue);
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
                            positions[i] += (1-positions[i])/120;
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
                y = (int) (factor * angle * frame * this.getHeight() / (FRAMERATE / (WIDTH * 2)));
                y1 = (int) (factor * (this.getHeight() * WIDTH) * angle + y);
            }
            colorGradient = new LinearGradient(x0, y, x1, y1, colors, positions, TILEMODE);
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
