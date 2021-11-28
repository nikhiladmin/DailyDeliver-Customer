package com.daytoday.customer.dailydelivery.HomeScreen.View;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.daytoday.customer.dailydelivery.CalendarBottomSheet;
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
import com.daytoday.customer.dailydelivery.Utilities.RealtimeDatabase;
import com.daytoday.customer.dailydelivery.Utilities.Request;
import com.daytoday.customer.dailydelivery.Utilities.SaveOfflineManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.util.HashMap;
import java.util.List;

public class CalenderActivity extends AppCompatActivity {
    public static final String CURRENT_PRODUCT = "CURRENT_PRODUCT";
    public static final String CUSTOMER_ID = "CUSTOMER_ID";
    MaterialCalendarView calendarView;
    String bussID, custID, bussCustId;
    ApiInterface apiInterface;
    DatesViewModel datesViewModel;
    ProgressBar progressBar;
    Product currentProduct;
    CardView monthCardView, totalCardView;
    MaterialTextView totalAccepted, totalRejected, totalPending, totalPriceTextView, currentMonthPriceTextView;
    MaterialTextView totalMonthAccepted, totalMonthRejected, totalMonthPending;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);
        currentProduct = getIntent().getParcelableExtra(CURRENT_PRODUCT);
        bussCustId = currentProduct.getUniqueId().toString();
        bussID = currentProduct.getBussId();
        custID = getIntent().getStringExtra(CUSTOMER_ID);
        getSupportActionBar().setTitle(currentProduct.getName().toUpperCase() + "");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        calendarView = findViewById(R.id.calendar);
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        datesViewModel = new ViewModelProvider(this).get(DatesViewModel.class);
        datesViewModel.setBusscustId(currentProduct.getUniqueId().toString());
        apiInterface = Client.getClient().create(ApiInterface.class);
        monthCardView = findViewById(R.id.month_card);
        totalCardView = findViewById(R.id.total_card);
        totalAccepted = findViewById(R.id.total_accepted);
        totalPending = findViewById(R.id.total_pending);
        totalRejected = findViewById(R.id.total_cancelled);
        totalPriceTextView = findViewById(R.id.total_price);
        totalMonthAccepted = findViewById(R.id.month_accepted);
        totalMonthPending = findViewById(R.id.month_pending);
        totalMonthRejected = findViewById(R.id.month_cancelled);
        currentMonthPriceTextView = findViewById(R.id.current_month_price);

        totalPriceTextView.setText("₹" + currentProduct.getPrice());
        currentMonthPriceTextView.setText("₹" + currentProduct.getPrice());

        calendarView.setOnDateChangedListener((widget, date, selected) -> {
            CalendarDay day = CalendarDay.from(date.getYear(), date.getMonth(), date.getDay());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Transaction transaction = datesViewModel.getTransaction(day);
                if (transaction != null) {
                    CalendarBottomSheet calendarBottomSheet = new CalendarBottomSheet(transaction, date);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(CURRENT_PRODUCT, currentProduct);
                    calendarBottomSheet.setArguments(bundle);
                    calendarBottomSheet.show(getSupportFragmentManager(), "CalendarActivity");
                } else {
                    //Snackbar.make(findViewById(android.R.id.content).getRootView(), "No transaction found on this date!", Snackbar.LENGTH_LONG).show();

                    MaterialAlertDialogBuilder alertDialog = new MaterialAlertDialogBuilder(this);
                    alertDialog.setMessage("Do you want to request for this item?");
                    alertDialog.setTitle("Send Request?");
                    alertDialog.setCancelable(false);
                    alertDialog.setPositiveButton("Request", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //TODO add Request here
                            createPendingRequest(date, "1");
                        }
                    });
                    alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Snackbar.make(findViewById(android.R.id.content), "Profile Update Cancelled", Snackbar.LENGTH_SHORT).show();
                        }
                    }).show();
                }
            }
        });

        datesViewModel.currentYear.setValue("" + calendarView.getCurrentDate().getYear());
        datesViewModel.getCurrentYearTotal(calendarView.getCurrentDate());
        datesViewModel.getTotalList(calendarView.getCurrentDate()).observe(this, new Observer<List<Transaction>>() {
            @Override
            public void onChanged(List<Transaction> transactions) {
                calendarView.removeDecorators();
                for (Transaction transaction : transactions) {
                    Log.i("message", "transaction is " + transaction.toString());
                    int drawableResourceId = AppUtils.getResourceIdDates(transaction.getStatus());
                    CircleDecorator decorator = new CircleDecorator(CalenderActivity.this, drawableResourceId, transaction);
                    calendarView.addDecorator(decorator);
                }
            }
        });

        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                datesViewModel.getTotalList(date);
                if (!datesViewModel.currentYear.getValue().equals(date.getYear() + "")) {
                    datesViewModel.currentYear.setValue(date.getYear() + "");
                    datesViewModel.getCurrentYearTotal(date);
                }
            }
        });

        datesViewModel.isLoading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            }
        });

        datesViewModel.totalAcceptedMonthlyLiveData.observe(this, s -> {
            totalMonthAccepted.setText("" + s);
            if (currentProduct != null && !currentProduct.getdOrM().isEmpty() && currentProduct.getdOrM().equals("D")) {
                int thisMonthPrice = Integer.parseInt(s != null ? s : "0");
                int singleProductPrice = currentProduct.getPrice();
                currentMonthPriceTextView.setText("₹" + (thisMonthPrice * singleProductPrice));
            }
        });
        datesViewModel.totalRejectedMonthlyLiveData.observe(this, s -> totalMonthRejected.setText("" + s));
        datesViewModel.totalPendingMonthlyLiveData.observe(this, s -> totalMonthPending.setText("" + s));

        datesViewModel.totalAcceptedYearlyLiveData.observe(this, s -> {
            if (currentProduct != null && !currentProduct.getdOrM().isEmpty() && currentProduct.getdOrM().equals("D")) {
                int thisYearPrice = Integer.parseInt(s != null ? s : "0");
                int singleProductPrice = currentProduct.getPrice();
                totalPriceTextView.setText("₹" + (thisYearPrice * singleProductPrice));
            }
            totalAccepted.setText("" + s);
        });
        datesViewModel.totalRejectedYearlyLiveData.observe(this, s -> totalRejected.setText("" + s));
        datesViewModel.totalPendingYearlyLiveData.observe(this, s -> totalPending.setText("" + s));


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void sendToReject(CalendarDay day, Transaction transaction) {
        HashMap<String, String> value = FirebaseUtils.getValueMapOfRequest(day, transaction.getQuantity(), Request.REJECTED);
        DatabaseReference reference = RealtimeDatabase.getInstance().getReference().child("Buss_Cust_DayWise").child(bussCustId);
        reference.child(FirebaseUtils.getDatePath(day))
                .setValue(value);
        FirebaseUtils.incrementAccToReq(day, reference, transaction.getQuantity(), Request.REJECTED);
        FirebaseUtils.decrementAccToReq(day, reference, transaction.getQuantity(), Request.PENDING);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void SendToAccept(CalendarDay day, Transaction transaction) {
        HashMap<String, String> value = FirebaseUtils.getValueMapOfRequest(day, transaction.getQuantity(), Request.ACCEPTED);
        DatabaseReference reference = RealtimeDatabase.getInstance().getReference().child("Buss_Cust_DayWise").child(bussCustId);
        reference.child(FirebaseUtils.getDatePath(day))
                .setValue(value);
        FirebaseUtils.incrementAccToReq(day, reference, transaction.getQuantity(), Request.ACCEPTED);
        FirebaseUtils.decrementAccToReq(day, reference, transaction.getQuantity(), Request.PENDING);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    //modifications

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void createPendingRequest(CalendarDay day, String quantity) {
        Log.e("TAG",""+bussCustId+" "+day.toString()+" "+quantity);
        DatabaseReference reference = RealtimeDatabase.getInstance().getReference().child("Buss_Cust_DayWise").child(bussCustId);
        HashMap<String, String> value = FirebaseUtils.getValueMapOfRequest(day, quantity, Request.PENDING);
        reference.child(FirebaseUtils.getDatePath(day))
                .setValue(value);
        FirebaseUtils.incrementAccToReq(day, reference, quantity, Request.PENDING);

        RequestNotification requestNotification = new RequestNotification()
                .setToken(SaveOfflineManager.getFireBaseToken(this))
                .setSendDataModel(new SendDataModel()
                        .setFromWhichPerson(SaveOfflineManager.getUserName(this))
                        .setFromWhichPersonID(SaveOfflineManager.getUserId(this))
                        .setNotificationStatus(Request.PENDING)
                        .setProductName(currentProduct.getName())
                        .setQuantity(quantity)
                        .setToWhichPerson(SaveOfflineManager.getUserName(this))
                        .setToWhichPersonId(custID));
        NotificationService.sendNotification(requestNotification);
    }
}
