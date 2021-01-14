package com.daytoday.customer.dailydelivery.NotificationUI;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.paging.PageKeyedDataSource;

import com.daytoday.customer.dailydelivery.Network.ApiInterface;
import com.daytoday.customer.dailydelivery.Network.Client;
import com.daytoday.customer.dailydelivery.searchui.Business;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationDataSource extends PageKeyedDataSource<Long, Notification> {
    ApiInterface apiInterface;
    Map<String, String> map = new HashMap<>();
    int page = 0;

    public NotificationDataSource(String userid) {
        this.map.put("page", "0");
        this.map.put("userid", userid);
        apiInterface = Client.getClient().create(ApiInterface.class);
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Long> params, @NonNull LoadInitialCallback<Long, Notification> callback) {
        this.map.put("page", String.valueOf(++page));
        Call<NotificationModelResponse> call = apiInterface.getNotifications(map);
        call.enqueue(new Callback<NotificationModelResponse>() {
            @Override
            public void onResponse(Call<NotificationModelResponse> call, Response<NotificationModelResponse> response) {
                List<Notification> notificationList = response.body().getNotifications();
                callback.onResult(notificationList, null, (long) page + 1);
            }

            @Override
            public void onFailure(Call<NotificationModelResponse> call, Throwable t) {
                Log.e("NotificationDataSource", "" + t);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void loadBefore(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Long, Notification> callback) {
        map.replace("page", String.valueOf(++page));
        Call<NotificationModelResponse> call = apiInterface.getNotifications(map);
        call.enqueue(new Callback<NotificationModelResponse>() {
            @Override
            public void onResponse(Call<NotificationModelResponse> call, Response<NotificationModelResponse> response) {
                Long key = (params.key > 1) ? params.key - 1 : null;
                List<Notification> model = response.body().getNotifications();
                callback.onResult(model, key);
            }

            @Override
            public void onFailure(Call<NotificationModelResponse> call, Throwable t) {
                Log.e("NotificationDataSource", "" + t);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void loadAfter(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Long, Notification> callback) {
        map.replace("page", String.valueOf(++page));
        Call<NotificationModelResponse> call = apiInterface.getNotifications(map);
        call.enqueue(new Callback<NotificationModelResponse>() {
            @Override
            public void onResponse(Call<NotificationModelResponse> call, Response<NotificationModelResponse> response) {
                List<Notification> model = response.body().getNotifications();
                callback.onResult(model, (long) page + 1);
            }

            @Override
            public void onFailure(Call<NotificationModelResponse> call, Throwable t) {
                Log.e("NotificationDataSource", "" + t);
            }
        });
    }
}
