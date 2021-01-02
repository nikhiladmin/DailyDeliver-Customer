package com.daytoday.customer.dailydelivery.searchui;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.paging.PageKeyedDataSource;

import com.daytoday.customer.dailydelivery.Network.ApiInterface;
import com.daytoday.customer.dailydelivery.Network.Client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchDataSource extends PageKeyedDataSource<Long,Business> {
    ApiInterface apiInterface;
    Map<String,String> map = new HashMap<>();
    int page=0;
    public SearchDataSource(String key,String custid)
    {
        this.map.put("page","0");
        this.map.put("custid",custid);
        if(key!=null)
        {
            this.map.put("key",key);
        }

    }
    @Override
    public void loadInitial(@NonNull LoadInitialParams<Long> params, @NonNull LoadInitialCallback<Long, Business> callback) {
        this.map.put("page",String.valueOf(++page));
        apiInterface = Client.getClient().create(ApiInterface.class);
        Call<SearchResponseModel> call = apiInterface.getSearch(map);
        call.enqueue(new Callback<SearchResponseModel>() {
            @Override
            public void onResponse(Call<SearchResponseModel> call, Response<SearchResponseModel> response) {
                List<Business> model = response.body().getBusinesses();
                callback.onResult(model,null,(long)page+1);
            }

            @Override
            public void onFailure(Call<SearchResponseModel> call, Throwable t) {
                Log.e("SearchDataSource",""+t);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void loadBefore(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Long, Business> callback) {
        map.replace("page",String.valueOf(++page));
        apiInterface = Client.getClient().create(ApiInterface.class);
        Call<SearchResponseModel> call = apiInterface.getSearch(map);
        call.enqueue(new Callback<SearchResponseModel>() {
            @Override
            public void onResponse(Call<SearchResponseModel> call, Response<SearchResponseModel> response) {
                Long key=(params.key > 1) ? params.key-1 : null;
                List<Business> model = response.body().getBusinesses();
                callback.onResult(model,key);
            }

            @Override
            public void onFailure(Call<SearchResponseModel> call, Throwable t) {
                Log.e("SearchDataSource",""+t);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void loadAfter(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Long, Business> callback) {
        map.replace("page",String.valueOf(++page));
        apiInterface = Client.getClient().create(ApiInterface.class);
        Call<SearchResponseModel> call = apiInterface.getSearch(map);
        call.enqueue(new Callback<SearchResponseModel>() {
            @Override
            public void onResponse(Call<SearchResponseModel> call, Response<SearchResponseModel> response) {
                List<Business> model = response.body().getBusinesses();
                callback.onResult(model,(long)page+1);
            }

            @Override
            public void onFailure(Call<SearchResponseModel> call, Throwable t) {
                Log.e("SearchDataSource",""+t);
            }
        });
    }
}
