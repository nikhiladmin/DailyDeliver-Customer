package com.daytoday.customer.dailydelivery.HomeScreen.ViewModel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.daytoday.customer.dailydelivery.Dates;
import com.daytoday.customer.dailydelivery.Network.ApiInterface;
import com.daytoday.customer.dailydelivery.Network.Client;
import com.daytoday.customer.dailydelivery.Network.Response.DayWiseResponse;
import com.daytoday.customer.dailydelivery.Utilities.AppConstants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DatesRepo {
    public MutableLiveData<List<Dates>> requestPendingList(String busscustId) {
        MutableLiveData<List<Dates>> liveData = new MutableLiveData<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Buss_Cust_DayWise").child(busscustId).child("Pending")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Iterator iterator = dataSnapshot.getChildren().iterator();
                        List<Dates> list = new ArrayList<>();
                        while (iterator.hasNext())
                        {
                            DataSnapshot currentSnapshot = (DataSnapshot)iterator.next();
                            String year = currentSnapshot.child("Year").getValue().toString();
                            String  mon = currentSnapshot.child("Mon").getValue().toString();
                            String day = currentSnapshot.child("Day").getValue().toString();
                            String quantity = currentSnapshot.child("quantity").getValue().toString();
                            if (quantity != null)
                            {
                                Log.i("msg","true");
                                list.add(new Dates(CalendarDay.from(Integer.parseInt(year),Integer.parseInt(mon),Integer.parseInt(day)),quantity));
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

    public MutableLiveData<List<Dates>> requestAcceptedList(String busscustId) {
        MutableLiveData<List<Dates>> liveData = new MutableLiveData<>();
        ApiInterface apiInterface = Client.getClient().create(ApiInterface.class);
        Call<DayWiseResponse> dayWiseResponseCall = apiInterface.getDayWise(busscustId);
        dayWiseResponseCall.enqueue(new Callback<DayWiseResponse>() {
            @Override
            public void onResponse(Call<DayWiseResponse> call, Response<DayWiseResponse> response) {
                if (response.body().getError()){
                    liveData.setValue(response.body().getAccepted());
                }
            }

            @Override
            public void onFailure(Call<DayWiseResponse> call, Throwable t) {
                Log.i(AppConstants.ERROR_LOG,"Some Error Occurred in DateRepo Error is  : { " + t.getMessage() + " }");
            }
        });
        return liveData;
    }

    public MutableLiveData<List<Dates>> requestCancelledList(String busscustId) {
        MutableLiveData<List<Dates>> liveData = new MutableLiveData<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Buss_Cust_DayWise").child(busscustId).child("Rejected")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Iterator iterator = dataSnapshot.getChildren().iterator();
                        List<Dates> list = new ArrayList<>();
                        while (iterator.hasNext())
                        {
                            DataSnapshot currentSnapshot = (DataSnapshot)iterator.next();
                            String year = currentSnapshot.child("Year").getValue().toString();
                            String  mon = currentSnapshot.child("Mon").getValue().toString();
                            String day = currentSnapshot.child("Day").getValue().toString();
                            String quantity = currentSnapshot.child("quantity").getValue().toString();
                            if (quantity != null)
                            {
                                list.add(new Dates(CalendarDay.from(Integer.parseInt(year),Integer.parseInt(mon),Integer.parseInt(day)),quantity));
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
