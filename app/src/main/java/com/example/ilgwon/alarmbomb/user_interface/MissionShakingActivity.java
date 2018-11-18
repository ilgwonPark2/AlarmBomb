package com.example.ilgwon.alarmbomb.user_interface;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ilgwon.alarmbomb.R;
import com.example.ilgwon.alarmbomb.module_shaking.ShakeDetector;

public class MissionShakingActivity extends AppCompatActivity {
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;
    private TextView countTextview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mission_shaking);
        countTextview=(TextView)findViewById(R.id.count);
        countTextview.setText("You shake 0 times");
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

            @Override
            public void onShake(int count) {
                if(count==0){
                    Toast.makeText(getApplicationContext(),"Reset",Toast.LENGTH_SHORT).show();
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

    private void handleShakeEvent(int count) {

        countTextview.setText("You shake "+count+" times");


        if(count>100){
            Toast.makeText(getApplicationContext(),"mission complete",Toast.LENGTH_SHORT).show();

            finish();
        }

//        set_view(count);
    }

    //    public void set_view(int count){
//        countTextview.setText(count);
//    }
    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);

    }

}
