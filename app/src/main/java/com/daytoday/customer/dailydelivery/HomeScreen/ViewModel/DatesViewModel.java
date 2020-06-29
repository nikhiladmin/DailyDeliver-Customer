package com.daytoday.customer.dailydelivery.HomeScreen.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.List;

public class DatesViewModel extends ViewModel {
    private DatesRepo datesRepo;
    private MutableLiveData<List<CalendarDay>> pendinglivedata = new MutableLiveData<>();
    private MutableLiveData<List<CalendarDay>> acceptedlivedata = new MutableLiveData<>();
    private MutableLiveData<List<CalendarDay>> canceledlivedata = new MutableLiveData<>();
    String bussId,custId;

    public DatesViewModel(String bussId, String custId) {
        datesRepo = new DatesRepo();
        this.bussId = bussId;
        this.custId = custId;
    }

    public MutableLiveData<List<CalendarDay>> getPendingList()
    {
        pendinglivedata = datesRepo.requestPendingList(bussId,custId);
        return pendinglivedata;
    }

    public MutableLiveData<List<CalendarDay>> getAcceptedList()
    {
        acceptedlivedata = datesRepo.requestAcceptedList(bussId,custId);
        return acceptedlivedata;
    }

    public MutableLiveData<List<CalendarDay>> getCancelledList()
    {
        canceledlivedata = datesRepo.requestCancelledList(bussId,custId);
        return canceledlivedata;
    }
}
