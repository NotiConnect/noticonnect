package io.github.drspeedy.noticonnectandroid.api;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationResponse;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.AuthorizationServiceConfiguration;
import net.openid.appauth.TokenRequest;
import net.openid.appauth.TokenResponse;

import java.util.LinkedHashMap;
import java.util.Map;

import static io.github.drspeedy.noticonnectandroid.api.NotiConnectCommon.*;

/**
 * Created by doc on 10/18/16.
 */

public class NotiConnectAuthenticator {

    private static final String TAG = NotiConnectAuthenticator.class.getSimpleName();

    private Context mContext;


    public NotiConnectAuthenticator(Context context) {
        mContext = context;
    }

    protected void handleAuthorizationResponse(@NonNull Intent intent) {
        AuthorizationResponse response = AuthorizationResponse.fromIntent(intent);
        AuthorizationException error = AuthorizationException.fromIntent(intent);
        final AuthState authState = new AuthState(response, error);

        if (response != null) {
            Log.i(TAG, String.format("Handled Auth-Response: %s", authState.toJsonString()));
            AuthorizationService service = new AuthorizationService(mContext);
            AuthorizationServiceConfiguration configuration = response.request.configuration;
            final String clientId = response.request.clientId;
            TokenRequest.Builder tokenRequestBuilder = new TokenRequest.Builder(configuration, clientId);
            tokenRequestBuilder.setAuthorizationCode(response.authorizationCode);
            tokenRequestBuilder.setGrantType(TokenRequest.GRANT_TYPE_AUTHORIZATION_CODE);
            tokenRequestBuilder.setRedirectUri(response.request.redirectUri);

            Map<String, String> additionalParameters = new LinkedHashMap<>();
            additionalParameters.put("client_secret", "FG5hKxceERDVljtYCAGgrK0EXFMnvyVRoGwRlJTg");
            tokenRequestBuilder.setAdditionalParameters(additionalParameters);

            service.performTokenRequest(response.createTokenExchangeRequest(), new AuthorizationService.TokenResponseCallback() {

                @Override
                public void onTokenRequestCompleted(@Nullable TokenResponse tokenResponse, @Nullable AuthorizationException e) {
                    if (e != null) {
                        Log.w(TAG, "Token exchange failed!", e);
                    }
                    else {
                        if (tokenResponse != null) {
                            authState.update(tokenResponse, e);
                            //persistAuthState(authState);
                            Log.i(TAG, String.format("Token Response [ Access Token: %s, ID Token: %s ]", tokenResponse.accessToken));
                        }
                    }
                }
            });
        }

    }

















    public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String authTokenType, String[] requiredFeatures, Bundle options) throws NetworkErrorException {
        Log.i(TAG, "addAccount()");



        return null;
    }

    public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account, Bundle options) throws NetworkErrorException {
        return null;
    }

    public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        return null;
    }

    public String getAuthTokenLabel(String authTokenType) {
        return null;
    }

    public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        return null;
    }

    public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account, String[] features) throws NetworkErrorException {
        return null;
    }
}
