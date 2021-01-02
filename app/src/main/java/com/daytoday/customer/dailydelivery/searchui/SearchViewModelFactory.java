package com.daytoday.customer.dailydelivery.searchui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class SearchViewModelFactory implements ViewModelProvider.Factory {
    private Application mApplication;
    private String key;
    private String custid;


    public SearchViewModelFactory(Application application,String key,String custid) {
        mApplication = application;
        this.key=key;
        this.custid = custid;
    }
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new SearchViewModel(mApplication,key,custid);
    }
}
