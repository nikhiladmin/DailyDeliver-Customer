package com.daytoday.customer.dailydelivery;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class ProductViewModel extends ViewModel {
    private ProductRepo productRepo;
    private MutableLiveData<List<Product>> liveData = new MutableLiveData<>();

    public ProductViewModel() {
        this.productRepo = new ProductRepo();
    }

    public LiveData<List<Product>> getProduct() {
        liveData = productRepo.requestProduct();
        return liveData;
    }
}
