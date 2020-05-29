package com.daytoday.customer.dailydelivery;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

public class ProductRepo {
    public MutableLiveData<List<Product>> requestProduct() {
        MutableLiveData<List<Product>> mutableLiveData = new MutableLiveData<>();

        //-----------------------------------------Initialise Here ---------------------------------

        List<Product> list = new ArrayList<>();

        list.add(new Product("Milk","Monthly","12","google.com","1000","9359270125","RB II 671 / D"));
        list.add(new Product("NewsPaper","Monthly","12","google.com","1000","9359270125","RB II 671 / D"));
        list.add(new Product("Extra","Monthly","12","google.com","1000","9359270125","RB II 671 / D"));

        mutableLiveData.setValue(list);
        //-----------------------------------------Ends Here ---------------------------------------
        return mutableLiveData;
    }
}
