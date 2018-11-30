package com.thenerds.ilgwon.alarmbomb.user_interface;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.thenerds.ilgwon.alarmbomb.R;
import com.thenerds.ilgwon.alarmbomb.module_shaking.ShakeDetector;

public class MissionShakingActivity extends AppCompatActivity {
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;
    private TextView countTextview;
    private int count = 300;
    private TextView timer;
    private CountDownTimer countDownTimer;
    private static final int Countdown = 30 * 1000;
    private static final int Interval = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mission_shaking);
        timer = (TextView) findViewById(R.id.timer);
        countDownTimer();
        countDownTimer.start();
        countTextview = (TextView) findViewById(R.id.count);
        countTextview.setText("You shake 0 times");
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

            @Override
            public void onShake(int count) {
                if (count == 0) {
                    Toast.makeText(getApplicationContext(), "Reset", Toast.LENGTH_SHORT).show();
                }
                /*
                 * The following method, "handleShakeEvent(count):" is a stub //
                 * method you would use to setup whatever you want done once the
                 * device has been shook.
                 */

                handleShakeEvent(count);
            }

        });

    }

    public void countDownTimer() {
        countDownTimer = new CountDownTimer(Countdown, Interval) {
            @Override
            public void onTick(long l) {
                timer.setText("You only left " + String.valueOf(count) + " sec");
                count--;

            }

            @Override
            public void onFinish() {
                timer.setText("Bomb sending..");
                Intent intent = getIntent();
                intent.putExtra("fail", true);
                setResult(Activity.RESULT_CANCELED, intent);
                finish();
            }
        };
    }

    private void handleShakeEvent(int count) {
        countTextview.setText("You shake " + count + " times");
        timer.setTextColor(Color.RED);
        if (count > 100) {
            Toast.makeText(getApplicationContext(), "mission complete", Toast.LENGTH_SHORT).show();
            Intent intent = getIntent();
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
        //        set_view(count);
    }
    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI);

    }

}
