package com.example.android.colorflow;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Observable;
import java.util.Random;


public class BackgroundView extends View {

    private LinearGradient colorGradient;
    private int[] colors = {Color.BLUE,Color.CYAN};
    private float[] positions = null;//{0.25f,0.75f};
    private int frame;
    private Paint paint;
    private int x0 = 0;
    private static final int FRAMERATE = 240;
    private final int WIDTH = colors.length;
    private static final Shader.TileMode TILEMODE = Shader.TileMode.MIRROR;
    private boolean isPaused = false;
    private int x;
    private int y;
    private int color;
    private Canvas canvas;
    private Random random;
    private float whitePosition = 0;
    private int x1;

    public BackgroundView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);


        colorGradient = new LinearGradient(x0,0,this.getWidth()*WIDTH,0,colors,positions,TILEMODE );
        paint = new Paint();
        random = new Random();
        paint.setShader(colorGradient);
    }

    public int getColor(int x, int y){
        Bitmap b = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        layout(0, 0, getWidth(), getHeight());
        draw(c);
        return b.getPixel(x,y);
    }



    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if(!isPaused) {
                frame += 1;
            }
            frame %= FRAMERATE;
            if(positions!=null) {
                positions = new float[]{(1 - whitePosition) / 2, 1 - whitePosition, 1};
                if(whitePosition!=1)whitePosition += 0.03;
                if(x1 == 1)x1=x0+(this.getWidth() * WIDTH);
                x0 *= 1.03f;
                colorGradient = new LinearGradient(x0, x0*0.4f, x1, 0, colors, positions, TILEMODE);

            }else {
                x0 = -frame * this.getWidth() / (FRAMERATE / (WIDTH * 2));
                colorGradient = new LinearGradient(x0, x0*0.4f, (this.getWidth() * WIDTH) + x0, ((this.getWidth() * WIDTH) + x0)*0.4f, colors, positions, TILEMODE);
            }
            paint.setShader(colorGradient);
            canvas.drawPaint(paint);
            invalidate();
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    public void togglePause(){
        isPaused = !isPaused;
        if(!isPaused){
            invalidate();
        }
    }




    public void fadeToWhite(){
        colors = new int[]{colors[0],colors[1],Color.WHITE};
        positions = new float[]{0.5f,1,1};
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

}
