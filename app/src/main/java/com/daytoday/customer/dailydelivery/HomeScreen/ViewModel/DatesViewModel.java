package com.daytoday.customer.dailydelivery.HomeScreen.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.daytoday.customer.dailydelivery.Dates;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.List;

public class DatesViewModel extends ViewModel {
    private DatesRepo datesRepo;
    private MutableLiveData<List<Dates>> pendinglivedata = new MutableLiveData<>();
    private MutableLiveData<List<Dates>> acceptedlivedata = new MutableLiveData<>();
    private MutableLiveData<List<Dates>> canceledlivedata = new MutableLiveData<>();
    String busscustId;

    public DatesViewModel(String busscustId) {
        this.busscustId = busscustId;
        datesRepo = new DatesRepo();
    }

    public MutableLiveData<List<Dates>> getPendingList()
    {
        pendinglivedata = datesRepo.requestPendingList(busscustId);
        return pendinglivedata;
    }

    public MutableLiveData<List<Dates>> getAcceptedList()
    {
        acceptedlivedata = datesRepo.requestAcceptedList(busscustId);
        return acceptedlivedata;
    }

    public MutableLiveData<List<Dates>> getCancelledList()
    {
        canceledlivedata = datesRepo.requestCancelledList(busscustId);
        return canceledlivedata;
    }

    public MutableLiveData<List<Dates>> rejectDataFromApi()
    {
        return datesRepo.rejectLiveDataApi;
    }

    public MutableLiveData<List<Dates>> pendingDataFromApi()
    {
        return datesRepo.pendingLiveDataApi;
    }
}
