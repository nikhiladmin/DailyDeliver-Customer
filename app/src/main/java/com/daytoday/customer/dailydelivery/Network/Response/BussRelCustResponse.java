package com.daytoday.customer.dailydelivery.Network.Response;

import com.daytoday.business.dailydelivery.MainHomeScreen.Model.Customers;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BussRelCustResponse {

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("cust")
    @Expose
    private List<Customers> custumers = null;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public List<Customers> getCustumers() {
        return custumers;
    }

    public void setCust(List<Customers> cust) {
        this.custumers = cust;
    }
}
