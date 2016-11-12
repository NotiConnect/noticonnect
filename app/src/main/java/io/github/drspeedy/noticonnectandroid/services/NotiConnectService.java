package io.github.drspeedy.noticonnectandroid.services;

import android.accessibilityservice.AccessibilityService;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import io.github.drspeedy.noticonnectandroid.tasks.network.NotiConnectClient;
import io.github.drspeedy.noticonnectandroid.models.User;


/**
 * Created by doc on 11/6/16.
 */

public class NotiConnectService extends AccessibilityService {

    private static final String TAG = NotiConnectService.class.getSimpleName();

    private NotiConnectClient mClient;
    private String mAccessToken;

    @Override
    public void onCreate() {
        User user = User.getUserFromPreferences(this);
        mAccessToken = user.getAccessToken();

        mClient = new NotiConnectClient(this);
        super.onCreate();
    }

    @Override
    protected void onServiceConnected() {
        Log.i(TAG, "onServiceConnected()");
        super.onServiceConnected();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Parcelable data = event.getParcelableData();
        if (data instanceof Notification) {
            String packageName = event.getPackageName().toString();
            onNotificationReceived((Notification) data, packageName);
        }
    }

    @Override
    public void onInterrupt() {
        Log.i(TAG, "onInterrupt()");
    }

    private void onNotificationReceived(Notification notification, String packageName) {
        Log.i(TAG, "onNotificationReceived()");
        // Post the notification to the sever
        mClient.postNotification(mAccessToken, notification, packageName);
    }
}
