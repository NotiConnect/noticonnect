package io.github.drspeedy.httpclient;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import io.github.drspeedy.httpclient.models.HttpResponse;

/**
 * Created by doc on 11/12/16.
 */

public class HttpClient {

    public static final String HTTP_POST = "POST";
    public static final String HTTP_GET = "GET";
    // TODO: Do this stuff below here...
    public static final String HTTP_PUT = "PUT";
    public static final String HTTP_DELETE = "DELETE";

    private URL mURL;

    public URL getURL() {
        return mURL;
    }

    public void setURL(URL URL) {
        mURL = URL;
    }

    public void httpRequest(@NonNull String requestType, @Nullable Map<String, String> requestProperties,
                            @Nullable byte[] requestBody, @Nullable HttpResponse.Callback callback) {
        new HttpRequestTask(requestType, requestProperties, requestBody, callback).execute();
    }

    private class HttpRequestTask extends AsyncTask<Void, Void, HttpResponse> {

        // TODO: Follow redirects? Add http timeout params. PUT DELETE
        private String mRequestType;
        private Map<String, String> mRequestProperties;
        private byte[] mRequestBody;
        private HttpResponse.Callback mCallback;

        /**
         *
         * @param requestType
         * @param requestProperties
         * @param requestBody
         * @param onHttpResponse
         */
        public HttpRequestTask(@NonNull String requestType, @Nullable Map<String, String> requestProperties,
                               @Nullable byte[] requestBody, @Nullable HttpResponse.Callback onHttpResponse) {
            mRequestType = requestType;
            mRequestProperties = new HashMap<>(requestProperties);
            mRequestBody = requestBody;
            mCallback = onHttpResponse;
        }

        @Override
        protected HttpResponse doInBackground(Void... params) {
            try {
                switch (mRequestType) {
                    case "POST":
                        return sendPostRequest();

                    case "GET":
                        return sendGetRequest();

                    default:
                        return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(HttpResponse httpResponse) {
            mCallback.onHttpResponse(httpResponse);
        }

        private HttpResponse sendPostRequest() throws IOException {
            HttpURLConnection connection = (HttpURLConnection) getURL().openConnection();

            connection.setInstanceFollowRedirects(false);
            connection.setUseCaches(true);

            connection.setDoOutput(true);
            connection.setRequestMethod(HTTP_POST);

            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.write(mRequestBody);
            out.flush();
            out.close();

            HttpResponse response = new HttpResponse(connection);
            connection.disconnect();

            return response;
        }

        private HttpResponse sendGetRequest() throws IOException {
            HttpURLConnection connection = (HttpURLConnection) getURL().openConnection();

            connection.setInstanceFollowRedirects(false);
            connection.setUseCaches(true);

            connection.setDoInput(true);
            connection.setRequestMethod(HTTP_GET);

            HttpResponse response = new HttpResponse(connection);
            connection.disconnect();

            return response;
        }
    }
}
