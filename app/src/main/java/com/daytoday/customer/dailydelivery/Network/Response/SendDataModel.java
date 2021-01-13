package com.daytoday.customer.dailydelivery.Network.Response;

public class SendDataModel {
    private String notificationStatus,quantity,productName;
    private String fromWhichPerson,fromWhichPersonID, toWhichPerson;

    public SendDataModel(String notificationStatus, String quantity, String productName, String fromWhichPerson, String toWhichPerson) {
        this.notificationStatus = notificationStatus;
        this.quantity = quantity;
        this.productName = productName;
        this.fromWhichPerson = fromWhichPerson;
        this.toWhichPerson = toWhichPerson;
    }

    public SendDataModel() {
    }

    @Override
    public String toString() {
        return "SendDataModel { " +
                "notificationStatus='" + notificationStatus + '\'' +
                " , quantity='" + quantity + '\'' +
                " , productName='" + productName + '\'' +
                " , fromWhichPerson='" + fromWhichPerson + '\'' +
                " , toWhichPerson='" + toWhichPerson + '\'' +
                '}';
    }

    public String getNotificationStatus() {
        return notificationStatus;
    }

    public SendDataModel setNotificationStatus(String notificationStatus) {
        this.notificationStatus = notificationStatus;
        return this;
    }

    public String getQuantity() {
        return quantity;
    }

    public SendDataModel setQuantity(String quantity) {
        this.quantity = quantity;
        return this;
    }

    public String getProductName() {
        return productName;
    }

    public SendDataModel setProductName(String productName) {
        this.productName = productName;
        return this;
    }

    public String getFromWhichPerson() {
        return fromWhichPerson;
    }

    public SendDataModel setFromWhichPerson(String fromWhichPerson) {
        this.fromWhichPerson = fromWhichPerson;
        return this;
    }

    public String getToWhichPerson() {
        return toWhichPerson;
    }

    public SendDataModel setToWhichPerson(String toWhichPerson) {
        this.toWhichPerson = toWhichPerson;
        return this;
    }

    public String getFromWhichPersonID() {
        return fromWhichPersonID;
    }

    public SendDataModel setFromWhichPersonID(String fromWhichPersonID) {
        this.fromWhichPersonID = fromWhichPersonID;
        return this;
    }
}