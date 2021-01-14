package com.daytoday.customer.dailydelivery.NotificationUI;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class NotificationViewModelFactory implements ViewModelProvider.Factory {
    private Application mApplication;
    private String userid;

    public NotificationViewModelFactory(Application application, String userid) {
        this.mApplication = application;
        this.userid = userid;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new NotificationViewModel(mApplication, userid);
    }
}
