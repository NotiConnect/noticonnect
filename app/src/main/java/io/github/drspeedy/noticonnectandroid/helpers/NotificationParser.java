package io.github.drspeedy.noticonnectandroid.helpers;

import android.app.Notification;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Created by doc on 10/8/16.
 */

public class NotificationParser {

    // Constants
    public static final String NOTIFICATION_TITLE = "android.title";
    public static final String NOTIFICATION_TEXT = "android.text";
    public static final String NOTIFICATION_SUB_TEXT = "android.subText";

    // Member variables
    private Context mContext;
    private final Bundle mNotificationBundle;
    private final String mPackageName;
    private final String mGroup;
    private final Icon mSmallIcon;
    private final Icon mLargeIcon;

    /**
     * Helper class to make retrieving relevant data from Notification
     * instances easier for interacting with client services
     * @param context
     * @param notification
     * @param packageName
     */
    public NotificationParser(Context context, Notification notification, String packageName) {
        mContext = context;
        mNotificationBundle = notification.extras;
        mPackageName = packageName;
        mSmallIcon = notification.getSmallIcon();
        mLargeIcon = notification.getLargeIcon();
        mGroup = notification.getGroup();
    }

    /**
     * Get the sender's package name
     * @return String
     */
    public final String getPackageName() {
        return mPackageName;
    }

    /**
     * Get the notification's title
     * @return String
     */
    public final String getTitle() {
        return mNotificationBundle.getString(NOTIFICATION_TITLE);
    }

    /**
     * Get the notification's text
     * @return String
     */
    public final String getText() {
        return mNotificationBundle.getString(NOTIFICATION_TEXT);
    }

    /**
     * Get the notification's sub text
     * @return String
     */
    public final String getSubText() {
        return mNotificationBundle.getString(NOTIFICATION_SUB_TEXT);
    }

    /**
     * Get the notification's group
     * @return String
     */
    public final String getGroup() {
        return mGroup;
    }

    /**
     * Get the large icon if one exists, otherwise use the small one
     * @return Icon
     */
    public final Icon getIcon() {
        return mLargeIcon != null ? mLargeIcon : mSmallIcon;
    }

    /**
     * Get the notification's icon and encode it
     * into a Base64 string with DEFAULT flags
     * @return String
     */
    public final String getIconBase64() {
        return getIconBase64(Base64.NO_WRAP);
    }

    /**
     * Get the notification's icon and encode it
     * into a Base64 string
     * @param flags
     * @return String
     */
    public final String getIconBase64(int flags) {
        byte[] icon = getIconByteArray();
        return Base64.encodeToString(icon, flags);
    }

    /**
     * Get the notification's icon as a byte array
     * @return byte[]
     */
    public final byte[] getIconByteArray() {
        Drawable drawable = getIcon().loadDrawable(mContext);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        return outputStream.toByteArray();
    }
}


