package com.bnpgames.android.colorflow.GameModes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.constraint.solver.widgets.WidgetContainer;
import android.util.AttributeSet;

import com.bnpgames.android.colorflow.Levels.Level;

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

    public void startFromStatus(FlowStatus status){
        super.setLevel(status.getLevel());

        colorGradient = new LinearGradient(status.getX1(), status.getY1(), status.getX2(), status.getY2(), colors, null, TILEMODE);
        paint = new Paint();
        paint.setShader(colorGradient);
        start();
    }

    public void start(){
        super.start();
        this.setVisibility(VISIBLE);
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
                            if(positions[i]/100f<=minExpand) positions[i] -= minExpand;
                            else positions[i] -= positions[i]/240f;
                        }
                    }else{
                        if(positions[i] != 1){
                            if(positions[i]/100f<=minExpand) positions[i] += minExpand;
                            else positions[i] += positions[i]/240f;
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
            colorGradient = null;
            invalidate();
    }

    public FlowStatus getStatus(){
        return new FlowStatus(x0,x1,y,y1,level);
    }






}
