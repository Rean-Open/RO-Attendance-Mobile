package com.attendance.qrcode;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.attendance.qrcode.service.response.TokenResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public final class AuthHelper {

    public static boolean auth(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Long expresIn =  prefs.getLong("auth.token.expresIn", -1);
        String accessToken =  prefs.getString("auth.token", null);
        if (accessToken != null && expresIn > 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(expresIn);
            return calendar.getTime().after(new Date());
        }
        return false;
    }

    public static boolean saveToken(Context context, TokenResponse token) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        try {
            Date exporesIn = dateFormat.parse(token.getExpiresIn());

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("auth.token", token.getAccessToken());
            editor.putLong("auth.token.expresIn", exporesIn.getTime());
            editor.putString("auth.token.type", token.getTokenType());
            editor.apply();
            return true;
        } catch (ParseException ex) {
            Log.e(AuthHelper.class.getSimpleName(), ex.getMessage(), ex);
        }
        return false;
    }

    public static String getAccessToken(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return  prefs.getString("auth.token", null);
    }

    public static boolean clearToken(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("auth.token");
        editor.remove("auth.token.expresIn");
        editor.remove("auth.token.type");
        editor.apply();
        return true;
    }
}
