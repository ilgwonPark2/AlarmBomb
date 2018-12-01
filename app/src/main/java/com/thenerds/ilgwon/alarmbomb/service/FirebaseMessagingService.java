package com.thenerds.ilgwon.alarmbomb.service;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;
import com.thenerds.ilgwon.alarmbomb.user_interface.AlarmReceivedActivity;
import com.thenerds.ilgwon.alarmbomb.user_interface.AlarmSettingActivity;
import com.thenerds.ilgwon.alarmbomb.user_interface.Invitation;

import java.util.Map;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    private static final String TAG = FirebaseMessagingService.class.getSimpleName(); //for log

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.i(TAG, "onMessageReceived");

        Map<String, String> data = remoteMessage.getData();
        String title = data.get("title");
        String message = data.get("content");

        //receive type1 알람 신청 승낙?
        if (title.equals("invitation")) {
            Log.i("invitation", "invitation");
            Intent intent = new Intent(this, AlarmSettingActivity.class);


        }
        //receive type2 승낙했음
        if (title.equals("OK")) {
            Log.i(TAG, "OK");

            //알람세팅
        }
        //receive type3 거절했음
        if (title.equals("NO")) {
            Log.i(TAG, "NO");

            //알람세팅 수정
        }
        if (title.equals("mission_failure")) {
            Log.i(TAG, "mission_failure");
            Intent intent = new Intent(this, AlarmReceivedActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        if (title.equals("waked")) {
            Log.i(TAG, "waked");
            Intent intent = new Intent(this, AlarmReceivedActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

            //알람끄기
        }


        if (title.equals("receiveCheck")) {
            Log.i(TAG, "received");
            Intent intent = new Intent(this, AlarmReceivedActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

            //알람끄기
        }
        if (title.equals("Wake")) {
            Log.i(TAG, "Wake");

            //알람끄기
        }
        Log.i(TAG, title);
    }
    private void sendNotification(){
        Intent intent=new Intent(this, Invitation.class);
//        intent.putExtra();
//        intent.putExtra();
//        intent.putExtra();
//        intent.putExtra();
//        intent.putExtra();


        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);


    }
}
