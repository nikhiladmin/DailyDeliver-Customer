package com.daytoday.customer.dailydelivery.HomeScreen.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.daytoday.customer.dailydelivery.HomeScreen.Model.Product;

import java.util.List;

public class ProductViewModel extends ViewModel {
    private ProductRepo productRepo;
    private MutableLiveData<List<Product>> liveData;

    public ProductViewModel() {
        this.productRepo = new ProductRepo();
    }

    public LiveData<List<Product>> getProduct() {
        if(liveData==null)
            liveData = productRepo.requestProduct();
        return liveData;
    }
}
