package com.daytoday.customer.dailydelivery.Network.Response;

import com.daytoday.customer.dailydelivery.Product;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BussDetailsResponse {

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("buss")
    @Expose
    private List<Product> buss = null;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public List<Product> getBuss() {
        return buss;
    }

    public void setBuss(List<Product> buss) {
        this.buss = buss;
    }
}
