package com.example.examportal.retrofit;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private static final String PREF_NAME = "JWT_SESSION";
    private static final String KEY_ACCESS_TOKEN = "access_token";

    private static final String USER = "user";


    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }


    public void saveUser(String user) {


        editor.putString(USER, user).apply();


    }

    public String getUser() {
        return sharedPreferences.getString(USER, null);
    }

    public void deleteUser() {
        editor.remove(USER);
    }


    public void saveAccessToken(String accessToken) {
        editor.putString(KEY_ACCESS_TOKEN, accessToken).apply();

    }

    public String getAccessToken() {
        return sharedPreferences.getString(KEY_ACCESS_TOKEN, null);
    }

    public void clearSession() {
        editor.clear().apply();
    }
}
