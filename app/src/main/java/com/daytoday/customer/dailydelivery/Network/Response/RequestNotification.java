package com.daytoday.customer.dailydelivery.Network.Response;

import com.google.gson.annotations.SerializedName;

public class RequestNotification {
    @SerializedName("to") //  "to" changed to token
    private String token;

    @SerializedName("data")
    private SendDataModel sendDataModel;

    public String getToken() {
        return token;
    }

    public RequestNotification setToken(String token) {
        this.token = token;
        return this;
    }

    public SendDataModel getSendDataModel() {
        return sendDataModel;
    }

    public RequestNotification setSendDataModel(SendDataModel sendDataModel) {
        this.sendDataModel = sendDataModel;
        return this;
    }

    public RequestNotification(String token, SendDataModel sendDataModel) {
        this.token = token;
        this.sendDataModel = sendDataModel;
    }

    public RequestNotification() {
    }
}
