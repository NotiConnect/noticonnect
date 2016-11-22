package io.github.drspeedy.noticonnectandroid.repositories.notification;

import android.app.Notification;
import android.net.UrlQuerySanitizer;
import android.os.AsyncTask;

import org.json.JSONObject;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import io.github.drspeedy.httpclient.models.HttpResponse;

/**
 * Created by doc on 11/14/16.
 */

public class NetworkNotificationRepository implements NotificationRepository {

    private Map<Integer, Notification> mNotificationMap;
    //private Queue<> mSyncNotificationsQueue;

    @Override
    public Notification get(Integer id) {
        return null;
    }

    @Override
    public Map<Integer, Notification> getAll() {
        return null;
    }

    @Override
    public void put(Integer id, Notification notification) {

    }

    @Override
    public void putAll(Collection<Notification> notifications) {

    }

    @Override
    public void delete(Integer id) {

    }

    @Override
    public int size() {
        return 0;
    }

    private class SyncNotificationsTask extends AsyncTask<Void, Void, HttpResponse> {

        @Override
        protected HttpResponse doInBackground(Void... params) {
            return null;
        }
    }
}
