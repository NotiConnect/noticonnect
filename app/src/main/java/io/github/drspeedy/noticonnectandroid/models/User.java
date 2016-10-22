package io.github.drspeedy.noticonnectandroid.models;

import net.openid.appauth.TokenResponse;

/**
 * Created by doc on 10/22/16.
 */

public class User {
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
}
