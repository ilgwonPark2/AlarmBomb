package com.thenerds.ilgwon.alarmbomb.Messaging;

import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;

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
        if (title == "invitation") {
            Log.i("invitation", "invitation");
            //
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
        Log.i(TAG, title);
    }
}
