package com.example.android.colorflow.TimeHandling;

import android.os.Handler;

import java.util.Observable;

public class Timer extends Observable {

    private static Timer instance;
    private Handler handler;
    private int remainingSeconds;
    private boolean isRunning = false;
    private Runnable incrementer = new Runnable() {
        @Override
        public void run() {
            remainingSeconds--;
            setChanged();
            notifyObservers(remainingSeconds);
            if(remainingSeconds==0) {
                stopTimer();
            }else{
                handler.postDelayed(incrementer,1000);
            }
        }
    };

    private void stopTimer() {
        handler.removeCallbacks(incrementer);
        isRunning = false;
    }

    public static Timer getInstance(){
        if(instance==null)instance=new Timer();
        return instance;
    }

    public boolean isRunning(){
        return isRunning;
    }

    private Timer(){
        handler = new Handler();
    }

    public void setTime(int seconds){
        remainingSeconds = seconds;
    }

    public void startTimer(){
        handler.postDelayed(incrementer,1000);
        isRunning = true;
    }


}
