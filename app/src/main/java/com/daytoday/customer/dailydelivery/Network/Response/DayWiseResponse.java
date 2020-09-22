package com.daytoday.customer.dailydelivery.Network.Response;

import com.daytoday.customer.dailydelivery.Dates;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DayWiseResponse {
    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("accepted")
    @Expose
    private List<Dates> accepted = null;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public List<Dates> getAccepted() {
        return accepted;
    }

    public void setAccepted(List<Dates> accepted) {
        this.accepted = accepted;
    }
}
