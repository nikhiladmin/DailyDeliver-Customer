package com.daytoday.customer.dailydelivery.Network;

import android.util.Log;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Client {
    private static Retrofit retrofit = null;
    private static Retrofit firebaseRetrofit = null;
    private static Retrofit geoCodingRetrofit = null;

    private static Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(ApiInterface.API_BASE_URL)
                    .client(getHeader())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getFirebaseClient() {
        if (firebaseRetrofit == null) {
            firebaseRetrofit = new Retrofit.Builder()
                    .baseUrl(ApiInterface.FCM_BASE_URL)
                    .client(getHeader())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return firebaseRetrofit;
    }

    private static OkHttpClient getHeader() {
        return new OkHttpClient
                .Builder()
                .addInterceptor(getInterceptor())
                .build();
    }
    public static Retrofit getGeocodingClient(){
        if(geoCodingRetrofit==null){
            geoCodingRetrofit = new Retrofit.Builder()
                    .baseUrl("https://nominatim.openstreetmap.org/")
                    .client(getHeader())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return geoCodingRetrofit;
    }
    private static Interceptor getInterceptor() {
        return new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                FirebaseCrashlytics.getInstance().log(message);
                Log.i("NETWORK LOG -> ",message);
            }
        }).setLevel(HttpLoggingInterceptor.Level.BODY);
    }
}
