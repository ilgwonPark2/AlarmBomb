package com.thenerds.ilgwon.alarmbomb.service;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;
import com.thenerds.ilgwon.alarmbomb.user_interface.AlarmSettingActivity;
import com.thenerds.ilgwon.alarmbomb.user_interface.Invitation;

import java.util.Map;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    private static final String TAG = FirebaseMessagingService.class.getSimpleName(); //for log

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.i(TAG, "onMessageReceived222");

        Map<String, String> data = remoteMessage.getData();
        String noti_title = remoteMessage.getNotification().getTitle();
        String noti_body=remoteMessage.getNotification().getBody();
        String title = data.get("title");
        String message = data.get("body");
        Log.i(TAG, title);
        Log.i(TAG, message);
        //receive type1 알람 신청 승낙?
        if (remoteMessage.getData().size() > 0) {
            if (title.equals("invitation")) {
                Log.i("ilgown222", "Hello");
//            startService(new Intent(this, AlarmRingService.class));
                Intent intent = new Intent(this, AlarmSettingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            //receive type2 승낙했음
            if (title == "OK") {
                Log.i(TAG, "OK");

                //알람세팅
            }
            //receive type3 거절했음
            if (title == "NO") {
                Log.i(TAG, "NO");

                //알람세팅 수정
            }
            if (title == "Mission_fail") {
                Log.i(TAG, "Mission_fail");

                //알람울리는 이벤트
            }
            if (title == "Wake") {
                Log.i(TAG, "Wake");

                //알람끄기
            }
        }
        if(remoteMessage.getNotification()!=null){
            if(noti_title.equals("inviation")){
                //intent로 엑티비티 변"
                String Friend=noti_body.split(",")[0];
                String Hour=noti_body.split(",")[1];
                String Minute=noti_body.split(",")[2];
                Intent intent = new Intent(this, Invitation.class);
                intent.putExtra("friend",Friend);
                intent.putExtra("hour",Hour);
                intent.putExtra("minute",Minute);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }

    }
}
