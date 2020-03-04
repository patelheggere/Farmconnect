package com.patelheggere.farmconnect.service;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.patelheggere.farmconnect.notification.MyNotificationManager;
import com.patelheggere.farmconnect.utils.SharedPrefsHelper;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMessagingServ";
    public MyFirebaseMessagingService() {
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
        //getting the title and the body
        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();
        MyNotificationManager.getInstance(this).displayNotification(title, body);
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "onNewToken: "+token);
        SharedPrefsHelper.getInstance().save("FCM",token);
    }
}
