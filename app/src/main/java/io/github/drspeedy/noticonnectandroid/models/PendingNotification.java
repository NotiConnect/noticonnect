package io.github.drspeedy.noticonnectandroid.models;

import android.app.Notification;

import org.json.JSONObject;

/**
 * Created by doc on 11/15/16.
 */

public class PendingNotification {
    private User mUser;
    private Notification mNotification;

    public Notification getNotification() {
        return mNotification;
    }

    public void setNotification(Notification notification) {
        mNotification = notification;
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }

    public PendingNotification(User user, Notification notification) {
        mUser = user;
        mNotification = notification;
    }

    public JSONObject toJSON() {

        return null;
    }
}
