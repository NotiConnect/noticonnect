package io.github.drspeedy.noticonnectandroid.tasks.network;

import android.app.Notification;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import io.github.drspeedy.noticonnectandroid.Application;
import io.github.drspeedy.noticonnectandroid.helpers.NotificationParser;
import io.github.drspeedy.noticonnectandroid.models.HttpResponse;

/**
 * Created by doc on 11/6/16.
 */

public class NotiConnectClient {

    private static final String TAG = NotiConnectClient.class.getSimpleName();

    private Context mContext;

    public NotiConnectClient(Context context) {
        mContext = context;
    }

    public void postNotification(@NonNull String accessToken, Notification notification, String packageName) {
        NotificationParser np = new NotificationParser(mContext, notification, packageName);
        Map<String, String> params = new HashMap<>();
        params.put("package_name", np.getPackageName());
        params.put("title", np.getTitle());
        params.put("text", np.getText());
        params.put("sub_text", np.getSubText());
        params.put("group", np.getGroup());
        params.put("icon_base64", np.getIconBase64());


        new ApiPostCall("/notify", accessToken, params, new HttpResponse.Callback() {
            @Override
            public void onApiResponse(HttpResponse response) {
                Log.i(TAG, "Response status: " + response.getResponseCode());
                Log.i(TAG, "Response message: " + response.getResponseString());
            }
        }).execute();

    }

    private class ApiPostCall extends AsyncTask<Void, Void, HttpResponse> {

        private String mRequest;
        private String mAccessToken;
        private byte[] mPostData;
        private HttpResponse.Callback mResponseCallback;

        /**
         * Make HTTP POST request to the API host
         * @param target API target
         * @param accessToken User's authorization access token
         * @param postDataMap Map of form keys and values to post to the server
         * @param callback Callback that is called when a response is received
         */
        ApiPostCall(@NonNull String target, @NonNull String accessToken, @NonNull Map<String, String> postDataMap,
                    @Nullable HttpResponse.Callback callback) {
            mRequest = Application.API_HOST + target;

            mAccessToken = accessToken;
            try {
                mPostData = mapToPostData(postDataMap);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            mResponseCallback = callback;
        }

        @Override
        protected HttpResponse doInBackground(Void... params) {

            try {
                URL url = new URL(mRequest);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setDoOutput(true);
                connection.setDoOutput(true);
                // TODO: Add support for redirected call handling
                connection.setInstanceFollowRedirects(false);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setRequestProperty("charset", "UTF-8");
                connection.setRequestProperty("Authorization", "Bearer " + mAccessToken);
                connection.setUseCaches(true);

                // Write post data to connection stream
                DataOutputStream writer = new DataOutputStream(connection.getOutputStream());
                writer.write(mPostData);
                writer.flush();
                writer.close();

                // Construct a response object from the connection
                HttpResponse response = new HttpResponse(connection);
                connection.disconnect();
                Log.d(TAG, "Disconnecting...");

                return response;

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(HttpResponse response) {
            if (response != null && mResponseCallback != null) {
                mResponseCallback.onApiResponse(response);
            }
        }

        /**
         * Transform a map of HTTP POST data into a byte array of data
         * @param map form-key => form-value map
         * @return Byte array
         * @throws UnsupportedEncodingException
         */
        private byte[] mapToPostData(Map<String, String> map) throws UnsupportedEncodingException {
            String params = "";

            for (Map.Entry<String, String> entry : map.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                params += key + "=" + value + "&";
            }

            Log.d(TAG, "mapToPostData() => " + params);
            String urlSafeParams = URLEncoder.encode(params, "utf-8");
            return params.getBytes();
        }
    }
}
