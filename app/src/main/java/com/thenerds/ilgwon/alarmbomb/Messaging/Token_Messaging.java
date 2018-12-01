package com.example.ilgwon.alarmbomb.Messaging;

import com.google.firebase.iid.FirebaseInstanceIdService;

public class Token_Messaging extends FirebaseInstanceIdService {
    private static final String TAG = Token_Messaging.class.getSimpleName();//for log

    @Override
    public void onTokenRefresh() {

    }
}
