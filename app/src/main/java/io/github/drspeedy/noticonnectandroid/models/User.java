package io.github.drspeedy.noticonnectandroid.models;

import android.content.Context;
import android.content.SharedPreferences;

import net.openid.appauth.TokenResponse;

import io.github.drspeedy.noticonnectandroid.R;

/**
 * Created by doc on 10/22/16.
 */

public class User {

    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String PREF_ACCESS_TOKEN = "accessToken";
    private static final String PREF_REFRESH_TOKEN = "refreshToken";
    private static final String PREF_TOKEN_EXPIRATION = "tokenExpiration";

    private final String mAccountName;
    private final String mAccessToken;
    private final String mRefreshToken;
    private final long mTokenExpiration;

    public User(String accountName, String accessToken, String refreshToken, long tokenExpiration) {
        mAccountName = accountName;
        mAccessToken = accessToken;
        mRefreshToken = refreshToken;
        mTokenExpiration = tokenExpiration;
    }

    public User(String accountName, TokenResponse tokenResponse) {
        mAccountName = accountName;
        mAccessToken = tokenResponse.accessToken;
        mRefreshToken = tokenResponse.refreshToken;
        mTokenExpiration = tokenResponse.accessTokenExpirationTime;
    }

    public String getAccountName() {
        return mAccountName;
    }

    public String getAccessToken() {
        return mAccessToken;
    }

    public String getRefreshToken() {
        return mRefreshToken;
    }

    public long getTokenExpiration() {
        return mTokenExpiration;
    }

    /**
     * Save the state of the model to the application's shared preference file
     * @param context
     */
    public void persist(Context context) {
        String preferenceFileKey = context.getString(R.string.preference_file_key);
        SharedPreferences preferences = context.getSharedPreferences(preferenceFileKey, Context.MODE_PRIVATE);
        preferences.edit()
                .putString(PREF_ACCOUNT_NAME, mAccountName)
                .putString(PREF_ACCESS_TOKEN, mAccessToken)
                .putString(PREF_REFRESH_TOKEN, mRefreshToken)
                .putLong(PREF_TOKEN_EXPIRATION, mTokenExpiration)
                .apply();
    }

    /**
     * Construct a new User model instance from the application's
     * shared preference file
     * @param context
     * @return User
     */
    public static User getUserFromPreferences(Context context) {
        String preferenceFileKey = context.getString(R.string.preference_file_key);
        SharedPreferences preferences = context.getSharedPreferences(preferenceFileKey, Context.MODE_PRIVATE);
        String accountName = preferences.getString(PREF_ACCOUNT_NAME, null);
        String accessToken = preferences.getString(PREF_ACCESS_TOKEN, null);
        String refreshToken = preferences.getString(PREF_REFRESH_TOKEN, null);
        long tokenExpiration = preferences.getLong(PREF_TOKEN_EXPIRATION, -1);

        return new User(accountName, accessToken, refreshToken, tokenExpiration);
    }
}
