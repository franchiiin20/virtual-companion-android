package companion.virtual.com.virtualcompanion.services;

import android.util.Log;

import com.google.android.gms.common.api.Response;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;

import companion.virtual.com.virtualcompanion.utils.ConstantUtils;

public class EventInstanceIDListenerService extends FirebaseInstanceIdService {

    public static final String TAG = "RegistrationLogs";
    public static final String TOPICS = "global";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + FirebaseInstanceId.getInstance().getToken());

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        try {
            FirebaseMessaging.getInstance().subscribeToTopic(TOPICS);
        } catch (Exception e) {
            Log.i("TAG", "Error during registering app push!");
        }
    }

}