package companion.virtual.com.virtualcompanion.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import companion.virtual.com.virtualcompanion.R;
import companion.virtual.com.virtualcompanion.utils.ConstantUtils;

public class FcmMessageListenerService extends FirebaseMessagingService {

    private static final String TAG = "FcmMessageLogs";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String message = remoteMessage.getData().get("message");
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Message: " + message + " " + remoteMessage.getNotification());

        if (remoteMessage.getFrom().startsWith("/topics/")) {
            // message received from some topic.
            Log.i(TAG, "A message received from some topic!");
        } else {
            // normal downstream message.
            Log.i(TAG, "A normal downstream message received!");
        }
    }

}