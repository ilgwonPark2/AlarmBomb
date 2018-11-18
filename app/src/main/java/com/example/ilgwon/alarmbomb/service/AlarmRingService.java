package com.example.ilgwon.alarmbomb.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;

public class AlarmRingService extends Service implements MediaPlayer.OnPreparedListener {
    //    private static final String ACTION_PLAY = "com.example.ilgwon.alarmbomb.service.PLAY";
    MediaPlayer mMediaPlayer = null;
    Thread thread;


    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                mMediaPlayer.start();
            } else if (msg.what == 0) {
                mMediaPlayer.stop();
            }
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        playSong();
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMediaPlayer.stop();
    }

    private void playSong() {
        try {
            //  make zombie player
            mMediaPlayer = MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI);
            mMediaPlayer.start();
            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        if (!mMediaPlayer.isPlaying()) {
                            Message message = new Message();
                            message.what = '1';
                            handler.sendMessage(message);
                        }
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
