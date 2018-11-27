package com.example.ilgwon.alarmbomb.user_interface;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ilgwon.alarmbomb.R;
import com.example.ilgwon.alarmbomb.module_decibel_meter.DecibelData;
import com.example.ilgwon.alarmbomb.module_decibel_meter.DecibelMeter;
import com.example.ilgwon.alarmbomb.module_decibel_meter.DecibelRecorder;
import com.example.ilgwon.alarmbomb.module_decibel_meter.FileUtil;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Date;

public class MissionDecibelMeterActivity extends Activity implements android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback {
    boolean refreshed = false;
    DecibelMeter decibelMeter;
    ImageButton refreshButton;
    TextView minVal;
    TextView maxVal;
    TextView mmVal;
    TextView curVal;
    long currentTime = 0;
    /* Decibel */
    private boolean bListener = true;
    private boolean isThreadRun = true;
    private Thread thread;
    float volume = 10000;
    int refresh = 0;
    private DecibelRecorder mRecorder;
    CountDownTimer myCountDownTimer;
    private static final int Countdown = 10 * 1000;
    private static final int Interval = 1000;


    /**
     * Thread Handler.
     */
    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            DecimalFormat df1 = new DecimalFormat("####.0");
            // update meter figures
            if (msg.what == 1) {
                decibelMeter.refresh();
                minVal.setText(df1.format(DecibelData.minDB));
                mmVal.setText(df1.format((DecibelData.minDB + DecibelData.maxDB) / 2));
                maxVal.setText(df1.format(DecibelData.maxDB));
                curVal.setText(df1.format(DecibelData.dbCount));

                // if decibel exceeds 90, it terminates the mission.
                if (DecibelData.dbCount > 90) {
                    Toast.makeText(getApplicationContext(), "mission complete", Toast.LENGTH_SHORT).show();
                    Intent intent = getIntent();
                    intent.putExtra("complete", true);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
                if (refresh == 1) {
                    long now = new Date().getTime();
                    now = now - currentTime;
                    now = now / 1000;
                    refresh = 0;
                } else {
                    refresh++;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mission_decibel);
        // check permission for this mission, dynamically checking.
        minVal = findViewById(R.id.minval);
        mmVal = findViewById(R.id.mmval);
        maxVal = findViewById(R.id.maxval);
        curVal = findViewById(R.id.curval);

        // set a refresh button.
        refreshButton = findViewById(R.id.refreshbutton);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshed = true;
                DecibelData.minDB = 100;
                DecibelData.dbCount = 0;
                DecibelData.lastDbCount = 0;
                DecibelData.maxDB = 0;
            }
        });

        // declare the recorder.
        decibelMeter = findViewById(R.id.speed);
        mRecorder = new DecibelRecorder();

        countDownTimer();
        myCountDownTimer.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        File file = FileUtil.createFile("temp.amr");
        if (file != null) {
            startRecord(file);
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.activity_recFileErr), Toast.LENGTH_LONG).show();
        }
        bListener = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        bListener = false;
        //  Stop recording and delete the recording file
        mRecorder.delete();
        thread = null;
    }

    @Override
    protected void onDestroy() {
        if (thread != null) {
            isThreadRun = false;
            thread = null;
        }
        mRecorder.delete();
        super.onDestroy();
    }

    /**
     * start listening Audio
     */
    private void startListenAudio() {
        // new Background Thread
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                // run the thread while Thread is run(listening an audio)
                while (isThreadRun) {
                    try {
                        if (bListener) {
                            //  Get the sound pressure value
                            volume = mRecorder.getMaxAmplitude();
                            if (volume > 0 && volume < 1000000) {
                                //  Change the sound pressure value to the decibel value
                                //  Formula reference: https://code.i-harness.com/ko-kr/q/a297d7
                                DecibelData.setDbCount(20 * (float) (Math.log10(volume)));
                                //  Update with thread
                                Message message = new Message();
                                message.what = 1;
                                handler.sendMessage(message);
                            }
                        }
                        if (refreshed) {
                            Thread.sleep(1200);
                            refreshed = false;
                        } else {
                            Thread.sleep(200);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        bListener = false;
                    }
                }
            }
        });
        thread.start();
    }

    /**
     * Start recording
     *
     * @param fFile
     */
    public void startRecord(File fFile) {
        try {
            mRecorder.setMyRecAudioFile(fFile);
            if (mRecorder.startRecorder()) {
                startListenAudio();
            } else {
                Toast.makeText(this, getString(R.string.activity_recStartErr), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.activity_recBusyErr), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void countDownTimer() {
        myCountDownTimer = new CountDownTimer(Countdown, Interval) {
            @Override
            public void onTick(long l) {
//                timer.setText("You only left " + String.valueOf(count) + " sec");
//                count--;

            }

            @Override
            public void onFinish() {
                Intent intent = getIntent();
                intent.putExtra("fail", true);
                setResult(Activity.RESULT_CANCELED, intent);
                finish();

            }
        };
    }
}

