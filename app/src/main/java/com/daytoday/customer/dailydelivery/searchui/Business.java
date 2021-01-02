package com.daytoday.customer.dailydelivery.searchui;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Business {
    @SerializedName("bussuserId")
    @Expose
    private String bussuserId;
    @SerializedName("bussname")
    @Expose
    private String bussname;
    @SerializedName("bussphoneno")
    @Expose
    private String bussphoneno;
    @SerializedName("bussaddress")
    @Expose
    private String bussaddress;
    @SerializedName("bussprice")
    @Expose
    private Integer bussprice;
    @SerializedName("DorM")
    @Expose
    private String dorM;
    @SerializedName("paymode")
    @Expose
    private String paymode;
    @SerializedName("custConnected")
    @Expose
    private Boolean custConnected;

    public String getBussuserId() {
        return bussuserId;
    }

    public void setBussuserId(String bussuserId) {
        this.bussuserId = bussuserId;
    }

    public String getBussname() {
        return bussname;
    }

    public void setBussname(String bussname) {
        this.bussname = bussname;
    }

    public String getBussphoneno() {
        return bussphoneno;
    }

    public void setBussphoneno(String bussphoneno) {
        this.bussphoneno = bussphoneno;
    }

    public String getBussaddress() {
        return bussaddress;
    }

    public void setBussaddress(String bussaddress) {
        this.bussaddress = bussaddress;
    }

    public Integer getBussprice() {
        return bussprice;
    }

    public void setBussprice(Integer bussprice) {
        this.bussprice = bussprice;
    }

    public String getDorM() {
        return dorM;
    }

    public void setDorM(String dorM) {
        this.dorM = dorM;
    }

    public String getPaymode() {
        return paymode;
    }

    public void setPaymode(String paymode) {
        this.paymode = paymode;
    }

    public Boolean getCustConnected() {
        return custConnected;
    }

    public void setCustConnected(Boolean custConnected) {
        this.custConnected = custConnected;
    }
}
