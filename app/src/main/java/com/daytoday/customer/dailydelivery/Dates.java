package com.daytoday.customer.dailydelivery;

import android.content.Intent;
import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.prolificinteractive.materialcalendarview.CalendarDay;

public class Dates {

    @SerializedName("Date")
    @Expose
    private String myDate;

    private CalendarDay date;

    @SerializedName("Quantity")
    @Expose
    private String quantity;

    public void convertDates()
    {
        String parseDate[] = myDate.split("-");
        CalendarDay dateconverter=null;
        if(parseDate.length >= 2)
            dateconverter = CalendarDay.from(Integer.parseInt(parseDate[0]),Integer.parseInt(parseDate[1]),Integer.parseInt(parseDate[2]));
        if(dateconverter!=null)
            this.date=dateconverter;
    }

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
