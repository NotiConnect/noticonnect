package io.github.drspeedy.noticonnectandroid.repositories.notification;

import android.app.Notification;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

/**
 * Created by doc on 11/14/16.
 */

public interface NotificationRepository extends Serializable {
    Notification get(Integer id);
    Map<Integer, Notification> getAll();

    void put(Integer id, Notification notification);
    void putAll(Collection<Notification> notifications);

    void delete(Integer id);

    int size();
}
