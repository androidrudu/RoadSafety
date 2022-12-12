package com.montbleu.fcm;



import android.text.TextUtils;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;



public class MyFirebaseMessagingService extends FirebaseMessagingService {

 //   private NotificationUtils notificationUtils;
    int MY_NOTIFICATION_ID = new Random().nextInt(26) + 75;
    ;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage == null)
            return;

        if (remoteMessage.getNotification() != null)
           // Handle_Notification(remoteMessage.getNotification().getBody());

        if (remoteMessage.getData() != null) {
            if (remoteMessage.getData().size() > 0) {
                Gson aGson = new Gson();
                JSONObject aJobject = new JSONObject(remoteMessage.getData());
            //    Notification_resp aNotification_Result;

                try {
                    if (!TextUtils.isEmpty(aJobject.getString("data"))) {
                     /*   aNotification_Result = aGson.fromJson(aJobject.getString("data"), Notification_resp.class);
                        Handle_Notification_Data(aNotification_Result, aJobject);*/
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}