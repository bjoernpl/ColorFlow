package com.bnpgames.android.colorflow.Helpers;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.KeyEvent;
import android.widget.Toast;

import com.bnpgames.android.colorflow.R;

import static android.view.KeyEvent.KEYCODE_MEDIA_PAUSE;

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
        AudioManager manager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        if(manager!=null&&manager.isMusicActive())
        {
            pauseOtherMusic(context);
        }
    }

    public void setMusicDisabled(Context context) {
        context.getSharedPreferences(PREF_NAME,0).edit().putBoolean(MUSIC_ENABLED,false).apply();
        release(context);
    }

    public boolean isMusicEnabled(Context context){
        return context.getSharedPreferences(PREF_NAME,0).getBoolean(MUSIC_ENABLED,true);
    }


    private void pauseOtherMusic(Context context){
            KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_DOWN, KEYCODE_MEDIA_PAUSE);
            Intent intent = new Intent(Intent.ACTION_MEDIA_BUTTON);
            intent.putExtra(Intent.EXTRA_KEY_EVENT, keyEvent);
            context.sendOrderedBroadcast(intent, null);

            keyEvent = new KeyEvent(KeyEvent.ACTION_UP, KEYCODE_MEDIA_PAUSE);
            intent = new Intent(Intent.ACTION_MEDIA_BUTTON);
            intent.putExtra(Intent.EXTRA_KEY_EVENT, keyEvent);
            context.sendOrderedBroadcast(intent, null);
    }
}
