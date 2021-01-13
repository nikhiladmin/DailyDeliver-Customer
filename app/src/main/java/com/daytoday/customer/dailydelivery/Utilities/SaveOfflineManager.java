package com.daytoday.customer.dailydelivery.Utilities;

import android.content.Context;
import android.content.SharedPreferences;

public class SaveOfflineManager {
    private static SharedPreferences preferences;

    private static void initialise(Context context) {
        preferences = context.getSharedPreferences(AppConstants.APP_NAME, Context.MODE_PRIVATE);
    }

    public static String getUserName(Context context) {
        if (preferences == null) {
            initialise(context);
        }
        return preferences.getString(AppConstants.USER_NAME, "");
    }

    public static void setUserName(Context context,String userName) {
        if (preferences == null) {
            initialise(context);
        }
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(AppConstants.USER_NAME, userName);
        editor.commit();
    }

    public static String getUserId(Context context) {
        if (preferences == null) {
            initialise(context);
        }
        return preferences.getString(AppConstants.USER_ID, "");
    }
    public static void clearSharedPreference(Context context) {
        if (preferences == null) {
            initialise(context);
        }
         preferences.edit().clear().apply();
    }

    public static void setUserId(Context context,String userName) {
        if (preferences == null) {
            initialise(context);
        }
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(AppConstants.USER_ID, userName);
        editor.commit();
    }

    public static String getUserAddress(Context context) {
        if (preferences == null) {
            initialise(context);
        }
        return preferences.getString(AppConstants.USER_ADRESS, "");
    }

    public static void setUserAddress(Context context,String userName) {
        if (preferences == null) {
            initialise(context);
        }
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(AppConstants.USER_ADRESS, userName);
        editor.commit();
    }

    public static String getUserPhoneNumber(Context context) {
        if (preferences == null) {
            initialise(context);
        }
        return preferences.getString(AppConstants.USER_PHONE_NUMBER, "");
    }

    public static void setUserPhoneNumber(Context context,String userName) {
        if (preferences == null) {
            initialise(context);
        }
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(AppConstants.USER_PHONE_NUMBER, userName);
        editor.commit();
    }
}
