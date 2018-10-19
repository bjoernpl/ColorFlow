package com.bnpgames.android.Gradients.Helpers;

import android.content.Context;
import android.media.MediaPlayer;

import com.bnpgames.android.Gradients.R;

public class MusicHelper {

    private static final String PREF_NAME = "music";
    private static final String SONG_POSITION = "time";
    private static final String MUSIC_ENABLED = "musicenabled";

    private static MusicHelper instance;
    private MediaPlayer mediaPlayer;

    public static MusicHelper getInstance(Context context) {
        if(instance==null)instance = new MusicHelper(context);
        return instance;
    }

    private MusicHelper(Context context){
        if(isMusicEnabled(context)) {
            mediaPlayer = MediaPlayer.create(context, R.raw.aether_theories);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }
    }

    public void release(Context context){
        if(mediaPlayer!=null) {
            context.getSharedPreferences(PREF_NAME, 0).edit().putInt(SONG_POSITION, mediaPlayer.getCurrentPosition()).apply();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public void restart(Context context){
        if(isMusicEnabled(context) && mediaPlayer==null) {
            mediaPlayer = MediaPlayer.create(context, R.raw.aether_theories);
            mediaPlayer.setLooping(true);
            mediaPlayer.seekTo(context.getSharedPreferences(PREF_NAME, 0).getInt(SONG_POSITION, 0));
            mediaPlayer.start();
        }
    }

    public void start(){
        mediaPlayer.start();
    }


    public void setMusicEnabled(Context context) {
        context.getSharedPreferences(PREF_NAME,0).edit().putBoolean(MUSIC_ENABLED,true).apply();
        restart(context);
    }

    public void setMusicDisabled(Context context) {
        context.getSharedPreferences(PREF_NAME,0).edit().putBoolean(MUSIC_ENABLED,false).apply();
        release(context);
    }

    public boolean isMusicEnabled(Context context){
        return context.getSharedPreferences(PREF_NAME,0).getBoolean(MUSIC_ENABLED,true);
    }
}
