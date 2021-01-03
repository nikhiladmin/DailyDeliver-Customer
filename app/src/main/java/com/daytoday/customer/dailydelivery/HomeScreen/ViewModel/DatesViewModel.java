package com.daytoday.customer.dailydelivery.HomeScreen.ViewModel;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.daytoday.customer.dailydelivery.HomeScreen.Model.Transaction;
import com.daytoday.customer.dailydelivery.Utilities.FirebaseUtils;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.List;

public class DatesViewModel extends ViewModel {
    private DatesRepo datesRepo;
    public MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
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
        return totalLiveData.getValue().stream()
                .filter(transaction -> transaction.getYear().equals("" + selectedDay.getYear())
                        && transaction.getMon().equals("" + selectedDay.getMonth())
                        && transaction.getDay().equals("" + selectedDay.getDay()))
                .findFirst()
                .orElse(null);
    }
}
