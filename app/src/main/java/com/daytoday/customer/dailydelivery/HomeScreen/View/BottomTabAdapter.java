package com.daytoday.customer.dailydelivery.HomeScreen.View;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class BottomTabAdapter extends FragmentPagerAdapter {
    Context context;
    int totaltabs;

    public BottomTabAdapter(@NonNull FragmentManager fm, Context context, int totaltabs) {
        super(fm);
        this.context = context;
        this.totaltabs = totaltabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0 :
                Log.i("msg", String.valueOf(position));
                ProductFragment productFragment = new ProductFragment();
                return productFragment;
            case 1 :
                Log.i("msg", String.valueOf(position));
                ScanFragment scanFragment = new ScanFragment();
                return scanFragment;
            case 2 :
                Log.i("msg", String.valueOf(position));
                SearchFragment searchFragment = new SearchFragment();
                return searchFragment;
            case 3 :
                Log.i("msg", String.valueOf(position));
                NotificationFragment notificationFragment = new NotificationFragment();
                return notificationFragment;
            case 4 :
                Log.i("msg", String.valueOf(position));
                UserFragment userFragment = new UserFragment();
                return userFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totaltabs;
    }
}
