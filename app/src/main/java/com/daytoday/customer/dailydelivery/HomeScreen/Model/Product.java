package com.daytoday.customer.dailydelivery.HomeScreen.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


import java.io.Serializable;
public class Product implements Parcelable {

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
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("custConnected")
    @Expose
    private Boolean isConnected;

    protected Product(Parcel in) {
        if (in.readByte() == 0) {
            uniqueId = null;
        } else {
            uniqueId = in.readInt();
        }
        bussId = in.readString();
        custId = in.readString();
        bussuserid = in.readString();
        name = in.readString();
        phoneno = in.readString();
        address = in.readString();
        if (in.readByte() == 0) {
            price = null;
        } else {
            price = in.readInt();
        }
        dOrM = in.readString();
        payment = in.readString();
        imgurl = in.readString();
        if (in.readByte() == 0) {
            noOfCust = null;
        } else {
            noOfCust = in.readInt();
        }
        if (in.readByte() == 0) {
            totCan = null;
        } else {
            totCan = in.readInt();
        }
        if (in.readByte() == 0) {
            totEarn = null;
        } else {
            totEarn = in.readInt();
        }
        if (in.readByte() == 0) {
            totPen = null;
        } else {
            totPen = in.readInt();
        }
        token = in.readString();
        byte tmpIsConnected = in.readByte();
        isConnected = tmpIsConnected == 0 ? null : tmpIsConnected == 1;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (uniqueId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(uniqueId);
        }
        dest.writeString(bussId);
        dest.writeString(custId);
        dest.writeString(bussuserid);
        dest.writeString(name);
        dest.writeString(phoneno);
        dest.writeString(address);
        if (price == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(price);
        }
        dest.writeString(dOrM);
        dest.writeString(payment);
        dest.writeString(imgurl);
        if (noOfCust == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(noOfCust);
        }
        if (totCan == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(totCan);
        }
        if (totEarn == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(totEarn);
        }
        if (totPen == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(totPen);
        }
        dest.writeString(token);
        dest.writeByte((byte) (isConnected == null ? 0 : isConnected ? 1 : 2));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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
                ", token='" + token + '\'' +
                ", isConnected=" + isConnected +
                '}';
    }
}
