package com.madd.madd.broadcastreceiverpractice;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class MusicService extends Service {


    MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = MediaPlayer.create(this,R.raw.song_game);
        mediaPlayer.setLooping(true);
        mediaPlayer.setVolume(100,10);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mediaPlayer.start();
        for( int i = 0 ; i < 120 ; i++){
            task();
        }
        return START_STICKY;
    }


    void task() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if( mediaPlayer.isPlaying() ){
            mediaPlayer.stop();
        }
        mediaPlayer.release();
        mediaPlayer= null;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
