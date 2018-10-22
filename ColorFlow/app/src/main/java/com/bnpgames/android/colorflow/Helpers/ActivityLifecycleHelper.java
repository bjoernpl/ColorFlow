package com.bnpgames.android.colorflow.Helpers;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;


public class ActivityLifecycleHelper extends DummyLifeCycleCallback {

    private Listener listener;
    private int activityCounter = 0;

    public interface Listener{
        public void onEnterForeground();
        public void onEnterBackground();
    }

    public ActivityLifecycleHelper(Listener listener){
        this.listener = listener;
    }

    @Override
    public void onActivityStarted(Activity activity) {
        if(++activityCounter == 1) listener.onEnterForeground();
    }

    @Override
    public void onActivityStopped(Activity activity) {
        if(--activityCounter == 0) listener.onEnterBackground();
    }
}
