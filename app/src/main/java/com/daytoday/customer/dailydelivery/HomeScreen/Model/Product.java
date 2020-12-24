package com.daytoday.customer.dailydelivery.HomeScreen.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Product {

    @SerializedName("uniqueId")
    @Expose
    private Integer uniqueId;
    @SerializedName("bussId")
    @Expose
    private String bussId;
    @SerializedName("custId")
    @Expose
    private String custId;
    @SerializedName("bussuserId")
    @Expose
    private String bussuserid;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Phoneno")
    @Expose
    private String phoneno;
    @SerializedName("Address")
    @Expose
    private String address;
    @SerializedName("Price")
    @Expose
    private Integer price;
    @SerializedName("DOrM")
    @Expose
    private String dOrM;
    @SerializedName("Payment")
    @Expose
    private String payment;
    @SerializedName("Imgurl")
    @Expose
    private String imgurl;
    @SerializedName("NoOfCust")
    @Expose
    private Integer noOfCust;
    @SerializedName("TotCan")
    @Expose
    private Integer totCan;
    @SerializedName("TotEarn")
    @Expose
    private Integer totEarn;
    @SerializedName("TotPen")
    @Expose
    private Integer totPen;

    @SerializedName("custConnected")
    @Expose
    private Boolean isConnected;

    public Boolean getConnected() {
        return isConnected;
    }

    public void setConnected(Boolean connected) {
        isConnected = connected;
    }

    public Integer getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(Integer uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getBussId() {
        return bussId;
    }

    public void setBussId(String bussId) {
        this.bussId = bussId;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getBussuserid() {
        return bussuserid;
    }

    public void setBussuserid(String bussuserid) {
        this.bussuserid = bussuserid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getDOrM() {
        return dOrM;
    }

    public void setDOrM(String dOrM) {
        this.dOrM = dOrM;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getdOrM() {
        return dOrM;
    }

    public void setdOrM(String dOrM) {
        this.dOrM = dOrM;
    }

    public Integer getNoOfCust() {
        return noOfCust;
    }

    public void setNoOfCust(Integer noOfCust) {
        this.noOfCust = noOfCust;
    }

    public Integer getTotCan() {
        return totCan;
    }

    public void setTotCan(Integer totCan) {
        this.totCan = totCan;
    }

    public Integer getTotEarn() {
        return totEarn;
    }

    public void setTotEarn(Integer totEarn) {
        this.totEarn = totEarn;
    }

    public Integer getTotPen() {
        return totPen;
    }

    public void setTotPen(Integer totPen) {
        this.totPen = totPen;
    }

    @Override
    public String toString() {
        return "Product{" +
                "uniqueId=" + uniqueId +
                ", bussId='" + bussId + '\'' +
                ", custId='" + custId + '\'' +
                ", bussuserid='" + bussuserid + '\'' +
                ", name='" + name + '\'' +
                ", phoneno='" + phoneno + '\'' +
                ", address='" + address + '\'' +
                ", price=" + price +
                ", dOrM='" + dOrM + '\'' +
                ", payment='" + payment + '\'' +
                ", imgurl='" + imgurl + '\'' +
                ", noOfCust=" + noOfCust +
                ", totCan=" + totCan +
                ", totEarn=" + totEarn +
                ", totPen=" + totPen +
                '}';
    }
}
