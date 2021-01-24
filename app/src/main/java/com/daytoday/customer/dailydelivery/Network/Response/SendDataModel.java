package com.daytoday.customer.dailydelivery.Network.Response;

public class SendDataModel {
    private String notificationStatus,quantity,productName;
    private String fromWhichPerson,fromWhichPersonID, toWhichPerson,toWhichPersonId;

    public SendDataModel(String notificationStatus, String quantity, String productName, String fromWhichPerson, String fromWhichPersonID, String toWhichPerson, String toWhichPersonId) {
        this.notificationStatus = notificationStatus;
        this.quantity = quantity;
        this.productName = productName;
        this.fromWhichPerson = fromWhichPerson;
        this.fromWhichPersonID = fromWhichPersonID;
        this.toWhichPerson = toWhichPerson;
        this.toWhichPersonId = toWhichPersonId;
    }

    public SendDataModel() {
    }

    @Override
    public String toString() {
        return "SendDataModel{" +
                "notificationStatus='" + notificationStatus + '\'' +
                ", quantity='" + quantity + '\'' +
                ", productName='" + productName + '\'' +
                ", fromWhichPerson='" + fromWhichPerson + '\'' +
                ", fromWhichPersonID='" + fromWhichPersonID + '\'' +
                ", toWhichPerson='" + toWhichPerson + '\'' +
                ", toWhichPersonId='" + toWhichPersonId + '\'' +
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

    public SendDataModel setToWhichPersonId(String toWhichPersonId) {
        this.toWhichPersonId = toWhichPersonId;
        return this;
    }

    public String getToWhichPersonId() {
        return toWhichPersonId;
    }
}