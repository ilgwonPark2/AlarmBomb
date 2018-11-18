package com.example.ilgwon.alarmbomb.user_interface;

import android.app.Activity;
import android.graphics.Color;
//import android.graphics.Typeface;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ilgwon.alarmbomb.R;
import com.example.ilgwon.alarmbomb.module_decibel_meter.FileUtil;
import com.example.ilgwon.alarmbomb.module_decibel_meter.MyMediaRecorder;
import com.example.ilgwon.alarmbomb.module_decibel_meter.SpeedMeter;
import com.example.ilgwon.alarmbomb.module_decibel_meter.World;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Date;

public class MissionDecibelMeterActivity extends Activity {
    boolean refreshed = false;
    SpeedMeter speedMeter;
//    public static Typeface tf;
//    ImageButton infoButton;
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
    private MyMediaRecorder mRecorder;

    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            DecimalFormat df1 = new DecimalFormat("####.0");
            if (msg.what == 1) {
                speedMeter.refresh();
                minVal.setText(df1.format(World.minDB));
                mmVal.setText(df1.format((World.minDB + World.maxDB) / 2));
                maxVal.setText(df1.format(World.maxDB));
                curVal.setText(df1.format(World.dbCount));
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
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.mission_decibel);
        //        tf= Typeface.createFromAsset(this.getAssets(), "fonts/Let_s go Digital Regular.ttf");
        minVal = (TextView) findViewById(R.id.minval);
//        minVal.setTypeface(tf);
        mmVal = (TextView) findViewById(R.id.mmval);
//        mmVal.setTypeface(tf);
        maxVal = (TextView) findViewById(R.id.maxval);
//        maxVal.setTypeface(tf);
        curVal = (TextView) findViewById(R.id.curval);
//        curVal.setTypeface(tf);
        //        infoButton=(ImageButton)findViewById(R.id.infobutton);
        //        infoButton.setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View view) {
        //                InfoDialog.Builder builder = new InfoDialog.Builder(MainActivity.this);
        //                builder.setMessage(getString(R.string.activity_infobull));
        //                builder.setTitle(getString(R.string.activity_infotitle));
        //                builder.setNegativeButton(getString(R.string.activity_infobutton),
        //                        new android.content.DialogInterface.OnClickListener() {
        //                            public void onClick(DialogInterface dialog, int which) {
        //                                dialog.dismiss();
        //                            }
        //                        });
        //                builder.create().show();
        //            }
        //        });
        refreshButton = (ImageButton) findViewById(R.id.refreshbutton);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshed = true;
                World.minDB = 100;
                World.dbCount = 0;
                World.lastDbCount = 0;
                World.maxDB = 0;
            }
        });

        speedMeter = (SpeedMeter) findViewById(R.id.speed);
        mRecorder = new MyMediaRecorder();
    }


    /* Sub-chant analysis */
    private void startListenAudio() {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isThreadRun) {
                    try {
                        if (bListener) {
                            volume = mRecorder.getMaxAmplitude();  //Get the sound pressure value
                            if (volume > 0 && volume < 1000000) {
                                World.setDbCount(20 * (float) (Math.log10(volume)));  //Change the sound pressure value to the decibel value
                                // Update with thread
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

    /**
     * Stop recording
     */
    @Override
    protected void onPause() {
        super.onPause();
        bListener = false;
        mRecorder.delete(); //Stop recording and delete the recording file
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
}
