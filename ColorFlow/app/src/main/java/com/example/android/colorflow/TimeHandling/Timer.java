package com.example.android.colorflow.TimeHandling;

import android.os.CountDownTimer;
import android.os.Handler;

import java.util.Observable;
import java.util.Observer;

public class Timer extends Observable {

    public static final int FINISHED = 233;

    /* Holds an instance of the Timer so it is accessible from different activities*/
    private static Timer instance;

    /* The timer doing the counting*/
    private CountDownTimer timer;

    /* Is the timer running or not */
    private boolean isRunning = false;

    /* keeps track of how many seconds are left*/
    private int remainingSeconds;

    /**
     * Return either a new object or an existing instance
     * @return instance of Timer
     */
    public static Timer getInstance(){
        if(instance==null)instance=new Timer();
        return instance;
    }

    /**
     * Accessor function
     * @return if the timer is running or not
     */
    public boolean isRunning(){
        return isRunning;
    }

    /**
     * Sets the total time of the timer. Also notifies observers when a tick happens or the timer runs out
     * @param seconds the amount of seconds the timer should run
     * @return this instance of Timer so arguments can be chained
     */
    public Timer setTime(int seconds){
        timer = new CountDownTimer(seconds,1000) {
            @Override
            public void onTick(long l) {
                remainingSeconds = (int)l;
                setChanged();
                notifyObservers(l);
            }

            @Override
            public void onFinish() {
                isRunning = false;
                setChanged();
                notifyObservers(FINISHED);
            }
        };
        return this;
    }

    /**
     * Starts the timer
     */
    public void startTimer(){
        isRunning = true;
        timer.start();
    }

    /**
     * Allow adding an observer in a chain of funtions e.g. Timer.getInstance().setTime(10).addTimeObserver(obs).startTimer();
     * @param obs the observer to be added
     * @return this instance of Timer
     */
    public Timer addTimeObserver(Observer obs){
        addObserver(obs);
        return this;
    }






}
