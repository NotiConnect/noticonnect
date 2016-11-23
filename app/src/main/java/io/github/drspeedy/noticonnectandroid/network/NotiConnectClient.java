package io.github.drspeedy.noticonnectandroid.network;

import android.content.Context;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import io.github.drspeedy.httpclient.HttpClient;
import io.github.drspeedy.httpclient.models.HttpResponse;
import io.github.drspeedy.noticonnectandroid.models.PendingNotification;
import io.github.drspeedy.noticonnectandroid.models.User;

/**
 * Created by doc on 11/23/16.
 */

public class NotiConnectClient extends HttpClient {

    private static final String TAG = NotiConnectClient.class.getSimpleName();

    private User mUser;
    private Context mContext;

    public NotiConnectClient(Context context) {
        mUser = User.getUserFromPreferences(context);
        mContext = context;
    }

    public void postNotification(PendingNotification pendingNotification) {
        Map<String, String> body = new HashMap<>();
        body.put("package_name", pendingNotification.getPackageName());
        body.put("title", pendingNotification.getTitle());
        body.put("text", pendingNotification.getText());
        body.put("sub_text", pendingNotification.getSubText());
        body.put("group", pendingNotification.getGroup());
        body.put("icon_base64", pendingNotification.getIconBase64());

        byte[] bodyUrlEncoded = null;
        try {
            bodyUrlEncoded = mapToUrlEncoded(body);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/x-www-url-encoded");
        header.put("charset", "utf-8");
        header.put("Authorization", "Bearer " + mUser.getAccessToken());

        httpRequest(HTTP_POST, header, bodyUrlEncoded, new HttpResponse.Callback() {
            @Override
            public void onHttpResponse(HttpResponse response) {
                Log.d(TAG, "postNotification() => " + response.getResponseString() + " " + response.getResponseCode());
            }
        });
    }
}
