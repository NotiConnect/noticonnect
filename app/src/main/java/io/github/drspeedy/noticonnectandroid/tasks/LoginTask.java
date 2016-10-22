package io.github.drspeedy.noticonnectandroid.tasks;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationResponse;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.AuthorizationServiceConfiguration;
import net.openid.appauth.TokenRequest;
import net.openid.appauth.TokenResponse;

import org.json.JSONException;

import java.util.LinkedHashMap;
import java.util.Map;

import io.github.drspeedy.noticonnectandroid.R;
import io.github.drspeedy.noticonnectandroid.models.User;

import static android.content.ContentValues.TAG;

/**
 * Created by doc on 10/21/16.
 */

public class LoginTask extends AsyncTask<Void, Void, Boolean> {

    public static final String AUTH_LOGIN_SUCCESS = "io.github.drspeedy.noticonnectandroid.AUTH_LOGIN_SUCCESS";
    public static final String AUTH_LOGIN_FAILED = "io.github.drspeedy.noticonnectandroid.AUTH_LOGIN_FAILED";
    public static final String AUTH_LOGIN_CANCELED = "io.github.drspeedy.noticonnectandroid.AUTH_LOGIN_CANCELED";

    private static final String AUTH_CLIENT_ID = "6";
    private static final String AUTH_CLIENT_SECRET = "3kheMkBVnhCNYP5EhFD9bUwkA2pMk97blarENM0M"; // wrong secret
    private static final String AUTH_RESPONSE_ACTION = "noticonnect:/auth/callback";
    private static final String AUTH_RESPONSE_SCHEME = "noticonnect";

    private Context mContext;
    private final String mAccountName;
    private final String mAccountPassword;

    private Boolean mResponseReceived;

    private User mUser;
    private AuthorizationService mAuthorizationService;

    public LoginTask(Context context, String accountName, String accountPassword) {
        mContext = context;
        mAccountName = accountName;
        mAccountPassword = accountPassword;

        mResponseReceived = false;
    }

    //public

    @Override
    protected Boolean doInBackground(Void... params) {
        sendTokenRequest(mAccountName, mAccountPassword, "*");
        waitForResponse();
        return isUserAuthenticated();
        //sendTokenRequest(mAccountName, mAccountPassword, "*");
        //return mUser;
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        String action;
        if (success) {
            action = AUTH_LOGIN_SUCCESS;
        }
        else {
            action = AUTH_LOGIN_FAILED;
        }
        Intent response = new Intent(action);
        mContext.sendBroadcast(response);
    }

    @Override
    protected void onCancelled() {
        Intent response = new Intent();
        response.setAction(AUTH_LOGIN_CANCELED);
        mContext.sendBroadcast(response);
    }

    private Boolean isUserAuthenticated() {
        Log.d(TAG, "Checking user has been authenticated...");
        return mUser != null;
    }

    private void waitForResponse(){
        Log.d(TAG, "Waiting for auth response...");
        try {
            while (!mResponseReceived) {
                Thread.sleep(100);
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void sendTokenRequest(String accountName, String accountPassword, String scope) {
        Log.d(TAG, "Sending token request...");
        AuthorizationServiceConfiguration serviceConfiguration = new AuthorizationServiceConfiguration(
                Uri.parse("http://172.16.1.36:8000/oauth/authorize"),
                Uri.parse("http://172.16.1.36:8000/oauth/token")
        );

        mAuthorizationService = new AuthorizationService(mContext);

        TokenRequest.Builder tokenRequestBuilder = new TokenRequest.Builder(serviceConfiguration, AUTH_CLIENT_ID);
        tokenRequestBuilder.setGrantType(TokenRequest.GRANT_TYPE_PASSWORD);
        tokenRequestBuilder.setScope(scope);

        Map<String, String> parameters = new LinkedHashMap<>();
        parameters.put("client_secret", AUTH_CLIENT_SECRET);
        parameters.put("username", accountName);
        parameters.put("password", accountPassword);
        //parameters.put("scope", scope);
        tokenRequestBuilder.setAdditionalParameters(parameters);
        TokenRequest request = tokenRequestBuilder.build();

        mAuthorizationService.performTokenRequest(request, new OnTokenResponse());
        Log.d(TAG, "Disposing Authorization Service");
    }

    private class OnTokenResponse implements AuthorizationService.TokenResponseCallback {
        @Override
        public void onTokenRequestCompleted(@Nullable TokenResponse tokenResponse, @Nullable AuthorizationException e) {
            mResponseReceived = true;
            Log.d(TAG, "Response received");
            if (e == null) {
                if (tokenResponse == null) Log.d(TAG, "Shits null breh!");
                Log.d(TAG, "Here's the tokenResponse:");
                Log.d(TAG, tokenResponse.accessToken);
                Log.d(TAG, tokenResponse.refreshToken);
                mUser = new User(mAccountName, tokenResponse);
            }
            else {

                //Log.d(TAG, e.error == null ? "wat" : "ok");
                Log.d(TAG, e.errorDescription);
            }
            mAuthorizationService.dispose();
        }
    }
}
