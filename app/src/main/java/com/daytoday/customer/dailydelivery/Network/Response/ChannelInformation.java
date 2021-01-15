package com.daytoday.customer.dailydelivery.Network.Response;

public class ChannelInformation {
    String channelId;
    String channelName;
    String channelDescription;

    public ChannelInformation(String channelId, String channelName, String channelDescription) {
        this.channelId = channelId;
        this.channelName = channelName;
        this.channelDescription = channelDescription;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelDescription() {
        return channelDescription;
    }

    public void setChannelDescription(String channelDescription) {
        this.channelDescription = channelDescription;
    }
}
