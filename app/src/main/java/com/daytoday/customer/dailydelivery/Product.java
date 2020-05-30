package com.daytoday.customer.dailydelivery;

public class Product {    private String productName, MonthDay;
    private String price;
    private String image;
    private String cust_cout;
    private String PhoneNo;
    private String Address;
    private String Id;

    public Product(String productName, String monthDay, String price, String image, String cust_cout, String phoneNo, String address, String id) {
        this.productName = productName;
        MonthDay = monthDay;
        this.price = price;
        this.image = image;
        this.cust_cout = cust_cout;
        PhoneNo = phoneNo;
        Address = address;
        Id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getMonthDay() {
        return MonthDay;
    }

    public void setMonthDay(String monthDay) {
        MonthDay = monthDay;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCust_cout() {
        return cust_cout;
    }

    public void setCust_cout(String cust_cout) {
        this.cust_cout = cust_cout;
    }

    public String getPhoneNo() {
        return PhoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        PhoneNo = phoneNo;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }
}
