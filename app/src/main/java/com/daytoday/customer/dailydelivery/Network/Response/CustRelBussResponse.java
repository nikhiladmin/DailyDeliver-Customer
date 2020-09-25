package com.daytoday.customer.dailydelivery.Network.Response;


import com.daytoday.customer.dailydelivery.HomeScreen.Model.Product;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CustRelBussResponse {

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("cust")
    @Expose
    private List<Product> cust = null;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public List<Product> getCust() {
        return cust;
    }

    public void setCust(List<Product> cust) {
        this.cust = cust;
    }
}
