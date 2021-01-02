package com.daytoday.customer.dailydelivery.searchui;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SearchViewModel extends AndroidViewModel {
    MutableLiveData<SearchDataSource> dataSourceMutableLiveData;
    MutableLiveData<String> filterTextAll = new MutableLiveData<>();
    SearchDataFactory searchDataFactory;
    Executor executor;
    LiveData<PagedList<Business>> pagedListLiveData;

    public SearchViewModel(@NonNull Application application, String key, String custid) {
        super(application);
        searchDataFactory = new SearchDataFactory(key, custid);
        dataSourceMutableLiveData = searchDataFactory.getLiveData();
        PagedList.Config config = (new PagedList.Config.Builder())
                .setEnablePlaceholders(true)
                .setInitialLoadSizeHint(10)
                .setPageSize(10)
                .setPrefetchDistance(4)
                .build();
        executor = Executors.newFixedThreadPool(5);
        pagedListLiveData = Transformations.switchMap(filterTextAll, input -> {
            if (input == null || input.equals("")) {
                return (new LivePagedListBuilder<Integer, Business>(searchDataFactory, config))
                        .setFetchExecutor(executor)
                        .build();
            } else {
                return (new LivePagedListBuilder<Integer, Business>(new SearchDataFactory(input, custid), config))
                        .setFetchExecutor(executor)
                        .build();
            }
        });
//        pagedListLiveData = (new LivePagedListBuilder<Integer, Business>(searchDataFactory, config))
//                .setFetchExecutor(executor)
//                .build();
    }

    public LiveData<PagedList<Business>> getPagedListLiveData() {
        return pagedListLiveData;
    }
}
