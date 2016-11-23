package io.github.drspeedy.noticonnectandroid.services;

import android.accessibilityservice.AccessibilityService;
import android.app.Notification;
import android.os.Parcelable;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import io.github.drspeedy.noticonnectandroid.models.PendingNotification;
import io.github.drspeedy.noticonnectandroid.network.NotiConnectClient;
import io.github.drspeedy.noticonnectandroid.models.User;


/**
 * Created by doc on 11/6/16.
 */

public class NotiConnectService extends AccessibilityService {

    private static final String TAG = NotiConnectService.class.getSimpleName();

    private String mAccessToken;

    @Override
    public void onCreate() {
        User user = User.getUserFromPreferences(this);
        mAccessToken = user.getAccessToken();
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

        NotiConnectClient client = new NotiConnectClient(this);
        PendingNotification pending = new PendingNotification(this, notification, packageName);
        client.postNotification(pending);
    }
}
