package com.daytoday.customer.dailydelivery.Network.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class YesNoResponse {
    @SerializedName("error")
    @Expose
    Boolean error;
    @SerializedName("message")
    @Expose
    String message;

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
}
