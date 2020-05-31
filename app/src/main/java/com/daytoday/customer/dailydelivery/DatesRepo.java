package com.daytoday.customer.dailydelivery;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DatesRepo {
    public MutableLiveData<List<CalendarDay>> requestPendingList(String bussId,String custId) {
        MutableLiveData<List<CalendarDay>> liveData = new MutableLiveData<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Buss-Cust-DayWise").child(bussId).child(custId).child("Pending")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Iterator iterator = dataSnapshot.getChildren().iterator();
                        List<CalendarDay> list = new ArrayList<>();
                        while (iterator.hasNext())
                        {
                            DataSnapshot currentSnapshot = (DataSnapshot)iterator.next();
                            String year = currentSnapshot.child("Year").getValue().toString();
                            String  mon = currentSnapshot.child("Mon").getValue().toString();
                            String day = currentSnapshot.child("Day").getValue().toString();
                            String quantity = currentSnapshot.child("quantity").getValue().toString();
                            if (quantity != null)
                            {
                                list.add(CalendarDay.from(Integer.parseInt(year),Integer.parseInt(mon),Integer.parseInt(day)));
                            }
                        }
                        liveData.setValue(list);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        return liveData;
    }

    public MutableLiveData<List<CalendarDay>> requestAcceptedList(String bussId,String custId) {
        MutableLiveData<List<CalendarDay>> liveData = new MutableLiveData<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Buss-Cust-DayWise").child(bussId).child(custId).child("Accepted")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Iterator iterator = dataSnapshot.getChildren().iterator();
                        List<CalendarDay> list = new ArrayList<>();
                        while (iterator.hasNext())
                        {
                            DataSnapshot currentSnapshot = (DataSnapshot)iterator.next();
                            String year = currentSnapshot.child("Year").getValue().toString();
                            String  mon = currentSnapshot.child("Mon").getValue().toString();
                            String day = currentSnapshot.child("Day").getValue().toString();
                            String quantity = currentSnapshot.child("quantity").getValue().toString();
                            if (quantity != null)
                            {
                                list.add(CalendarDay.from(Integer.parseInt(year),Integer.parseInt(mon),Integer.parseInt(day)));
                            }
                        }
                        liveData.setValue(list);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        return liveData;
    }

    public MutableLiveData<List<CalendarDay>> requestCancelledList(String bussId,String custId) {
        MutableLiveData<List<CalendarDay>> liveData = new MutableLiveData<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Buss-Cust-DayWise").child(bussId).child(custId).child("Rejected")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Iterator iterator = dataSnapshot.getChildren().iterator();
                        List<CalendarDay> list = new ArrayList<>();
                        while (iterator.hasNext())
                        {
                            DataSnapshot currentSnapshot = (DataSnapshot)iterator.next();
                            String year = currentSnapshot.child("Year").getValue().toString();
                            String  mon = currentSnapshot.child("Mon").getValue().toString();
                            String day = currentSnapshot.child("Day").getValue().toString();
                            String quantity = currentSnapshot.child("quantity").getValue().toString();
                            if (quantity != null)
                            {
                                list.add(CalendarDay.from(Integer.parseInt(year),Integer.parseInt(mon),Integer.parseInt(day)));
                            }
                        }
                        liveData.setValue(list);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        return liveData;
    }
}
