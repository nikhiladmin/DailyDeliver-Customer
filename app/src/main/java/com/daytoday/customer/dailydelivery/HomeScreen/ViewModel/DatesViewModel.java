package com.daytoday.customer.dailydelivery.HomeScreen.ViewModel;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.daytoday.customer.dailydelivery.HomeScreen.Model.Transaction;
import com.daytoday.customer.dailydelivery.Utilities.AppUtils;
import com.daytoday.customer.dailydelivery.Utilities.FirebaseUtils;
import com.daytoday.customer.dailydelivery.Utilities.Request;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.Iterator;
import java.util.List;

public class DatesViewModel extends ViewModel {
    private DatesRepo datesRepo;
    public MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    public MutableLiveData<String> totalAcceptedLiveData = new MutableLiveData<>();
    public MutableLiveData<String> totalRejectedLiveData = new MutableLiveData<>();
    public MutableLiveData<String> totalPendingLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Transaction>> totalLiveData = new MutableLiveData<>();
    String busscustId;

    public DatesViewModel(String busscustId) {
        this.busscustId = busscustId;
        datesRepo = new DatesRepo();
        isLoading.setValue(false);
    }

    public MutableLiveData<List<Transaction>> getTotalList(CalendarDay day) {
        isLoading.setValue(true);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Buss_Cust_DayWise").child(busscustId)
                .child(FirebaseUtils.getAllMonthPath("" + day.getYear(),day.getMonth() + ""));
        getCurrentMonth(day);
        datesRepo.requestTotalList(this,reference);
        return totalLiveData;
    }

    public MutableLiveData<List<Transaction>> getTotalLiveData() {
        return totalLiveData;
    }

    /**
     *
     * @param selectedDay
     * @return Null if no data is present
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public Transaction getTransaction(CalendarDay selectedDay){
        return totalLiveData.getValue()!=null ? totalLiveData.getValue().stream()
                .filter(transaction -> transaction.getYear().equals("" + selectedDay.getYear())
                        && transaction.getMon().equals("" + selectedDay.getMonth())
                        && transaction.getDay().equals("" + selectedDay.getDay()))
                .findFirst()
                .orElse(null) : null;
    }
    private void getCurrentMonth(CalendarDay day)
    {
        totalPendingLiveData.setValue("0");
        totalRejectedLiveData.setValue("0");
        totalAcceptedLiveData.setValue("0");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Buss_Cust_DayWise").child(busscustId)
                .child(FirebaseUtils.getAllMonthPath(day.getYear()+"",""+day.getMonth())).child("Monthly-Total");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterator iterator = dataSnapshot.getChildren().iterator();

                while (iterator.hasNext()) {
                    DataSnapshot snapshot = ((DataSnapshot) iterator.next());
                    if(snapshot.getKey().equals(Request.REJECTED))
                    {
                        totalRejectedLiveData.setValue(snapshot.getValue()+"");
                    }else if(snapshot.getKey().equals(Request.PENDING))
                    {
                        totalPendingLiveData.setValue(snapshot.getValue()+"");
                    }else
                    {
                        totalAcceptedLiveData.setValue(snapshot.getValue()+"");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
