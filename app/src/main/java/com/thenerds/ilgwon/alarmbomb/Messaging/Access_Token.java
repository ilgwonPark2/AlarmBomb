package com.example.ilgwon.alarmbomb.Messaging;

import android.util.Log;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

public class Access_Token {
    public static String AccessToken() throws IOException
    {
        GoogleCredential googleCredential = GoogleCredential
                .fromStream(new FileInputStream("com/example/ilgwon/alarmbomb/Messaging/alarmbomb-5fbe8-firebase-adminsdk-3tmaz-aa67b3d00b.json"))
                .createScoped(Arrays.asList("https://www.googleapis.com/auth/firebase.messaging"));

        googleCredential.refreshToken();
        Log.i("Token", String.valueOf(googleCredential.refreshToken()));
        return googleCredential.getAccessToken();
    }
}
