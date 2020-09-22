package com.daytoday.customer.dailydelivery;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.prolificinteractive.materialcalendarview.CalendarDay;

public class Dates {

    @SerializedName("Date")
    @Expose
    private CalendarDay date;
    @SerializedName("Quantity")
    @Expose
    private String quantity;

    public Dates(CalendarDay date, String quantity) {
        this.date = date;
        this.quantity = quantity;
    }

    public CalendarDay getDate() {
        return date;
    }

    public void setDate(CalendarDay date) {
        this.date = date;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
