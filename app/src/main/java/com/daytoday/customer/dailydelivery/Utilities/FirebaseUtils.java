package com.daytoday.customer.dailydelivery.Utilities;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.HashMap;

public class FirebaseUtils {

    public static final Integer INCREMENT = +1;
    public static final Integer DECREMENT = -1;

    public static String getDatePath(CalendarDay day) {
        return day.getYear() + "/" + day.getMonth() + "/" + day.getDay();
    }

    public static String getYearlyPathOnRequest(CalendarDay day, String request) {
        return day.getYear() + "/Yearly-Total/" + request;
    }

    public static String getMonthlyPathOnRequest(CalendarDay day, String request) {
        return day.getYear() + "/" + day.getMonth() + "/Monthly-Total/" + request;
    }

    public static String getAllMonthPath(String year, String month) {
        return year + "/" + month ;
    }

    public static HashMap<String, String> getValueMapOfRequest(CalendarDay day, String quantity, String request) {
        HashMap<String, String> value = new HashMap<>();
        value.put("Status",request);
        value.put("Year", String.valueOf(day.getYear()));
        value.put("Mon", String.valueOf(day.getMonth()));
        value.put("Day", String.valueOf(day.getDay()));
        value.put("Quantity",quantity);
        value.put("Time",AppUtils.getCurrentTimeStamp());
        return value;
    }

    public static void changeValueOnReference(DatabaseReference reference, Integer changeValue) {
        reference.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                Integer value = mutableData.getValue(Integer.class);
                if (value == null){
                    mutableData.setValue(1);
                }else {
                    mutableData.setValue(value+changeValue);
                }
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean completedOrNot, @Nullable DataSnapshot dataSnapshot) {
                if (!completedOrNot){
                    //TODO We need to add tag here and everywhere
                    //TODO log in crashlytics or to do anything
                    Log.d("todo/tag","NOT Completed");
                }
            }
        });
    }

    public static void incrementAccToReq(CalendarDay day, DatabaseReference reference, String request) {
        // request means pending, accepted Or rejected
        DatabaseReference yearlyReference = reference.child(getYearlyPathOnRequest(day, request));
        changeValueOnReference(yearlyReference,INCREMENT);
        DatabaseReference monthlyReference = reference.child(getMonthlyPathOnRequest(day, request));
        changeValueOnReference(monthlyReference,INCREMENT);
    }

    public static void decrementAccToReq(CalendarDay day, DatabaseReference reference, String request) {
        // request means pending, accepted Or rejected
        DatabaseReference newReference = reference.child(getYearlyPathOnRequest(day, request));
        changeValueOnReference(newReference,DECREMENT);
        DatabaseReference monthlyReference = reference.child(getMonthlyPathOnRequest(day, request));
        changeValueOnReference(monthlyReference,DECREMENT);
    }
}
