package io.github.drspeedy.noticonnectandroid.tasks;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;
import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.AuthorizationServiceConfiguration;
import net.openid.appauth.TokenRequest;
import net.openid.appauth.TokenResponse;
import java.util.LinkedHashMap;
import java.util.Map;

import io.github.drspeedy.noticonnectandroid.Application;
import io.github.drspeedy.noticonnectandroid.models.User;

/**
 * Created by doc on 10/21/16.
 */

public class LoginTask extends AsyncTask<Void, Void, Boolean> {

    // Log tag
    private static final String TAG = LoginTask.class.getSimpleName();

    // Login response status constants
    public static final String AUTH_LOGIN_SUCCESS = "io.github.drspeedy.noticonnectandroid.AUTH_LOGIN_SUCCESS";
    public static final String AUTH_LOGIN_FAILED = "io.github.drspeedy.noticonnectandroid.AUTH_LOGIN_FAILED";
    public static final String AUTH_LOGIN_CANCELED = "io.github.drspeedy.noticonnectandroid.AUTH_LOGIN_CANCELED";

    // Password grant client id and secret
    private static final String AUTH_CLIENT_ID = "6";
    private static final String AUTH_CLIENT_SECRET = "3kheMkBVnhCNYP5EhFD9bUwkA2pMk97blarENM0M";

    // OAuth server authorize and token endpoints
    public static final String AUTH_AUTHORIZE_ENDPOINT = Application.BASE_HOST + "/oauth/authorize";
    public static final String AUTH_TOKEN_ENDPOINT = Application.BASE_HOST + "/oauth/token";

    // Member variables
    private Context mContext;                               // Context of the activity that executed this task
    private AuthorizationService mAuthorizationService;     // Authorization service singleton to handle the bulk of network logic
    private final String mAccountName;                      // User's email address (Could also be a username, however our server uses email)
    private final String mAccountPassword;                  // User's password
    private Boolean mResponseReceived;                      // Set true when OnTokenResponse is called
    private User mUser;                                     // Object to hold authenticated user

    /**
     * Construct the LoginTask object
     * @param context Context of the activity that executed this task
     * @param accountName User's email address
     * @param accountPassword User's password
     */
    public LoginTask(Context context, String accountName, String accountPassword) {
        mContext = context;
        mAccountName = accountName;
        mAccountPassword = accountPassword;

        mResponseReceived = false;
    }

    /**
     * Do authentication work in on a separate thread than the UI
     * @param params
     * @return success
     */
    @Override
    protected Boolean doInBackground(Void... params) {
        // Send a token request for all authorization scopes
        sendTokenRequest(mAccountName, mAccountPassword, "*");
        waitForResponse();
        return isUserAuthenticated();
    }

    /**
     * Broadcast whether or not if the login was successful back
     * to the activity that executed the task
     * @param success
     */
    @Override
    protected void onPostExecute(final Boolean success) {
        mAuthorizationService.dispose();
        String action;
        if (success) {
            action = AUTH_LOGIN_SUCCESS;

            // Save the user data to shared preferences
            mUser.persist(mContext);
        }
        else {
            action = AUTH_LOGIN_FAILED;
        }
        Intent response = new Intent(action);
        mContext.sendBroadcast(response);
    }

    /**
     * Broadcast a message to the activity that executed the
     * task if for some reason the task was canceled
     */
    @Override
    protected void onCancelled() {
        mAuthorizationService.dispose();
        Intent response = new Intent();
        response.setAction(AUTH_LOGIN_CANCELED);
        mContext.sendBroadcast(response);
    }

    /**
     * Check to see whether the user has been authenticated
     * @return Boolean
     */
    private Boolean isUserAuthenticated() {
        Log.d(TAG, "Checking user has been authenticated...");
        return mUser != null;
    }

    /**
     * Wait for the OnTokenResponse callback to be executed
     */
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

    /**
     * Request an access token from the oauth server using the password grant type
     * @param accountName User's email address
     * @param accountPassword User's password
     * @param scope User's API permissions
     */
    private void sendTokenRequest(String accountName, String accountPassword, String scope) {
        Log.d(TAG, "Sending token request...");
        AuthorizationServiceConfiguration serviceConfiguration = new AuthorizationServiceConfiguration(
                Uri.parse(AUTH_AUTHORIZE_ENDPOINT),
                Uri.parse(AUTH_TOKEN_ENDPOINT)
        );

        mAuthorizationService = new AuthorizationService(mContext);

        TokenRequest.Builder tokenRequestBuilder = new TokenRequest.Builder(serviceConfiguration, AUTH_CLIENT_ID);
        tokenRequestBuilder.setGrantType(TokenRequest.GRANT_TYPE_PASSWORD);
        tokenRequestBuilder.setScope(scope);

        Map<String, String> parameters = new LinkedHashMap<>();
        parameters.put("client_secret", AUTH_CLIENT_SECRET);
        parameters.put("username", accountName);
        parameters.put("password", accountPassword);
        tokenRequestBuilder.setAdditionalParameters(parameters);

        TokenRequest request = tokenRequestBuilder.build();
        mAuthorizationService.performTokenRequest(request, new OnTokenResponse());
    }

    /**
     * When a response is received, if successful then create a new user object
     * to hold the now authenticated user. If not then we'll just log out the error description
     */
    private class OnTokenResponse implements AuthorizationService.TokenResponseCallback {
        @Override
        public void onTokenRequestCompleted(@Nullable TokenResponse tokenResponse, @Nullable AuthorizationException e) {
            mResponseReceived = true;
            Log.d(TAG, "Response received");
            if (e == null) {
                Log.d(TAG, "Here's the tokenResponse:");
                Log.d(TAG, tokenResponse.accessToken);
                Log.d(TAG, tokenResponse.refreshToken);
                mUser = new User(mAccountName, tokenResponse);
            }
            else {
                Log.d(TAG, e.errorDescription);
            }
        }
    }
}
