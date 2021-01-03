package com.daytoday.customer.dailydelivery.HomeScreen.Model;

public class Transaction {
    String Day;
    String Mon;
    String Quantity;
    String Status;
    String Time;
    String Year;

    public String getMon() {
        return Mon;
    }

    public void setMon(String mon) {
        Mon = mon;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        Year = year;
    }

    public String getDay() {
        return Day;
    }

    public void setDay(String day) {
        Day = day;
    }

    @Override
    public String toString() {
        return "Transaction { " +
                "Day='" + Day + '\'' +
                " , Mon='" + Mon + '\'' +
                " , Quantity='" + Quantity + '\'' +
                " , Status='" + Status + '\'' +
                " , Time='" + Time + '\'' +
                " , Year='" + Year + '\'' +
                "} ";
    }
}
