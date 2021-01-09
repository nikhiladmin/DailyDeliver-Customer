package com.daytoday.customer.dailydelivery.HomeScreen.View;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.daytoday.customer.dailydelivery.HomeScreen.Model.Product;
import com.daytoday.customer.dailydelivery.HomeScreen.Model.Transaction;
import com.daytoday.customer.dailydelivery.HomeScreen.ViewModel.DatesViewModel;
import com.daytoday.customer.dailydelivery.Network.ApiInterface;
import com.daytoday.customer.dailydelivery.Network.Client;
import com.daytoday.customer.dailydelivery.Network.Response.RequestNotification;
import com.daytoday.customer.dailydelivery.Network.Response.SendDataModel;
import com.daytoday.customer.dailydelivery.R;
import com.daytoday.customer.dailydelivery.Utilities.AppUtils;
import com.daytoday.customer.dailydelivery.Utilities.FirebaseUtils;
import com.daytoday.customer.dailydelivery.Utilities.NotificationService;
import com.daytoday.customer.dailydelivery.Utilities.Request;
import com.daytoday.customer.dailydelivery.Utilities.SaveOfflineManager;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.util.HashMap;
import java.util.List;

import static com.daytoday.customer.dailydelivery.HomeScreen.View.ProductAdapter.BUSS_OBJECT;

public class CalenderActivity extends AppCompatActivity {
    MaterialCalendarView calendarView;
    String custID;
    ApiInterface apiInterface;
    DatesViewModel datesViewModel;
    ProgressBar progressBar;
    private Product currentProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);
        currentProduct = (Product) getIntent().getSerializableExtra(BUSS_OBJECT);
        custID = SaveOfflineManager.getUserId(this);
        getSupportActionBar().setTitle("Calender");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        calendarView = findViewById(R.id.calendar);
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        datesViewModel = new ViewModelProvider(this).get(DatesViewModel.class);
        datesViewModel.setBusscustId(currentProduct.getUniqueId().toString());
        apiInterface = Client.getClient().create(ApiInterface.class);

        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                CalendarDay day = CalendarDay.from(date.getYear(),date.getMonth(),date.getDay());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Transaction transaction = datesViewModel.getTransaction(day);
                    if (transaction != null) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(CalenderActivity.this);
                        builder.setTitle("Accept").setMessage("Do you Accept The Product");
                        ;
                        builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SendToAccept(date,transaction);
                            }
                        }).setNegativeButton("Reject", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sendToReject(date,transaction);
                            }
                        });
                        builder.show();
                    }
                }
            }
        });

        datesViewModel.getTotalList(calendarView.getCurrentDate()).observe(this, new Observer<List<Transaction>>() {
            @Override
            public void onChanged(List<Transaction> transactions) {
                calendarView.removeDecorators();
                for (Transaction transaction : transactions) {
                    Log.i("message","transaction is " +transaction.toString());
                    int drawableResourceId = AppUtils.getResourceIdDates(transaction.getStatus());
                    CircleDecorator decorator = new CircleDecorator(CalenderActivity.this,drawableResourceId,transaction);
                    calendarView.addDecorator(decorator);
                }
            }
        });

        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                datesViewModel.getTotalList(date);
            }
        });

        datesViewModel.isLoading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void sendToReject(CalendarDay day, Transaction transaction) {
        HashMap<String,String> value = FirebaseUtils.getValueMapOfRequest(day,transaction.getQuantity(), Request.REJECTED);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Buss_Cust_DayWise").child(currentProduct.getUniqueId().toString());
        reference.child(FirebaseUtils.getDatePath(day))
                .setValue(value);
        FirebaseUtils.incrementAccToReq(day,reference,Request.REJECTED);
        FirebaseUtils.decrementAccToReq(day,reference,Request.PENDING);
        //TODO Need To Complete This
        if (currentProduct.getToken()!=null) {
            RequestNotification requestNotification = new RequestNotification()
                    .setToken(currentProduct.getToken())
                    .setSendDataModel(new SendDataModel()
                            .setFromWhichPerson(SaveOfflineManager.getUserName(this))
                            .setFromWhichPersonID(SaveOfflineManager.getUserId(this))
                            .setNotificationStatus(Request.ACCEPTED)
                            .setProductName(currentProduct.getName())
                            .setQuantity(transaction.getQuantity()));
            NotificationService.sendNotification(requestNotification);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void SendToAccept(CalendarDay day, Transaction transaction) {
        HashMap<String,String> value = FirebaseUtils.getValueMapOfRequest(day,transaction.getQuantity(), Request.ACCEPTED);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Buss_Cust_DayWise").child(currentProduct.getUniqueId().toString());
        reference.child(FirebaseUtils.getDatePath(day))
                .setValue(value);
        FirebaseUtils.incrementAccToReq(day,reference,Request.ACCEPTED);
        FirebaseUtils.decrementAccToReq(day,reference,Request.PENDING);
        //TODO Need To Complete This
        if (currentProduct.getToken()!=null) {
            RequestNotification requestNotification = new RequestNotification()
                    .setToken(currentProduct.getToken())
                    .setSendDataModel(new SendDataModel()
                            .setFromWhichPerson(SaveOfflineManager.getUserName(this))
                            .setFromWhichPersonID(SaveOfflineManager.getUserId(this))
                            .setNotificationStatus(Request.ACCEPTED)
                            .setProductName(currentProduct.getName())
                            .setQuantity(transaction.getQuantity()));
            NotificationService.sendNotification(requestNotification);
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
