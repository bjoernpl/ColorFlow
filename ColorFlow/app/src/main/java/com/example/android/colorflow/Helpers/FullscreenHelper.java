package com.example.android.colorflow.Helpers;

import android.app.Activity;
import android.content.Context;
import android.view.View;

public class FullscreenHelper {

    public static void setFullscreen(Activity activity){
        View decorView = activity.getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(uiOptions);
    }
}
