package com.thenerds.ilgwon.alarmbomb.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;
import com.thenerds.ilgwon.alarmbomb.Model.NotificationModel;
import com.thenerds.ilgwon.alarmbomb.R;
import com.thenerds.ilgwon.alarmbomb.user_interface.AlarmReceivedActivity;
import com.thenerds.ilgwon.alarmbomb.user_interface.AlarmSettingActivity;
import com.thenerds.ilgwon.alarmbomb.user_interface.AlarmWakedActivity;
import com.thenerds.ilgwon.alarmbomb.user_interface.Invitation;
import com.thenerds.ilgwon.alarmbomb.user_interface.AlarmWakedActivity;

import java.util.Map;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    private static final String TAG = FirebaseMessagingService.class.getSimpleName(); //for log

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.i(TAG, "onMessageReceived");

        Map<String, String> data = remoteMessage.getData();
        String title = data.get("title");
        //        String message = data.get("body");

        //receive type1 알람 신청 승낙?
        if (title.equals("invitation")) {
            Log.i("invitation", "invitation");
            String friend = data.get("friend_name");
            String time = data.get("time");
            String code = data.get("code");
            String from = data.get("from_user");
            sendNotification(friend, time, code, from);

        }
        //receive type2 승낙했음
        if (title.equals("YES")) {
            Log.i(TAG, "YES?");
            onlyNotification(title);

            //isfriend 수정
            String req = data.get("code");

            SharedPreferences pref = getApplicationContext().getSharedPreferences("Alarm", MODE_PRIVATE);
//            SharedPreferences.Editor editor = pref.edit();

            int size = pref.getInt("size", 0);
            Log.i(TAG, String.valueOf(size));
            for (int i = 1; i < size + 1; i++) {
                int reqCode = pref.getInt("list" + i + "reqCode", 0);
                //Log.i("TAG", String.valueOf(reqCode));
                if (String.valueOf(reqCode).equals(req)) {
                    Log.i("TAG", String.valueOf(reqCode));
                    Boolean result = pref.getBoolean("list" + i + "isFriend", false);
                    pref.edit().putBoolean("list" + i + "isFriend", true).apply();
                    pref.edit().commit();
                    result = pref.getBoolean("list" + i + "isFriend", false);
                    Log.i("TAG", String.valueOf(result));
                    break;

                }
            }


        }
        //receive type3 거절했음
        if (title.equals("NO")) {
            Log.i(TAG, "NO");
            onlyNotification(title);

            //pass
        }
        if (title.equals("mission_failure")) {
            Log.i(TAG, "mission_failure");
            String message = data.get("friend_name");
            Intent intent = new Intent(this, AlarmReceivedActivity.class);
            intent.putExtra("friend_name", message);
            //            Intent intent = new Intent(this, AlarmReceivedActivity.class);
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
            String message = data.get("friend_name");
            Intent intent = new Intent(this, AlarmWakedActivity.class);
            intent.putExtra("friend_name", message);
            stopService(intent);

            //알람끄기
        }
        if (title.equals("Wake")) {
            Log.i(TAG, "Wake");

            //알람끄기
        }
        Log.i(TAG, title);
    }

    private void sendNotification(String friend, String time, String code, String from) {
//        Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.naver.com"));
        Intent intent = new Intent(this, Invitation.class);
        intent.putExtra("friend", friend);
        intent.putExtra("time", time);
        intent.putExtra("code", code);
        intent.putExtra("from", from);
//        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1123, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.bomb)
                .setContentTitle(friend + " send you to Alarm BOMB!")
                .setContentText("at " + time + ". You AGREE?")
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1123, notificationBuilder.build());
        //startActivity(intent);


    }
    private void onlyNotification(String title){
//        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.bomb)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setDefaults(Notification.DEFAULT_ALL);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1123, notificationBuilder.build());

    }
}
