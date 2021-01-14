package com.daytoday.customer.dailydelivery.Utilities;

import android.content.Context;

import com.daytoday.customer.dailydelivery.BuildConfig;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.FirebaseDatabase;

public class RealtimeDatabase {

    private static final String BUSINESS_DATABASE = "BusinessDatabase";
    public static FirebaseApp bussApp;

    public static FirebaseDatabase getInstance() {
        FirebaseDatabase secondDatabase = FirebaseDatabase.getInstance(bussApp);
        return secondDatabase;
    }

    public static void initialiseApp(Context context){
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setApiKey(BuildConfig.Firebase_Server_Key)
                .setApplicationId(BuildConfig.Firebase_Application_ID)
                .setDatabaseUrl(BuildConfig.Firebase_Buss_Databse_Url)
                .build();
         bussApp = FirebaseApp.initializeApp(context, options, BUSINESS_DATABASE);
    }
}
