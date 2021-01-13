
package com.daytoday.customer.dailydelivery.Network.Response;

import java.util.List;

import com.daytoday.customer.dailydelivery.HomeScreen.Model.AuthUser;
import com.google.android.gms.auth.api.Auth;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuthUserResponse {

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("cust_details")
    @Expose
    private List<AuthUser> custDetails = null;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<AuthUser> getCustDetails() {
        return custDetails;
    }

    public void setCustDetails(List<AuthUser> custDetails) {
        this.custDetails = custDetails;
    }
}
