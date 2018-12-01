package com.thenerds.ilgwon.alarmbomb.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;

public class AlarmRingService extends Service implements MediaPlayer.OnPreparedListener {
    MediaPlayer mMediaPlayer = null;
    Thread thread;


    /**
     * Thread handler
     */
    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // if msg.what is 1, it starts the mMediaPlayer.
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


    /**
     * Play ringtone
     */
    private void playSong() {
        try {
            // Play default ringtone.
            mMediaPlayer = MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI);
            mMediaPlayer.start();
            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    //  Make a continuous player
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
