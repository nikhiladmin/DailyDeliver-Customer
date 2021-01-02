package com.daytoday.customer.dailydelivery.searchui;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

public class SearchDataFactory extends DataSource.Factory {
    SearchDataSource dataSource;
    MutableLiveData<SearchDataSource> liveData;
    private String key;
    private String custid;
    public SearchDataFactory(String key,String custid)
    {
        this.key=key;
        this.custid = custid;
        liveData = new MutableLiveData<>();
    }
    @NonNull
    @Override
    public DataSource create() {
        dataSource = new SearchDataSource(key,custid);
        liveData.postValue(dataSource);
        return dataSource;
    }
    public MutableLiveData<SearchDataSource> getLiveData()
    {
        return liveData;
    }
}
