package com.daytoday.customer.dailydelivery.HomeScreen.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.daytoday.customer.dailydelivery.CalendarBottomSheet;
import com.daytoday.customer.dailydelivery.Dates;
import com.daytoday.customer.dailydelivery.HomeScreen.Model.Product;
import com.daytoday.customer.dailydelivery.HomeScreen.Model.Transaction;
import com.daytoday.customer.dailydelivery.HomeScreen.ViewModel.DatesViewModel;
import com.daytoday.customer.dailydelivery.Network.ApiInterface;
import com.daytoday.customer.dailydelivery.Network.Client;
import com.daytoday.customer.dailydelivery.Network.Response.YesNoResponse;
import com.daytoday.customer.dailydelivery.R;
import com.daytoday.customer.dailydelivery.Utilities.AppConstants;
import com.daytoday.customer.dailydelivery.Utilities.AppUtils;
import com.daytoday.customer.dailydelivery.Utilities.FirebaseUtils;
import com.daytoday.customer.dailydelivery.Utilities.Request;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CalenderActivity extends AppCompatActivity {
    public static final String CURRENT_PRODUCT = "CURRENT_PRODUCT";
    public static final String CUSTOMER_ID = "CUSTOMER_ID";
    MaterialCalendarView calendarView;
    String bussID,custID,bussCustId;
    ApiInterface apiInterface;
    DatesViewModel datesViewModel;
    ProgressBar progressBar;
    Product currentProduct;
    CardView monthCardView,totalCardView;
    MaterialTextView totalAccepted,totalRejected,totalPending,totalPriceTextView,currentMonthPriceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);
        currentProduct = getIntent().getParcelableExtra(CURRENT_PRODUCT);
        bussCustId = currentProduct.getUniqueId().toString();
        bussID = currentProduct.getBussId();
        custID = getIntent().getStringExtra(CUSTOMER_ID);
        getSupportActionBar().setTitle(currentProduct.getName().toUpperCase()+"");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        calendarView = findViewById(R.id.calendar);
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        datesViewModel = new DatesViewModel(bussCustId);
        apiInterface = Client.getClient().create(ApiInterface.class);
        monthCardView = findViewById(R.id.month_card);
        totalCardView = findViewById(R.id.total_card);
        totalAccepted = findViewById(R.id.total_accepted);
        totalPending = findViewById(R.id.total_pending);
        totalRejected = findViewById(R.id.total_cancelled);
        totalPriceTextView = findViewById(R.id.total_price);
        currentMonthPriceTextView = findViewById(R.id.current_month_price);

        totalPriceTextView.setText("₹"+currentProduct.getPrice());
        currentMonthPriceTextView.setText("₹"+currentProduct.getPrice());

        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                CalendarDay day = CalendarDay.from(date.getYear(),date.getMonth(),date.getDay());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Transaction transaction = datesViewModel.getTransaction(day);
                    if (transaction != null) {
                        CalendarBottomSheet calendarBottomSheet = new CalendarBottomSheet(transaction,date);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(CURRENT_PRODUCT,currentProduct);
                        calendarBottomSheet.setArguments(bundle);
                        calendarBottomSheet.show(getSupportFragmentManager(),"CalendarActivity");
                    }else
                    {
                        Snackbar.make(findViewById(android.R.id.content).getRootView(),"No transaction found on this date!",Snackbar.LENGTH_LONG).show();
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
        datesViewModel.totalAcceptedLiveData.observe(this, s -> totalAccepted.setText(""+s));
        datesViewModel.totalRejectedLiveData.observe(this, s -> totalRejected.setText(""+s));
        datesViewModel.totalPendingLiveData.observe(this, s -> totalPending.setText(""+s));

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void sendToReject(CalendarDay day, Transaction transaction) {
        HashMap<String,String> value = FirebaseUtils.getValueMapOfRequest(day,transaction.getQuantity(), Request.REJECTED);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Buss_Cust_DayWise").child(bussCustId);
        reference.child(FirebaseUtils.getDatePath(day))
                .setValue(value);
        FirebaseUtils.incrementAccToReq(day,reference,Request.REJECTED);
        FirebaseUtils.decrementAccToReq(day,reference,Request.PENDING);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void SendToAccept(CalendarDay day, Transaction transaction) {
        HashMap<String,String> value = FirebaseUtils.getValueMapOfRequest(day,transaction.getQuantity(), Request.ACCEPTED);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Buss_Cust_DayWise").child(bussCustId);
        reference.child(FirebaseUtils.getDatePath(day))
                .setValue(value);
        FirebaseUtils.incrementAccToReq(day,reference,Request.ACCEPTED);
        FirebaseUtils.decrementAccToReq(day,reference,Request.PENDING);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
