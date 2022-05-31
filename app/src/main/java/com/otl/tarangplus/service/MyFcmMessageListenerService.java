package com.otl.tarangplus.service;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.otl.tarangplus.MyApplication;
import com.webengage.sdk.android.WebEngage;

import java.util.Map;

/**/
public class MyFcmMessageListenerService extends FirebaseMessagingService {

    private static String TAG = MyFcmMessageListenerService.class.getSimpleName();
    private Handler mHandler = null;
    private String title = "";
    private String title_1 = "";
    private String image_url = "";
    private String description = "";


    @Override
    public void onMessageReceived(RemoteMessage message) {
        super.onMessageReceived(message);
        Log.d(TAG, " Firebase onMessageReceived");

        Map<String, String> data = message.getData();
        if(data != null) {
            if(data.containsKey("source") && "webengage".equals(data.get("source"))) {
                WebEngage.get().receive(data);
            }
        }

        if (MyApplication.isFirebaseActive && message.getData().size() > 0) {

            String title = message.getData().get("title");
            String image_url = message.getData().get("imageurl");
            String description = message.getData().get("description");
            Intent intent = new Intent("com.otl.tarangplus_FCM-MESSAGE");
            intent.putExtra("title", title);
            intent.putExtra("imageurl", image_url);
            intent.putExtra("description", description);
            LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
            localBroadcastManager.sendBroadcast(intent);


            Bundle extras = new Bundle();
            for (Map.Entry<String, String> entry : message.getData().entrySet()) {
                if (entry != null && entry.getKey() != null && entry.getKey().equalsIgnoreCase("title")) {
                    title_1 = entry.getValue().toString();
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        public void run() {
                            //Toast.makeText(getApplicationContext(), title_1, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }
    }


    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        WebEngage.get().setRegistrationID(s);
        Log.e("onNewToken", s);
    }


}
